package com.surge.common.core.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

import java.util.List;
import java.util.Map;

public class PinyinUtils {
    public static void main(String[] args) {
        String chinese = "你ni好";
        String pinyin = convertToPinyin(chinese);
        System.out.println(pinyin);
    }

    public static String convertToPinyin(String chinese) {
        // 设置不带声调的输出选项
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(net.sourceforge.pinyin4j.format.HanyuPinyinToneType.WITHOUT_TONE);
        StringBuilder output = new StringBuilder();
        for (char c : chinese.toCharArray()) {
            try {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    output.append(pinyinArray[0]);
                } else {
                    output.append(c);
                }
            } catch (Exception e) {
                output.append(c);
            }
        }
        return output.toString().trim();
    }

//    public static String fieldToPinyin(Map<String, FieldBO> fields, String replace) {
//        String fileToPinyin = ChineseToPinyin.convertToPinyin(replace);
//        while (fields.get(fileToPinyin) != null) {
//            fileToPinyin += 1;
//        }
//        return fileToPinyin;
//    }
//
//    public static String fieldToPinyin(List<String> fields, String replace) {
//        String fileToPinyin = ChineseToPinyin.convertToPinyin(replace);
//        while (fields.contains(fileToPinyin)) {
//            fileToPinyin += 1;
//        }
//        return fileToPinyin;
//    }
}