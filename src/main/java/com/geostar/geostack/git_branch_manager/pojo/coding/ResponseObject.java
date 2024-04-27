package com.geostar.geostack.git_branch_manager.pojo.coding;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseObject implements Serializable {
    private DataObject Data;
    private String RequestId;

    // 省略 getter 和 setter 方法
}
