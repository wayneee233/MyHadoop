package com.lagou.controller;

import com.lagou.bean.Result;
import com.lagou.mapper.CovidMapper;
import com.lagou.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/31 5:44
 */
@Controller//@Controller + @ResponseBody JSON形式
@RequestMapping("covid")
public class DataApp {
    @Autowired
    private CovidMapper mapper;


    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/data",method = RequestMethod.GET)
    @ResponseBody
    @CrossOrigin
    private Result getData(){
        System.out.println("接收到前端的JSON数据请求,后续查询mysql并返回");
        List<Map<String,Object>> covidDataAll = mapper.getCovidDataByDate(Date.valueOf(TimeUtils.getDate()));

        return Result.success(covidDataAll);
    }
}
