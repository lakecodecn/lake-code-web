package cn.lakecode.web.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountUtils {

    /**
     * 元转分
     *
     * @param yuan 元
     * @return 分
     */
    public static Integer formatFen(BigDecimal yuan) {
        return yuan.multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 分转元
     *
     * @param fen 分
     * @return 元
     */
    public static BigDecimal formatYuan(Integer fen) {
        return new BigDecimal(fen).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }

}
