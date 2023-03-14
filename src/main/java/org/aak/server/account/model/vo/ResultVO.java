package org.aak.server.account.model.vo;

import lombok.Data;
import org.aak.server.account.emuns.ResultCode;

@Data
public class ResultVO<T> {

    /**
     * 状态码, 默认1000是成功
     */
    private int code;
    /**
     * 响应信息, 来说明响应情况
     */
    private String message;
    /**
     * 响应的具体数据
     */
    private T data;

    public ResultVO(T data) {
        this(ResultCode.SUCCESS, data);
    }

    public ResultVO(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResultVO(String message, T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = message;
        this.data = data;
    }

    public ResultVO(ResultCode resultCode, String message, T data) {
        this.code = resultCode.getCode();
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("{\"code\":%d,\"message\":\"%s\",\"data\":\"%s\"}", code, message, data.toString());
    }
}
