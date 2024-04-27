package com.geostar.geostack.git_branch_manager.pojo;

import lombok.Data;

@Data
public class CodingDescribeDepotMergeReq {
    /**
     * {
     *   "CreatedAtEndDate": "“2023-06-28”",
     *   "CreatedAtStartDate": "“2023-06-28”",
     *   "CreatorEmails": [
     *     "coding@coding.net"
     *   ],
     *   "CreatorGlobalKeys": "无",
     *   "DepotId": "1",
     *   "DepotPath": "\"codingcorp/test/depot\"",
     *   "IsSortDirectionAsc": "true",
     *   "KeyWord": "关键词",
     *   "Labels": [
     *     "label-1",
     *     "label-2"
     *   ],
     *   "MergerEmails": [
     *     "coding@coding.net"
     *   ],
     *   "MergerGlobalKeys": "无",
     *   "PageNumber": "1",
     *   "PageSize": "10",
     *   "ReviewerEmails": [
     *     "coding@coding.net"
     *   ],
     *   "ReviewerGlobalKeys": "无",
     *   "Sort": "action_at",
     *   "SourceBranches": [
     *     "master"
     *   ],
     *   "Status": "OPEN",
     *   "TargetBranches": [
     *     "master"
     *   ],
     *   "UpdatedAtEndDate": "“2023-06-28”",
     *   "UpdatedAtStartDate": "“2023-06-28”"
     * }
     */
    private String DepotId;
    private String DepotPath;
    private String Status;
}
