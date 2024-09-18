package com.learn.ws.mock.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/response")
public class MockController {

    @GetMapping("/200")
    ResponseEntity<String> response200(){
        return ResponseEntity.ok("200");
    }

    @GetMapping("/500")
    ResponseEntity<?> response500(){
        return ResponseEntity.internalServerError().build();
    }

}
