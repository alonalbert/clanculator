package com.karamba.clanculator.utils;

import android.text.TextUtils;
import com.karamba.clanculator.data.Time;

import java.util.LinkedList;

/**
 * todo: javadocs.
 */
public class Utils {
    public static String camelCaseToUnderscore(String s) {
        final char[] chars = s.toCharArray();

        final LinkedList<Integer> list = new LinkedList<Integer>();
        list.add(0);

        // get all upper case letter index positions
        for (int i = 1; i < chars.length; i++) {
            final char c = chars[i];
            //add more interested index beyond upper case letter
            if (c >= 65 && c <= 90) {
                list.add(i);
            }
        }

        final LinkedList<String> stringList = new LinkedList<String>();

        // this handles the all lower letter case
        if (list.size() == 1) {
            return s;
        }

        int prev = 0;
        int curr = 0;
        int begin = 0;
        for (int k = 1; k < list.size(); k++) {
            curr = list.get(k);

            if (curr == s.length() - 1){
                break;
            }
            if (curr == prev + 1 && curr != s.length() - 1) {
                prev = curr;
            } else if(curr == prev + 1 &&  curr == s.length() - 1){
                stringList.add(s.substring(begin, curr + 1).toLowerCase());
            } else {
                stringList.add(s.substring(prev, curr).toLowerCase());
                prev = curr;
                begin = curr;
                if (k == list.size() - 1) {
                    stringList.add(s.substring(curr, s.length()).toLowerCase());
                }
            }
        }
        return TextUtils.join("_", stringList);
    }

    public static String longToAmount(long value) {
        if (value < 1000) {
            return String.valueOf(value);
        } else if (value < 1000000) {
            return String.valueOf((int) Math.round(((double) value) / 1000)) + "k";
        } else if (value < 1000000000) {
            return String.valueOf((int) Math.round(((double) value) / 1000000)) + "m";
        } else {
            return String.valueOf((int) Math.round(((double) value) / 1000000000)) + "b";
        }
    }

    public static String longToTime(long value) {
        if (value < Time.MIN_1) {
            return String.valueOf(value) + "s";
        } else if (value < Time.HOUR_1) {
            return String.valueOf((int) Math.round(((double) value) / Time.MIN_1)) + "m";
        } else if (value < Time.DAY_1) {
            return String.valueOf((int) Math.round(((double) value) / Time.HOUR_1)) + "h";
        } else {
            return String.valueOf((int) Math.round(((double) value) / Time.DAY_1)) + "d";
        }
    }
}
