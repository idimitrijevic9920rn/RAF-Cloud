package com.raf.cloud.controller;

import com.raf.cloud.model.enums.Role;
import com.raf.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserRepository userRepository;
    @DeleteMapping(value = "/delete/{id}")
    private ResponseEntity<?> deleteUser(@PathVariable("id") Integer id){
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }





}
