package com.mna.springbootsecurity.powerbi.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupData {
    private String id;
    private String name;
    private String type;
}
