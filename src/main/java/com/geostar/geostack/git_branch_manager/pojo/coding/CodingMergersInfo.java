package com.geostar.geostack.git_branch_manager.pojo.coding;

import lombok.Data;

import java.util.List;


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
        return "https://xxxxxx.coding.net".concat(this.Path);
    }

    public String showInfo(){
        return this.getTitle().concat("->>").concat("\t").concat(this.getPath()).concat("\n\t");
    }

    public String showBranchInfo(){
        return this.getSourceBranch().concat("-->>").concat(this.getDesBranch()).concat("\n\t");
    }
}
