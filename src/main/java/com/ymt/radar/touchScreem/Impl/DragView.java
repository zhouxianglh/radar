package com.ymt.radar.touchScreem.Impl;

import java.awt.Point;
import java.awt.event.InputEvent;

/**
 * 鼠标拖动效果工具类
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public class DragView extends BaseView {

    public DragView(int actheight, int actwidth, int viewHeight, int viewWidth) {
        super(actheight, actwidth, viewHeight, viewWidth);
    }

    private boolean isPress = false;

    @Override
    public void execute(Point point) {
        if (null != point) {
            robot.mouseMove((int) point.getX(), (int) point.getY());
            if (!isPress) {
                isPress = true;
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            }
        } else {
            if (isPress) {
                isPress = false;
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        }
    }
}
