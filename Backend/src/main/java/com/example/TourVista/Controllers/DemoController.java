package com.example.TourVista.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @PostMapping("/demo")
    public ResponseEntity<String> demo(){
        return ResponseEntity.ok("Hello form url");
    }

    @PostMapping("/admin")
    public ResponseEntity<String> admin_only(){
        return ResponseEntity.ok("Hello form admin");
    }
}
