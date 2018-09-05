package com.bingo.qa.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * 新鲜事
 *
 * @author bingo
 * @since 2018/6/2
 */
@Data
public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createdDate;

    /**
     * JSON格式
     */
    private String data;

    private JSONObject dataJSON = null;

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
