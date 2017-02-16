package com.ymt.radar.impl;

import java.io.Serializable;

/**
 * 扫描点
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月8日
 */
public class StepBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 302849986816433437L;
    private int distance;
    private int step;

    public StepBean() {
        super();
    };

    public StepBean(int step, int distance) {
        super();
        this.step = step;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public int getStep() {
        return step;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setStep(int step) {
        this.step = step;
    }

}
