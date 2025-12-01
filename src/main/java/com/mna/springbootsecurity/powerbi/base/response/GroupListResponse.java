package com.mna.springbootsecurity.powerbi.base.response;

import com.mna.springbootsecurity.powerbi.base.dto.GroupData;
import lombok.Data;

import java.util.List;

@Data
public class GroupListResponse {
    private List<GroupData> value;
}
