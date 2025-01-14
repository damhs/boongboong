package com.example.shoong.controller;

import java.nio.charset.Charset;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/search-path")
public class SearchPathController {
    @Value("${naver.map-client-id}")
    private String clientId;

    @Value("${naver.map-client-secret}")
    private String clientSecret;

    @GetMapping
    public ResponseEntity<String> search(@RequestParam String start, @RequestParam String goal) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://naveropenapi.apigw.ntruss.com")
            .path("/map-direction/v1/driving")
            .queryParam("goal", goal)
            .queryParam("start", start)
            .encode(Charset.forName("UTF-8"))
            .build()
            .toUri();
        
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("x-ncp-apigw-api-key-id", clientId)
                .header("x-ncp-apigw-api-key", clientSecret)
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);
        
        String responseBody = responseEntity.getBody();
        
        return ResponseEntity.ok(responseBody);
    }
}
