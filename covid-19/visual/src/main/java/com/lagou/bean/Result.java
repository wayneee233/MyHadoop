package com.lagou.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Author jiang.weiyu
 * @Date 2021/01/31 5:45
 */
@Data
@NoArgsConstructor
public class Result {
    private Object data;
    private Integer status;
    private String message;

    static Result result = new Result();

    public static Result success(Object data){
        result.setStatus(200);
        result.setMessage("success");
        result.setData(data);

        return result;
    }

    public static Result fail(){
        result.setStatus(500);
        result.setMessage("fail");
        result.setData(null);

        return result;
    }
}
