package com.mna.springbootsecurity.powerbi.base.dto;

import lombok.Data;

@Data
public class EmbedTokenData {

    private String tokenId;
    private String token;
    private String expiration;

}
