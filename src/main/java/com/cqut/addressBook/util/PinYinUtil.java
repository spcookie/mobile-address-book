package com.cqut.addressBook.util;

import com.rnkrsoft.bopomofo4j.Bopomofo4j;
import com.rnkrsoft.bopomofo4j.ToneType;

/**
 * @author Augenstern
 * @date 2021/12/5
 */
public class PinYinUtil {

    static {
        Bopomofo4j.local();
    }

    public static String pinyin(String word) {
        return Bopomofo4j.pinyin(word, ToneType.WITHOUT_TONE, false, true, "-");
    }

    public static int comparePinYin(String pin1, String pin2) {
        String[] s1 = pin1.split("-");
        String[] s2 = pin2.split("-");
        if (s1.length == s2.length) {
            for (int j = 0; j < s1.length; j++) {
                int val = s1[j].charAt(0) - s2[j].charAt(0);
                if (val != 0) {
                    return val;
                }
            }
            return 0;
        } else {
            return s1.length - s2.length;
        }
    }
}
