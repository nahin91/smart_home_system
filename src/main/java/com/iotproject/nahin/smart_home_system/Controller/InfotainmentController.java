package com.iotproject.nahin.smart_home_system.Controller;

import com.iotproject.nahin.smart_home_system.Model.ResponseDistanceData;
import com.iotproject.nahin.smart_home_system.SmartHomeSystemApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("iot/")
public class InfotainmentController {

    @GetMapping("home")
    public String home(){
        return "iotHome";
    }

    @GetMapping("get-distance")
    @ResponseBody
    public ResponseDistanceData getDistance(){
        return SmartHomeSystemApplication.responseData;
    }
}
