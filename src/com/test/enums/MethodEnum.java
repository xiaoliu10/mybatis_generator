package com.test.enums;

import java.util.Arrays;

/**
 * Description: mybatis_generator
 * Created by zhangxun27348 on 2019/8/15 18:38
 *
 * @author zhangxun27348
 */
public enum MethodEnum {

    deleteByPrimaryKey("根据主键删除一条数据"), insert("插入一条数据"), insertSelective("插入一条数据, 为null 不操作"),
    selectByPrimaryKey("根据主键查询一条数据"),updateByPrimaryKeySelective("根据主键更新数据, 为空不操作"),
    updateByPrimaryKey("根据主键更新数据");

    private String describe;

    MethodEnum(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public static MethodEnum nameOf(String name){
        return Arrays.asList(values()).parallelStream().filter(entity -> entity.name().equals(name))
                .findAny().orElse(null);
    }

}
