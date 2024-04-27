package com.geostar.geostack.git_branch_manager.pojo;

import com.geostar.geostack.git_branch_manager.pojo.coding.CodingMergersInfo;
import lombok.Data;

import java.util.List;

@Data
public class CodingMergeReqInfo {

    private List<CodingMergersInfo> List;

    private Integer PageNumber;

    private Integer PageSize;

    private Integer TotalPage;

    private Integer TotalRow;
}
