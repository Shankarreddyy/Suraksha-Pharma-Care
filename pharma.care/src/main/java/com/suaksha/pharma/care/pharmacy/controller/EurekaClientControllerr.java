package com.suaksha.pharma.care.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/instances")
public class EurekaClientControllerr {
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping
    public List<String> getInstances() {
        return discoveryClient.getServices();
    }
}
