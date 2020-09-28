package com.ecoeler.util;

import java.text.DecimalFormat;
import java.util.Optional;

/**
 * 比率工具
 * @author tangcx
 */
public class RatioUtil {
    private RatioUtil(){}

    /**
     * 环比或者同比
     * @param last
     * @param now
     * @return
     */
    public static float getCompareRatio(Integer last,Integer now){
        last= Optional.ofNullable(last).orElse(0);
        now=Optional.ofNullable(now).orElse(0);
        if (last.equals(now)){
            return 0.0f;
        }
        if(last==0){
            return 1.0f;
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        float result=(float)(now-last)/(float)last;
        return Float.parseFloat(df.format(result));
    }
}
