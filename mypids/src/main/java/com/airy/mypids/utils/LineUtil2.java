package com.airy.mypids.utils;

import android.content.Context;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class LineUtil2 {
    public final static int TRANSLATE_TO_PINYIN = 1;
    public final static int TRANSLATE_TO_PINYIN_WITH_BREAK = 2;
    public final static int TRANSLATE_BY_DICT = 3;
//    public static int TRANSLATE_BY_NET = 4;

    private static HashMap<String, String> englishDictionary;

    private LineUtil2(){}

    // test only
//    public static Line getFakeLineItem(Context context){
//        Line line = new Line("229"
//                , new String[]{"罗冲围总站（松南路）","罗冲围客运站","富力半岛花园","西场电器城","和平新村","东风西路","市少年宫","广医"
//                        ,"中山纪念堂","广东大厦","东风中路","正骨医院","大沙头游船码头","远安路","滨江东路","珠江泳场","中大北门西"
//                        ,"中大北门","中信乐涛苑","下渡路口","鹭江","珠影","赤岗路口","赤岗","磨碟沙","海港花园","广交会展馆","琶洲"
//                        ,"琶洲大桥南","琶洲塔","琶洲村","万胜围","石基村口","黄埔村南村口","琶洲石基村总站"}
//                , LineLanguage.Chinese);
//        return inflateEnglish(line, context);
//    }
//
//    public static Line inflateEnglish(Line line, Context context){
//        line.languageList.add(LineLanguage.English);
//        for (Station curr: line.stationItemList) {
//            curr.stationNameList.add(getEnglish(curr.stationNameList.get(0), context));
//        }
//        return line;
//    }

    public static String getEnglish(String text, Context context){
        switch (getTranslateMode()){
            case TRANSLATE_TO_PINYIN:
                char[] temp = getPinyin(text).toCharArray();
                if(temp.length>0)temp[0] = Character.toUpperCase(temp[0]);
                return new String(temp);
            case TRANSLATE_TO_PINYIN_WITH_BREAK:
                return getPinyinWithBreak(text);
            case TRANSLATE_BY_DICT:
                return getEnglishByDict(text, context);
        }
        return null;
    }

    private static String getPinyinWithBreak(String text) {
        JiebaSegmenter segment = new JiebaSegmenter();
        StringBuilder res = new StringBuilder();
        List<SegToken> list = segment.process(text, JiebaSegmenter.SegMode.SEARCH);
        for (int i = 0; i<list.size(); i++) {
            String key = list.get(i).word;
            char[] temp = getPinyin(key).toCharArray();
            if(temp.length>0)temp[0] = Character.toUpperCase(temp[0]);
            res.append(temp);
            res.append(" ");
        }
        return res.deleteCharAt(res.length()-1).toString();
    }

    private static String getPinyin(String text) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            return PinyinHelper.toHanYuPinyinString(text, format, "", true);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            return "";
        }
    }

    private static String getEnglishByDict(String text, Context context){
        HashMap<String, String> dictionary = getEnglishDictionary(context);
        JiebaSegmenter segment = new JiebaSegmenter();
        StringBuilder res = new StringBuilder();
        List<SegToken> list = segment.process(text, JiebaSegmenter.SegMode.SEARCH);
        for (int i = 0; i<list.size(); i++) {
            String key = list.get(i).word;
            if(dictionary.containsKey(key)){
                res.append(dictionary.get(key));
            }else{
                char[] temp = getPinyin(key).toCharArray();
                if(temp.length>0)temp[0] = Character.toUpperCase(temp[0]);
                res.append(temp);
            }
            res.append(" ");
        }
        return res.deleteCharAt(res.length()-1).toString();
    }

    // testing
    public static int getTranslateMode(){
        return TRANSLATE_TO_PINYIN;
    }

    public static HashMap<String, String> getEnglishDictionary(Context context){
        if(englishDictionary!=null)return englishDictionary;
        englishDictionary = new HashMap<>();
        try {
            InputStream inputStream = context.getResources().getAssets().open("myDict.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while((line = reader.readLine())!=null){
                int i = 0;
                for (; i < line.length(); i++) {
                    if(line.charAt(i)=='\t')break;
                }
                englishDictionary.put(line.substring(0,i), line.substring(i+1, line.length()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return englishDictionary;
    }
}
