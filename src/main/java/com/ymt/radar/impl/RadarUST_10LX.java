package com.ymt.radar.impl;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ymt.radar.Radar;
import com.ymt.radar.utils.RadarUtils;

/**
 * UST-10LX 雷达实现类(可用于 UST-10LX,UST-20LX,同时兼容 UTM-30LX-EW).具体操作参考协议
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年1月24日
 */
public class RadarUST_10LX implements Radar {
    /**
     * 角度对应step
     */
    public static final int angelToStep = 4;
    /**
     * 默认结束step,对应180度角
     */
    private static final int def_endStep = 900;
    /**
     * 默认起始step,对应0度角
     */
    private static final int def_startStep = 180;
    /**
     * 激光雷达默认ID
     */
    public static final String defaultIP = "192.168.0.10";
    /**
     * 激光雷达默认端口
     */
    public static final int defaultPort = 10940;
    /**
     * 换行符
     */
    private static final byte finalChar = (byte) '\n';
    private static final Logger logger = LoggerFactory.getLogger(RadarUST_10LX.class);
    private static final ConcurrentHashMap<String, RadarUST_10LX> radarMap = new ConcurrentHashMap<>();
    /**
     * 扫描次数(多次扫描结果合并,用于减震计算)
     */
    private static final int scanCount = 3;

    /**
     * 按文档要求将三位字符组成的字符串转换成距离值
     */
    public static int converToInt(String data) {
        int value = 0;
        for (int i = 0; i < data.length(); i++) {
            value <<= 6;
            value &= ~0x3f;
            value |= data.charAt(i) - 0x30;
        }
        return value;
    }

    /**
     * 获取雷达操作对象
     *
     * @param host
     * @param port
     * @param startStep
     *            起始扫描范围
     * @param endStep
     *            起始扫描范围
     * @param deviation
     *            误差角度
     * @param calculatePoint
     *            角度转换器
     * @return
     */
    public static synchronized RadarUST_10LX getInstance(String host, int port, double deviation,
            CalculatePoint calculatePoint) {
        RadarUST_10LX radar = radarMap.get(host);
        if (null == radar) {
            radar = new RadarUST_10LX(host, port, deviation, calculatePoint);
            radarMap.put(host, radar);
        }
        return radar;
    }

    private CalculatePoint calculatePoint;
    private int endStep;
    private String host;
    private InputStream is = null;
    private OutputStream os = null;
    private int port;
    private Socket socket = null;
    private int startStep;

    /**
     *
     * @param host
     *            雷达IP
     * @param port
     *            雷达端口
     * @param startStep
     *            开始扫描范围
     * @param endStep
     *            结束扫描范围
     * @param deviation
     *            误差角度
     */
    private RadarUST_10LX(String host, int port, double deviation, CalculatePoint calculatePoint) {
        super();
        this.host = host;
        this.port = port;
        // (当前雷达为扇形270度角,step in (0-1080),如果使用180度用,startStep=180,endStep=900 ),,另注意雷达安装过程中的误差,适当调整step
        this.startStep = def_startStep + (int) (angelToStep * deviation);
        this.endStep = def_endStep + (int) (angelToStep * deviation);
        this.calculatePoint = calculatePoint;
    }

    private int[] decodeValue(byte[] returnValue) {
        String value = new String(returnValue);
        String[] datas = value.split("\n");
        logger.debug("执行命令:" + datas[0]);
        logger.debug("返回状态:" + datas[1]);
        logger.debug("执行时间:" + datas[2]);
        // 收集返回
        StringBuilder allValue = new StringBuilder();
        for (int i = 3, j = datas.length; i < j; i++) {
            allValue.append(datas[i].substring(0, datas[i].length() - 1));
        }
        //
        int flag = 0;
        int[] point = new int[endStep - startStep];
        for (int m = 3, n = allValue.length(); m < n; m += 3) {
            String distance = allValue.substring(m - 3, m);
            point[flag++] = converToInt(distance);
        }
        return point;
    }

    /**
     * 获取响应的返回值
     */
    private byte[] recive(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] byteArr = new byte[1024];
        int count = 0;
        while ((count = is.read(byteArr)) > -1) {
            os.write(byteArr, 0, count);
            byte[] byteCurrent = os.toByteArray();
            if (byteCurrent.length >= 2 && byteCurrent[byteCurrent.length - 2] == finalChar
                    && byteCurrent[byteCurrent.length - 1] == finalChar) {
                break;
            }
        }
        return os.toByteArray();
    }

    @Override
    public synchronized List<Point> scan() throws Exception {
        String cmd = String.format("GD%04d%04d%02d\n", startStep, endStep, 1);
        // 一次扫描实际上雷达执行多次扫描,然后求出平均值,用于减小误差
        List<String> list = new ArrayList<>();
        for (int i = 0; i < scanCount; i++) {
            list.add(cmd);
        }
        List<byte[]> returnValues = sendCommand(list.toArray(new String[0]));
        int[][] points = new int[returnValues.size()][];
        for (int i = 0, j = returnValues.size(); i < j; i++) {
            points[i] = decodeValue(returnValues.get(i));
        }
        return calculatePoint.run(points);
    }

    private byte[] sendAndRecive(String cmd) throws Exception {
        try {
            logger.debug("send command:" + cmd);
            os.write(cmd.getBytes());
            byte[] bytes = recive(is);
            logger.debug("command result:" + new String(bytes));
            return bytes;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    /**
     * 发送指令
     *
     * @param cmd
     * @return
     */
    private List<byte[]> sendCommand(String... cmds) throws Exception {
        List<byte[]> list = new ArrayList<>();
        sendAndRecive("BM\n");// 激光雷达启动
        for (String cmd : cmds) {
            list.add(sendAndRecive(cmd));
        }
        sendAndRecive("QT\n");// 激光雷达关闭
        return list;
    }

    @Override
    public synchronized boolean start() throws Exception {
        socket = new Socket(host, port);
        os = socket.getOutputStream();
        is = socket.getInputStream();
        // 初始化雷达设置
        sendAndRecive("SCIP2.0\n");
        sendAndRecive("QT\n");
        return true;
    }

    @Override
    public synchronized boolean stop() throws Exception {
        sendAndRecive("QT\n");
        RadarUtils.close(socket);
        return true;
    }
}
