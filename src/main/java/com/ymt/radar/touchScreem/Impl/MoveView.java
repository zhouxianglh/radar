package com.ymt.radar.touchScreem.Impl;

import java.awt.Point;

/**
 * 鼠标移动效果工具类
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public class MoveView extends BaseView {

    public MoveView(int actheight, int actwidth, int viewHeight, int viewWidth) {
        super(actheight, actwidth, viewHeight, viewWidth);
    }

    @Override
    public void execute(Point point) {
        if (null == point) {
            return;
        }
        robot.mouseMove((int) point.getX(), (int) point.getY());
    }

}
