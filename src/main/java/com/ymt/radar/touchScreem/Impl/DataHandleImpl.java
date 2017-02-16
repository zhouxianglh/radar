package com.ymt.radar.touchScreem.Impl;

import java.awt.Point;

import com.ymt.radar.touchScreem.DataHandle;

/**
 * 数据处理
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public class DataHandleImpl implements DataHandle {

    @Override
    public Point handle(Point point) {
        // 在雷达扫描时的处理已经满足要求了,所以这里也不需要做会什么
        return point;
    }

}
