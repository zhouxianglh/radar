package com.ymt.radar;

import java.awt.Point;
import java.util.List;

/**
 * 雷达接口
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年1月25日
 */
public interface Radar {
    /**
     * 启动扫描
     *
     * @return 扫描结果
     */
    public List<Point> scan() throws Exception;

    /**
     * 启动雷达
     */
    public boolean start() throws Exception;

    /**
     * 关闭雷达
     */
    public boolean stop() throws Exception;
}
