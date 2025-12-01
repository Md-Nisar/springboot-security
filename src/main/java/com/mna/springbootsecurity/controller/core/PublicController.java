package com.mna.springbootsecurity.controller.core;

import com.mna.springbootsecurity.base.vo.response.ControllerResponse;
import com.mna.springbootsecurity.util.ResponseUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    public Map<Double, EmailData> map = new HashMap<>();


    @GetMapping("")
    public ResponseEntity<ControllerResponse<Void>> greet(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseUtil.success("Hello, Public!");
    }

    @PostMapping("/josn")
    public ResponseEntity<ControllerResponse<EmailData>> greet(@RequestBody EmailData emailData) {
        double random = Math.random();
        this.map.put(random, emailData);
        return ResponseUtil.success(this.map.get(random), "Success");
    }
}

@Getter
@Setter
 class EmailData {
    private String content;
}
