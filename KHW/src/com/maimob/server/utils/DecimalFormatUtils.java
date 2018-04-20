package com.maimob.server.utils;

import java.math.BigDecimal;

/**
 * Created by yang on 2018/4/20.
 */
public class DecimalFormatUtils {

    public static double formatDouble2(double v) {
        BigDecimal decimal = new BigDecimal(v);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
