package com.raf.cloud.controller;

import com.raf.cloud.model.Machine;
import com.raf.cloud.model.enums.Status;
import com.raf.cloud.request.MachineRequest;
import com.raf.cloud.request.ScheduleRequest;
import com.raf.cloud.service.MachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/machine")
public class MachineController {

    private final MachineService machineService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Machine> create(@RequestBody MachineRequest machineRequest){
        return new ResponseEntity<>(machineService.addMachine(machineRequest.getName()), HttpStatus.OK);
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/restart/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.restartMachine(id, null));
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> startMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.startMachine(id, null));
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/destroy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> destroyMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.destroyMachine(id));
    }

    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#id)")
    @PostMapping(value = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopMachine(@PathVariable Integer id) {
        return new ResponseEntity<>(machineService.stopMachine(id, null));
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Machine>> getMachines(){
        return ResponseEntity.ok().body(machineService.getAll());
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Machine>> filterMachines(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        return ResponseEntity.ok(machineService.filter(name, status, dateFrom, dateTo));
    }


    @PostMapping(value = "/schedule", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@machineAuthService.isUserAuthorizedToModifyMachine(#request.id)")
    public ResponseEntity<?> scheduleTask(@RequestBody ScheduleRequest request){

        return ResponseEntity.ok(machineService.scheduleTask(request.getId(), request.getOperation(), request.getDate()));
    }



}
