package com.iotproject.nahin.smart_home_system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway/")
public class GateWayController {

    @GetMapping("send/")
    public String send() {
        return "";
    }
}
