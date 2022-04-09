package cn.com.yan.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GradeUtils {

    /**
     *
     * @param value1 平时成绩
     * @param value2 期末成绩
     * @return 最终成绩  平时成绩*0.3+期末成绩*0.7
     */
    public static String gradeSum(String value1,String value2){
        BigDecimal b1 = new BigDecimal(value1).multiply(new BigDecimal("0.3"));
        BigDecimal b2 = new BigDecimal(value2).multiply(new BigDecimal("0.7"));
        BigDecimal total = b1.add(b2);
        return total.setScale(1).toPlainString();
    }

    /**
     * 能力指标计算
     * @param value 指标比例
     * @param skillValue 指标值
     * @return 能力指标成绩
     */
    public static String skillCal(String value, String skillValue){
        BigDecimal b2 = new BigDecimal(value).multiply(new BigDecimal(skillValue));
        return b2.setScale(1).toPlainString();
    }

    public static String getCourseRate(String value) {
        BigDecimal s100 = new BigDecimal(100);
        BigDecimal s95 = new BigDecimal(95);
        BigDecimal s90 = new BigDecimal(90);
        BigDecimal s85 = new BigDecimal(85);
        BigDecimal s80 = new BigDecimal(80);
        BigDecimal s75 = new BigDecimal(75);
        BigDecimal s70 = new BigDecimal(70);
        BigDecimal s65 = new BigDecimal(65);
        BigDecimal s60 = new BigDecimal(60);
        BigDecimal s55 = new BigDecimal(55);
        BigDecimal rate;
        BigDecimal svalue = new BigDecimal(value);
        if(svalue.compareTo(s100) <= 0 && svalue.compareTo(s95)>=0){
            rate = new BigDecimal(1);
        }else if(svalue.compareTo(s95) < 0 && svalue.compareTo(s90)>=0){
            rate = new BigDecimal(0.95);
        }else if(svalue.compareTo(s90) < 0 && svalue.compareTo(s85)>=0){
            rate = new BigDecimal(0.90);
        }else if(svalue.compareTo(s85) < 0 && svalue.compareTo(s80)>=0){
            rate = new BigDecimal(0.85);
        }else if(svalue.compareTo(s80) < 0 && svalue.compareTo(s75)>=0) {
            rate = new BigDecimal(0.80);
        }else if(svalue.compareTo(s75) < 0 && svalue.compareTo(s70)>=0) {
            rate = new BigDecimal(75);
        }else if(svalue.compareTo(s70) < 0 && svalue.compareTo(s65)>=0) {
            rate = new BigDecimal(0.70);
        }else if(svalue.compareTo(s65) < 0 && svalue.compareTo(s60)>=0) {
            rate = new BigDecimal(0.65);
        }else if(svalue.compareTo(s60) < 0 && svalue.compareTo(s55)>=0) {
            rate = new BigDecimal(0.6);
        }else {
            rate = new BigDecimal(0);
        }
        return rate.setScale(1, RoundingMode.HALF_UP).toPlainString();
    }
}
