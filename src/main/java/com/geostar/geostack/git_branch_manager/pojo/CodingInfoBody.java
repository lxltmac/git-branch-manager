package com.geostar.geostack.git_branch_manager.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.geostar.geostack.git_branch_manager.pojo.coding.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class CodingInfoBody implements Serializable {
    private ResponseObject Response;
}


