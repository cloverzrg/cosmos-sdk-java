package com.jeongen.cosmos.util;

import java.math.BigDecimal;
import java.math.BigInteger;

// 1 atom = 1,000,000 uATOM
// 	atom  = "atom"  // 1 (base denom unit)
//	matom = "matom" // 10^-3 (milli)
//	uatom = "uatom" // 10^-6 (micro)
//	natom = "natom" // 10^-9 (nano)
// https://blog.cosmos.network/phase-ii-initiated-cosmos-atom-transfers-enabled-by-governance-831a7e555ab6
public class ATOMUnitUtil {

    // uatom 即 micro atom
    public static BigDecimal microAtomToAtom(String uatomString) {
        BigDecimal uatom = new BigDecimal(uatomString);
        return uatom.movePointLeft(6).stripTrailingZeros();
    }

    public static BigDecimal microAtomToAtom(BigInteger uatomBigInteger) {
        BigDecimal uatom = new BigDecimal(uatomBigInteger);
        return uatom.movePointLeft(6).stripTrailingZeros();
    }

    public static BigDecimal atomToMicroAtom(String atomVal) {
        BigDecimal atom = new BigDecimal(atomVal);
        return atomToMicroAtom(atom);
    }

    public static BigDecimal atomToMicroAtom(BigDecimal atom) {
        return atom.movePointRight(6).stripTrailingZeros();
    }

    public static BigInteger atomToMicroAtomBigInteger(BigDecimal atom) {
        BigDecimal bigDecimal = atom.movePointRight(6);
        if (getNumberOfDecimalPlaces(bigDecimal) != 0) {
            throw new RuntimeException("atom to uAtom: 转换成整数后，含有小数点:" + bigDecimal);
        }
        // 忽略小数位
        return bigDecimal.toBigInteger();
    }

    // 小数位位数
    public static int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
        String string = bigDecimal.stripTrailingZeros().toPlainString();
        int index = string.indexOf(".");
        return index < 0 ? 0 : string.length() - index - 1;
    }

}
