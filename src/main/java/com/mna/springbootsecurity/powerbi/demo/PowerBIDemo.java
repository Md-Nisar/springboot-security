package com.mna.springbootsecurity.powerbi.demo;

import com.mna.springbootsecurity.powerbi.base.dto.EmbedConfigData;
import com.mna.springbootsecurity.powerbi.service.PowerBIAuthService;
import com.mna.springbootsecurity.powerbi.service.PowerBIService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PowerBIDemo {


    private final PowerBIAuthService powerBIAuthService;
    private final PowerBIService powerBIService;

    public void powerBI() {
        String accessToken = powerBIAuthService.getAccessToken();
        log.info(accessToken);

        String gId = "3e2ab247-71da-47c7-9ebc-eca6e0ccf81d";
        String rId = "dcf5807f-7314-4013-aa41-3e1abe11c266";

        EmbedConfigData embedConfig = powerBIService.getEmbedConfig(gId, rId);
        log.info(embedConfig.toString());

        log.info("------------------------------");
    }

}
