package com.ymt.radar.test;

import java.awt.Point;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ymt.radar.impl.CalculatePoint;
import com.ymt.radar.impl.RadarUST_10LX;

/**
 * RadarUST_10LX 测试用例
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年1月25日
 */
public class RadarUST_10LX_Test {
    private RadarUST_10LX radar;

    @Before
    public void init() {
        CalculatePoint calculate = new CalculatePoint(5, 50);
        radar = RadarUST_10LX.getInstance("192.168.3.244", RadarUST_10LX.defaultPort, 0, calculate);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testScan() throws Exception {
        radar.start();
        List<Point> list = radar.scan();
        for (Point point : list) {
            System.out.println(point);
        }
        radar.stop();
    }

    /**
     * 扫描测试用例
     */
    // @Test
    public void testGetInstance() {
        CalculatePoint calculate = new CalculatePoint(2, 20);
        RadarUST_10LX radar1 = RadarUST_10LX.getInstance("192.168.3.244", RadarUST_10LX.defaultPort, 0, calculate);
        RadarUST_10LX radar2 = RadarUST_10LX.getInstance("192.168.3.244", RadarUST_10LX.defaultPort, 0, calculate);
        Assert.assertEquals(radar1, radar2);
        Assert.assertEquals(radar, radar2);
    }
}
