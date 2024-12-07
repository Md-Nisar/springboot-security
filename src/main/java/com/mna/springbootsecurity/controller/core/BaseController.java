package com.mna.springbootsecurity.controller.core;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class BaseController {

    @GetMapping("/status")
    public String status(@AuthenticationPrincipal UserDetails userDetails) {
        return "Active";
    }
}
