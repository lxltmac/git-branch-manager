package com.geostar.geostack.git_branch_manager.pojo.coding;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DataObject implements Serializable {
    private List<CodingMergersInfo> List;
    private int PageNumber;
    private int PageSize;
    private int TotalPage;
    private int TotalRow;
}
