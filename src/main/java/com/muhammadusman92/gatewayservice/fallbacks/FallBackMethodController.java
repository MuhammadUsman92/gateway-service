package com.muhammadusman92.gatewayservice.fallbacks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FallBackMethodController {
    @GetMapping("/authServiceFallBack")
    public String authServiceFallBackMethod(){
        return "Authentication Service is taking longer than Expected."+
                "Please try again later";
    }
    @GetMapping("/userServiceFallBack")
    public String userServiceFallBackMethod(){
        return "User Service is taking longer than Expected."+
                "Please try again later";
    }
    @GetMapping("/accountServiceFallBack")
    public String accountServiceFallBackMethod(){
        return "Account Service is taking longer than Expected."+
                "Please try again later";
    }
    @GetMapping("/storeServiceFallBack")
    public String storeServiceFallBackMethod(){
        return "Store Service is taking longer than Expected."+
                "Please try again later";
    }


}
