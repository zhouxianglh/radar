package com.ymt.radar.touchScreem.Impl;

import java.awt.Point;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ymt.radar.Radar;
import com.ymt.radar.touchScreem.Control;
import com.ymt.radar.touchScreem.CoordinateAdapter;
import com.ymt.radar.touchScreem.DataHandle;
import com.ymt.radar.touchScreem.View;

/**
 * 控制器的实现
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public class ControlImpl implements Control {
    private static final Logger logger = LoggerFactory.getLogger(ControlImpl.class);
    private CoordinateAdapter adapter;
    private DataHandle dataHandle;
    //
    private Point limitA;
    private Point limitB;
    private Point limitC;
    private Radar radar;
    private View view;

    public ControlImpl(Point limitA, CoordinateAdapter adapter, View view, DataHandle dataHandle, Radar radar) {
        super();
        this.adapter = adapter;
        this.view = view;
        this.dataHandle = dataHandle;
        this.radar = radar;
        this.limitA = limitA;
        getView();
    }

    /**
     * 获取触摸点(这里只支持单点操作)
     */
    private Point getScanPoint(List<Point> list) throws Exception {
        // logger.info("---------startPrint------------");
        for (Point point : list) {
            if (isLimit(point)) {
                // logger.info(point + "");
                return point;// 遇到合适的点,直接返回,后面的点就不管了
            }
        }
        // logger.info("---------endPrint------------");
        return null;
    }

    /**
     * 获取屏幕所在位置(根据原始点坐标,计划出对应四个点坐标)
     */
    private void getView() {
        limitB = new Point(limitA.x, limitA.y + view.getActHeight());
        limitC = new Point(limitA.x + view.getActWidth(), limitA.y);
    }

    /**
     * 当前点是否在屏幕中
     */
    private boolean isLimit(Point point) {
        return point.x > limitA.x && point.x < limitC.x && point.y > limitA.y && point.y < limitB.y;
    }

    @Override
    public void run() {
        try {
            radar.start();
            while (true) {
                List<Point> points = radar.scan();
                Point point = getScanPoint(points);
                Point handlePoint = dataHandle.handle(point);
                Point adapterPoint = adapter.conver(handlePoint);
                view.execute(adapterPoint);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

}
