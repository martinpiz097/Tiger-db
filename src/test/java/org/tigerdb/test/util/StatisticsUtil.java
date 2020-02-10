package org.tigerdb.test.util;

public class StatisticsUtil {
    public static String elementsPerSeconds(int elementCuont, double taskTime) {
        double elementsPerSec = (double) elementCuont / taskTime;
        String elemPerSecStr = TimeUtil.getNumberFormat("#0.000").format(elementsPerSec);
        return elemPerSecStr.concat(" elements per second");
    }
}
