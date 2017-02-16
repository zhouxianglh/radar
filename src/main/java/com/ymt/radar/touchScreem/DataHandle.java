package com.ymt.radar.touchScreem;

import java.awt.Point;

/**
 * 数据处理(如防抖操作)
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月14日
 */
public interface DataHandle {
    /**
     * 数据处理(减震等操作处理)
     */
    public Point handle(Point point);
}
