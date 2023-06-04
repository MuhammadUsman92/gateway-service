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
    @GetMapping("/healthServiceFallBack")
    public String userServiceFallBackMethod(){
        return "Health Service is taking longer than Expected."+
                "Please try again later";
    }
    @GetMapping("/criminalServiceFallBack")
    public String accountServiceFallBackMethod(){
        return "Criminal Service is taking longer than Expected."+
                "Please try again later";
    }
    @GetMapping("/nearbyServiceFallBack")
    public String storeServiceFallBackMethod(){
        return "Near By Service is taking longer than Expected."+
                "Please try again later";
    }


}
