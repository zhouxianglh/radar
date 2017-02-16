package com.ymt.radar.touchScreem;

import java.awt.Point;

/**
 * 显示视图
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月14日
 */
public interface View {
    /**
     * 显示屏的实际高度
     */
    public int getActHeight();

    /**
     * 显示屏的实际宽度
     */
    public int getActWidth();

    /**
     * 显示屏分辨率的高
     */
    public int getViewHeight();

    /**
     * 显示屏分辨率的宽
     */
    public int getViewWidth();

    /**
     * 执行操作
     */
    public void execute(Point point);

}
