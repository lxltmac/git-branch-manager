package com.geostar.geostack.git_branch_manager.pojo;

import lombok.Data;

@Data
public class CodingDescribeDepotMergeReq {
    private String DepotId;
    private String DepotPath;
    private String Status;
}
