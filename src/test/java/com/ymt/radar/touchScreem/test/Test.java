package com.ymt.radar.touchScreem.test;

import java.awt.Point;

import com.ymt.radar.Radar;
import com.ymt.radar.impl.CalculatePoint;
import com.ymt.radar.impl.RadarUST_10LX;
import com.ymt.radar.touchScreem.Control;
import com.ymt.radar.touchScreem.CoordinateAdapter;
import com.ymt.radar.touchScreem.DataHandle;
import com.ymt.radar.touchScreem.View;
import com.ymt.radar.touchScreem.Impl.ControlImpl;
import com.ymt.radar.touchScreem.Impl.CoordinateAdapterImpl;
import com.ymt.radar.touchScreem.Impl.DataHandleImpl;
import com.ymt.radar.touchScreem.Impl.DragView;

/**
 * 鼠标效果的实现
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月16日
 */
public class Test {
    public static void main(String[] args) {
        CalculatePoint calculate = new CalculatePoint(10, 50);
        Radar radar = RadarUST_10LX.getInstance("192.168.3.244", RadarUST_10LX.defaultPort, 0, calculate);
        Point limitA = new Point(-550, 950);
        //
        // View view = new MoveView(700, 1200, 768, 1280);// 鼠标移动效果
        // View view = new ClickView(700, 1200, 768, 1280);// 鼠标双击效果
        View view = new DragView(700, 1200, 768, 1280);// 鼠标拖拽效果
        //
        CoordinateAdapter adapter = new CoordinateAdapterImpl(limitA, view);
        DataHandle dataHandle = new DataHandleImpl();
        Control control = new ControlImpl(limitA, adapter, view, dataHandle, radar);
        control.run();
    }
}
