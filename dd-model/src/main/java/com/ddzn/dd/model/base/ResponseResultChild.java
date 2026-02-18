package com.ddzn.dd.model.base;

import lombok.Data;

/**
 *
 */
@Data
public class ResponseResultChild {
    /**
     * 数据集合
     */
    private Object list;
    /**
     * 数据量
     */
    private int count;
    /**
     * 数量
     */
    private int num;

}
