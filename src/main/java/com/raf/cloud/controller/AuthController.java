package com.raf.cloud.controller;

import com.raf.cloud.request.AuthenticationRequest;
import com.raf.cloud.request.RegisterRequest;
import com.raf.cloud.response.AuthenticationResponse;
import com.raf.cloud.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        AuthenticationResponse auth = authService.register(request);
        System.out.println(auth + " iz reg");
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse auth = authService.authenticate(request);
        System.out.println(auth + " iz auth");
        return ResponseEntity.ok(authService.authenticate(request));
    }


}
