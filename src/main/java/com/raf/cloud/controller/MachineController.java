package com.raf.cloud.controller;

import com.raf.cloud.model.Machine;
import com.raf.cloud.request.MachineRequest;
import com.raf.cloud.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/machine")
public class MachineController {

    private final MachineService machineService;

    private final SimpMessagingTemplate template;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Machine> create(@RequestBody MachineRequest machineRequest){
        return new ResponseEntity<>(machineService.addMachine(machineRequest.getName()), HttpStatus.OK);
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/restart/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.restartMachine(id));
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.startMachine(id));
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/destroy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> destroyMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.destroyMachine(id));
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.stopMachine(id));
    }


    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Machine>> getMachines(){
        return ResponseEntity.ok().body(machineService.getAll());
    }


    public void notifyFrontend(String machineStatus, Integer machineId) {
        this.template.convertAndSend("/topic/machine/" + machineId, machineStatus);
    }
}
