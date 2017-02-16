package com.ymt.radar.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于筛选,并将激光雷达的点转换为坐标点
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年2月8日
 */
public class CalculatePoint {
    /**
     * 扫描结果在指定范围内则认为是正确的
     */
    private static final int MAX_Distanse = 50;
    private int distance;

    private int groupSize;

    /**
     *
     * @param groupSize
     *            至少多少个雷达点合成一个坐标点
     * @param distance
     *            两个雷达点的距离多少以内才视为一组(单位:毫米)
     */
    public CalculatePoint(int groupSize, int distance) {
        this.groupSize = groupSize;
        this.distance = distance;
    }

    private void addPoint(int[] points, List<StepBean> values, List<StepBean> group, int i) {
        if (group.size() >= groupSize) {// 点数较多,合并为一点
            StepBean bean = null;
            int distance = Integer.MAX_VALUE;
            for (StepBean stepBean : group) {
                if (stepBean.getDistance() < distance) {// 取最接近雷达的坐标点,目测这个点的位置是最准确的
                    distance = stepBean.getDistance();
                    bean = stepBean;
                }
            }
            values.add(bean);
        }
        group.clear();
        group.add(new StepBean(i, points[i]));
    }

    /**
     * 筛选,把多个点合并成一个点
     */
    private void countPoint(List<StepBean> values, int length, int[] totalArr, int[]... points) {
        List<StepBean> group = new ArrayList<>();
        group.add(new StepBean(0, totalArr[0]));
        for (int i = 1, j = length; i < j; i++) {
            if (totalArr[i] == 0) {// 偏移量太大,没作统计的点
                continue;
            }
            int before = group.get(group.size() - 1).getDistance();
            if (Math.abs(totalArr[i] - before) < distance) {// 如果两个点之间的距离超出,则认为是两个不同的点
                group.add(new StepBean(i, totalArr[i]));
            } else {
                addPoint(totalArr, values, group, i);
            }

        }
        addPoint(totalArr, values, group, points.length - 1);
    }

    /**
     * 多次扫描,合并扫描结果
     */
    private int[] getPoint(int length, int[]... points) {
        int size = points.length;
        int[] totalArr = new int[length];
        for (int i = 0, j = length; i < j; i++) {
            int count = 0;
            int total = 0;
            for (int m = 0, n = size - 1; m < n; m++) {
                // 多次扫描结果误差小于指定值,则认为此次扫描是有效的,则计处统计,否则无视它
                if (Math.abs(points[m][i] - points[m + 1][i]) < MAX_Distanse) {
                    total += points[m][i];
                    count++;
                }
            }
            if (0 != count && 0 != total) {
                totalArr[i] = total / count;
            }
        }
        return totalArr;
    }

    /**
     * 坐标点计算
     */
    public List<Point> run(int[]... points) {
        List<Point> result = new ArrayList<>();
        List<StepBean> values = new ArrayList<>();
        // 多次扫描,合并扫描结果
        int length = points[0].length;
        int[] totalArr = getPoint(length, points);
        // 筛选,把多个点合并成一个点
        countPoint(values, length, totalArr, points);
        // 计算坐标点
        for (StepBean stepBean : values) {
            double angle = ((double) stepBean.getStep()) / RadarUST_10LX.angelToStep;
            int x = (int) (stepBean.getDistance() * Math.cos(Math.toRadians(angle)));
            int y = (int) (stepBean.getDistance() * Math.sin(Math.toRadians(angle)));
            result.add(new Point(x, y));
        }
        return result;
    }
}
