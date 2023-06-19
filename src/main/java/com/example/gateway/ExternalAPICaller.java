package com.example.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalAPICaller {

    @Autowired
    public ExternalAPICaller(RestTemplate restTemplate) {
    }
}
