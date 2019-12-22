package com.iotproject.nahin.smart_home_system.Controller;

import com.iotproject.nahin.smart_home_system.SmartHomeSystemApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("iot/")
public class InfotainmentController {

    @GetMapping("home")
    public String home(){
        return "iotHome";
    }

    @GetMapping("get-distance")
    public String getDistance(){
        return SmartHomeSystemApplication.sensorData;
    }
}
