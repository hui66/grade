package cn.com.yan.entity.dto;

import lombok.Data;

@Data
public class PageInfo {
    private Integer currentPage;
    private Integer pageSize;

    private String id;
    private  String rate;

}
