package com.shounoop.carrentalspring.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.util.Map;

@Service
public class MLService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://ml-service:5001") // Docker service name
            .build();

    public String trainModel() {
        return webClient.post()
                .uri("/train")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getRecommendations(Map<String, Object> request) {
        return webClient.post()
                .uri("/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
