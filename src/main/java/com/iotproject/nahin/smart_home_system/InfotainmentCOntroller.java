package com.iotproject.nahin.smart_home_system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("iot/")
public class InfotainmentCOntroller {

    @GetMapping("home")
    public String home(){
        return "iotHome";
    }
}
