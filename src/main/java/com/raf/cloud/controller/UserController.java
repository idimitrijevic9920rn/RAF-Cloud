package com.raf.cloud.controller;

import com.raf.cloud.model.User;
import com.raf.cloud.model.UserInfo;
import com.raf.cloud.request.RegisterRequest;
import com.raf.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody RegisterRequest user){
        return userService.addUser(user);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody UserInfo user){
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<User>> getRoles(){
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping(value = "/getById/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Integer id){
        return ResponseEntity.ok().body(userService.getById(id));
    }


}
