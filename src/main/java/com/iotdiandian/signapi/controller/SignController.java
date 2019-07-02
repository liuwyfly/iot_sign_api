package com.iotdiandian.signapi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class SignController {
	@RequestMapping("/test_json")
    public String testJson() {
        return "{\"msg\": \"hello\"}";
    }
}
