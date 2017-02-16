package com.ymt.radar.touchScreem.Impl;

import java.awt.Point;

import com.ymt.radar.touchScreem.CoordinateAdapter;
import com.ymt.radar.touchScreem.View;

/**
 * 坐标适配(光雷达坐标转换为显示器坐标)
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public class CoordinateAdapterImpl implements CoordinateAdapter {
    private Point limitA;
    private View view;

    public CoordinateAdapterImpl(Point limitA, View view) {
        super();
        this.limitA = limitA;
        this.view = view;
    }

    @Override
    public Point conver(Point point) {
        if (null == point) {
            return null;
        }
        int x = (int) (point.x - limitA.getX());
        int y = (int) (point.y - limitA.getY());
        return new Point(x * view.getViewWidth() / view.getActWidth(),
                view.getViewHeight() - (y * view.getViewHeight() / view.getActHeight()));
    }

}
