package com.panda.utils;

/**
 * @ClassName PandaNumberUtils
 * @Description 请描述类的业务用途
 * @Author guoshunfa
 * @Date 2022/3/2 10:13 AM
 * @Version 1.0
 **/
public class PandaNumberUtils {

    /**
     * 求和
     *
     * @param data double数组
     * @return
     */
    public static double sum(double[] data) {
        double sum = 0;
        for (int i = 0; i < data.length; i++)
            sum = sum + data[i];
        return sum;
    }

    /**
     * 平均数
     *
     * @param data double数组
     * @return
     */
    public static double avg(double[] data) {
        double mean = 0;
        mean = sum(data) / data.length;
        return mean;
    }

    /**
     * 总体方差
     *
     * @param data double数组
     * @return
     */
    public static double popVariance(double[] data) {
        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance = variance + (Math.pow((data[i] - avg(data)), 2));
        }
        variance = variance / data.length;
        return variance;
    }

    /**
     * 总体标准差
     *
     * @param data double数组
     * @return
     */
    public static double popStdDev(double[] data) {
        double std_dev;
        std_dev = Math.sqrt(popVariance(data));
        return std_dev;
    }

    /**
     * 样本方差
     *
     * @param data double数组
     * @return
     */
    public static double sampleVariance(double[] data) {
        double variance = 0;
        for (int i = 0; i < data.length; i++) {
            variance = variance + (Math.pow((data[i] - avg(data)), 2));
        }
        variance = variance / (data.length - 1);
        return variance;
    }

    /**
     * 样本标准差
     *
     * @param data
     * @return
     */
    public static double sampleStdDev(double[] data) {
        double std_dev;
        std_dev = Math.sqrt(sampleVariance(data));
        return std_dev;
    }

    public static void main(String[] args) {
        double[] array = {48.1, 98.2, 62.1, 74.2, 78.9, 63.4, 72.9};
        double average = avg(array); // 平均值
        double standarDeviation = sampleStdDev(array); // 样本标准差

        System.out.println("原值：48.1, 98.2, 62.1, 74.2, 78.9, 63.4, 72.9");
        System.out.println("平均值：" + average);
        System.out.println("样本标准差：" + standarDeviation);
        System.out.println("可行区间：" + (average - 2 * standarDeviation) + "-" + (average + 2 * standarDeviation));
    }
}
