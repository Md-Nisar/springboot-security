package com.mna.springbootsecurity.base.vo.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mna.springbootsecurity.mail.enums.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailNotificationData {

    private Object data;
    private EmailType type;
    private String emailAddress;
}
