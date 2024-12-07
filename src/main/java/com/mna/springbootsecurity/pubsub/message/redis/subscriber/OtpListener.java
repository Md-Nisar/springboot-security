package com.mna.springbootsecurity.pubsub.message.redis.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mna.springbootsecurity.base.vo.message.OTPData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OtpListener implements MessageListener {

    private final ObjectMapper objectMapper;

    public void onMessage(@NonNull Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        OTPData otp = null;
        try {
            otp = objectMapper.readValue(messageBody, OTPData.class);
            handleOtp(otp);
        } catch (JsonProcessingException e) {
            log.error("Failed to process Otp message", e);
        }

        log.info("Message Received: " + otp);
    }

    public void handleOtp(OTPData otp) {
        // Handle Otp
    }
}
