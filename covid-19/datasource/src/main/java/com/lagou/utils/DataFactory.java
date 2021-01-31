package com.lagou.utils;

import com.alibaba.fastjson.JSON;
import com.lagou.bean.CovidData;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 3:57
 */
public class DataFactory {
    public static List<CovidData> getHtml() {
        String html = HttpUtils.getHtml("https://opendata.corona.go.jp/api/Covid19JapanAll?date="+TimeUtils.getDate());
        String pattern = "\\[(.*)\\]";

        Pattern reg = Pattern.compile(pattern);
        Matcher matcher = reg.matcher(html);

        String jsonStr = "";
        if (matcher.find()) {
            jsonStr = matcher.group(0);
        }
        List<CovidData> covidDataList = JSON.parseArray(jsonStr, CovidData.class);
        Collections.reverse(covidDataList);

        return covidDataList;
    }
}
