package com.mna.springbootsecurity.controller.core;

import com.mna.springbootsecurity.base.vo.response.ControllerResponse;
import com.mna.springbootsecurity.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping("")
    public ResponseEntity<ControllerResponse<Void>> greet(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseUtil.success("Hello, Admin!");
    }
}
