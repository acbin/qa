package com.bingo.qa.common;

/**
 * @author bingo
 * @since 2018/8/15
 */

public enum CommonRespConst {
    /**
     * 响应类型
     */
    SUCCESS(1, "success"),
    FAIL(0, "fail"),
    NOTHING_FOUND(2, "find nothing"),
    REQUEST_INVALID(3, "illegal request"),
    UNKNOWN_ERR(-1, "unknown exception");


    private int code;
    private String msg;

    CommonRespConst(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
