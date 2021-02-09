package com.lagou.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author jiang.weiyu
 * @Date 2021/2/9 16:59
 */
public class test01 {
    public static void main(String[] args) {
        String str = "ADDA";
        String[] filed = str.split("");

        Map<String,Integer> map = new HashMap<>();

        for (String s : filed) {
            System.out.println(s);
            // 不存在则添加 值为0
            if (!map.containsKey(s)) {
                map.put(s,1);
            }else {
                // 已经有的话+1
                map.put(s,map.get(s)+1);
            }
        }

        int max = 0;
        int temp = 0;

        for (String key : map.keySet()) {
            temp = map.get(key);
            if (max < temp){
                max = temp;
            }
        }

        if (map.containsKey("*")){
            max += 1;
        }

        if (max == 1) {
            System.out.println("NoPair");
        } else if (max == 2 && map.size() != 2) {
            System.out.println("OnePair");
        } else if (max == 3) {
            System.out.println("ThreeCard");
        } else if (max == 4) {
            System.out.println("FourCard");
        } else {
            System.out.println("TwoPair");
        }
    }
}
