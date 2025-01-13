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
@RequestMapping("/api/search")
public class SearchPlaceController {
    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @GetMapping
    public ResponseEntity<String> search(@RequestParam String text) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/local.json")
            .queryParam("query", text)
            .queryParam("display", 5)
            .encode(Charset.forName("UTF-8"))
            .build()
            .toUri();
        
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);
        
        String responseBody = responseEntity.getBody();
        
        return ResponseEntity.ok(responseBody);
    }
}
