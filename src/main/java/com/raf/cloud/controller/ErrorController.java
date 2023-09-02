package com.raf.cloud.controller;

import com.raf.cloud.model.Error;
import com.raf.cloud.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/error")
public class ErrorController {

    private final ErrorService errorService;

    @GetMapping(value = "/getAll")
    public ResponseEntity<Optional<List<Error>>> getErrors(){
        return ResponseEntity.ok().body(errorService.getAll());
    }


}
