package com.ymt.radar.touchScreem.Impl;

import java.awt.AWTException;
import java.awt.Robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ymt.radar.touchScreem.View;

/**
 * 基础视图操作类
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public abstract class BaseView implements View {
    private static final Logger logger = LoggerFactory.getLogger(BaseView.class);
    protected int actheight;
    protected int actwidth;
    protected int viewHeight;
    protected int viewWidth;
    protected Robot robot;

    public BaseView() {
        super();
        getRobot();
    }

    /**
     * 获取鼠标操作对象
     */
    private void getRobot() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    public BaseView(int actheight, int actwidth, int viewHeight, int viewWidth) {
        super();
        getRobot();
        this.actheight = actheight;
        this.actwidth = actwidth;
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
    }

    public int getActheight() {
        return actheight;
    }

    public void setActheight(int actheight) {
        this.actheight = actheight;
    }

    public int getActwidth() {
        return actwidth;
    }

    public void setActwidth(int actwidth) {
        this.actwidth = actwidth;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    @Override
    public int getActHeight() {
        return actheight;
    }

    @Override
    public int getActWidth() {
        return actwidth;
    }

    @Override
    public int getViewHeight() {
        return viewHeight;
    }

    @Override
    public int getViewWidth() {
        return viewWidth;
    }

}
