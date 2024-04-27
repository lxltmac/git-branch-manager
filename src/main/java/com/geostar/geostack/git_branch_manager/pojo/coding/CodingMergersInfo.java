package com.geostar.geostack.git_branch_manager.pojo.coding;

import lombok.Data;

import java.util.List;


/**
 * {
 *     "ActionAt": 1714014146000,
 *     "ActionAuthor": {
 *         "Id": 8288257,
 *         "TeamId": 0,
 *         "Name": "刘伟",
 *         "Email": "540003564@qq.com",
 *         "GlobalKey": "qSoWQyZJfS",
 *         "Status": "INACTIVE",
 *         "Avatar": "https://coding-net-production-static-ci.codehub.cn/WM-TEXT-AVATAR-tNjsWPmQixjHRxKKiGQC.jpg"
 *     },
 *     "Author": {
 *         "Id": 8288257,
 *         "TeamId": 0,
 *         "Name": "刘伟",
 *         "Email": "540003564@qq.com",
 *         "GlobalKey": "qSoWQyZJfS",
 *         "Status": "INACTIVE",
 *         "Avatar": "https://coding-net-production-static-ci.codehub.cn/WM-TEXT-AVATAR-tNjsWPmQixjHRxKKiGQC.jpg"
 *     },
 *     "CommentCount": 0,
 *     "CreatedAt": 1714014146000,
 *     "DesBranch": "tencent-prod",
 *     "Granted": 0,
 *     "Id": 11956268,
 *     "MergeId": 25845,
 *     "Labels": [],
 *     "Status": "CANMERGE",
 *     "Path": "/p/DIEPORDERN/d/dm-order/git/merge/25845",
 *     "Reviewers": [],
 *     "SrcBranch": "T_v3.6.17",
 *     "Title": "线上订单3.6.17上腾讯云",
 *     "UpdateAt": 1714014146000,
 *     "TargetBranchProtected": false,
 *     "Reminded": false,
 *     "DepotId": 9168854,
 *     "Describe": "https://oa.pagoda.com.cn/spa/workflow/static4form/index.html?_rdm=1713862522698#/main/workflow/req?requestid=1066513&preloadkey=1713862522698&timestamp=1713862522698&_key=e1hc4l",
 *     "BaseSha": "",
 *     "MergeCommitSha": "c056be5ef4ab6e36783478bd9ac820080e034a03",
 *     "SourceBranchSha": "f8a2321172196b500714b17a6c80946a1132032e",
 *     "TargetBranchSha": "06d833c3a6cc0e26183a5be5e6a5525ddee11e54",
 *     "StickingPoint": "PASS",
 *     "ProjectId": 9274766,
 *     "TargetBranch": "tencent-prod",
 *     "SourceBranch": "T_v3.6.17"
 * }
 */
@Data
public class CodingMergersInfo {

    private String ActionAt;

    private Author ActionAuthor;

    private Author Author;

    private String CommentCount;

    private String BaseSha;

    private Long DepotId;

    private String Describe;

    private String CreatedAt;

    private String Description;

    private String DesBranch;

    private Long Granted;

    private Long Id;

    private List<String> Labels;

    private String MergeCommitSha;

    private Long MergeId;

    private String Path;

    private Long ProjectId;

    private Boolean Reminded;

    private String SourceBranch;

    private String SourceBranchSha;

    private String Status;

    private String StickingPoint;

    private String TargetBranch;

    private String TargetBranchProtected;

    private String TargetBranchSha;

    private String Title;

    private Long UpdateAt;

    private List<Author> Reviewers;
    @Data
    public static class Author {
        private Long Id;
        private String Name;
        private String Email;
        private String GlobalKey;
        private String Status;
        private String Avatar;
        private Long TeamId;
    }

    public String getPath(){
        return "https://baiguokeji.coding.net".concat(this.Path);
    }

    public String showInfo(){
        return this.getTitle().concat("->>").concat("\t").concat(this.getPath()).concat("\n\t");
    }

    public String showBranchInfo(){
        return this.getSourceBranch().concat("-->>").concat(this.getDesBranch()).concat("\n\t");
    }
}
