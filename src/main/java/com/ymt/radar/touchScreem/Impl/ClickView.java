package com.ymt.radar.touchScreem.Impl;

import java.awt.Point;
import java.awt.event.InputEvent;

/**
 * 鼠标双击效果工具类
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public class ClickView extends BaseView {

    public ClickView(int actheight, int actwidth, int viewHeight, int viewWidth) {
        super(actheight, actwidth, viewHeight, viewWidth);
    }

    private boolean isPress = false;

    @Override
    public void execute(Point point) {
        if (null != point) {
            if (!isPress) {
                isPress = true;
                robot.mouseMove((int) point.getX(), (int) point.getY());
                mouseClick();
                mouseClick();
            }
        } else {
            if (isPress) {
                isPress = false;
            }
        }
    }

    /**
     * 鼠标左键单击
     */
    private void mouseClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

}
