package com.geostar.geostack.git_branch_manager.pojo;

import lombok.Data;

@Data
public class CodingMergeReq {
    /**
     * {
     *   "Content": "oa",
     *   "DepotId": "9168854",
     *   "DestBranch": "huawei-prod",
     *   "Reviewers": "EgtknuPCSd,qSoWQyZJfS,uFKDabiekA,DvjWXJhTkS,csKLXfkfOY,afvENvZhgy,yhFlPeAPoA,CvRZkHlFPQ",
     *   "SrcBranch": "H_v3.6.16Bak",
     *   "Title": "test1"
     * }
     */
    private String Content;
    private String DepotId;
    private String DepotPath;
    private String DestBranch;
    private String Reviewers;
    private String SrcBranch;
    private String Title;
}
