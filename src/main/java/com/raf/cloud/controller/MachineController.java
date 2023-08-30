package com.raf.cloud.controller;

import com.raf.cloud.model.Machine;
import com.raf.cloud.model.User;
import com.raf.cloud.model.UserInfo;
import com.raf.cloud.service.MachineService;
import com.raf.cloud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/machine")
public class MachineController {

    private final UserService userService;
    private final MachineService machineService;

    @PostMapping(value = "/create")
    public Machine create(){
        return machineService.addMachine();
    }

    @PostMapping(value = "/restart/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Machine> restartMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.restartMachine(id));
    }

    @DeleteMapping(value = "/delete/{id}")
    private ResponseEntity<?> deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/restart", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<User> updateUser(@RequestBody UserInfo user){
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @GetMapping(value = "/getAll")
    private ResponseEntity<List<User>> getRoles(){
        return ResponseEntity.ok().body(userService.getAll());
    }

}
