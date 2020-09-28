package com.siyun.mr.wcsort;

import org.apache.hadoop.io.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class WordCountBean implements WritableComparable<WordCountBean> {
    // 定义属性
    private String word;
    private Long count;

    public WordCountBean() {
    }

    public WordCountBean(String word, Long count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        word = word;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    /*
    利用自定义的compareTo方法来实现排序效果
     */
    @Override
    public int compareTo(WordCountBean o) {
        if(count > o.count){
            return -1;
        }else if(count < o.count){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public void write(DataOutput Output) throws IOException {
        Output.writeUTF(word);
        Output.writeLong(count);
    }

    @Override
    public void readFields(DataInput Input) throws IOException {
        this.word = Input.readUTF();
        this.count = Input.readLong();
    }

    @Override
    public String toString() {
        return "Word=" + word +
                ", count=" + count ;
    }
}
