package com.lagou.controller;

import com.lagou.bean.Result;
import com.lagou.mapper.CovidMapper;
import com.lagou.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/31 5:44
 */
@Controller //@Controller + @ResponseBody
@RequestMapping("covid")
public class DataApp {
    @Autowired
    private CovidMapper mapper;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        modelMap.put("msg", "SpringBoot Ajax 示例");

        return "index";
    }

    @ResponseBody
    public String home() {

        return "home";
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
