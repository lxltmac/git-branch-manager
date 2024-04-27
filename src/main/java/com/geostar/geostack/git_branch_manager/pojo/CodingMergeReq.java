package com.geostar.geostack.git_branch_manager.pojo;

import lombok.Data;

@Data
public class CodingMergeReq {
    private String Content;
    private String DepotId;
    private String DepotPath;
    private String DestBranch;
    private String Reviewers;
    private String SrcBranch;
    private String Title;
}
