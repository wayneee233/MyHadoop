package com.lagou.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 1:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CovidData {
    // date
    private Date date;
    // province
    private String name_jp;
    // patients
    private String npatients;
}
