package com.lagou.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/31 8:17
 */
@Mapper
@Component
public interface CovidMapper {
    /*    @Select("SELECT name_jp,SUM(npatients) FROM covid19 GROUP BY name_jp")
        Map<String,Object> getCovidDataAll();*/
    @Select("SELECT name_jp,SUM(npatients) as total FROM covid19 GROUP BY name_jp")
    List<Map<String, Object>> getCovidDataAll();

    @Select("SELECT name_jp,npatients FROM covid19  WHERE DATE = #{date}")
    List<Map<String, Object>> getCovidDataByDate(Date date);
}
