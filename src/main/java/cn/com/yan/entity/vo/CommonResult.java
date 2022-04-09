package cn.com.yan.entity.vo;

import lombok.Data;

@Data
public class CommonResult {
    private Integer code;

    private String msg;

    private Object data;

    private Long total;

    public static CommonResult success(Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(1);
        commonResult.setMsg("success");
        commonResult.setData(data);
        return commonResult;
    }

    public static CommonResult success(Object data,Long total) {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(1);
        commonResult.setMsg("success");
        commonResult.setData(data);
        commonResult.setTotal(total);
        return commonResult;
    }

    public static CommonResult fail(Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.setCode(-1);
        commonResult.setMsg("fail");
        return commonResult;
    }
}
