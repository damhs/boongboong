package com.example.shoong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class ShoongApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoongApplication.class, args);
	}

}

@RestController
class HelloWorld {
	@GetMapping("/")
	public String greet() {
		return "Hello!";
	}
}
