package com.raf.cloud.service;

import com.raf.cloud.model.Error;
import com.raf.cloud.model.Machine;
import com.raf.cloud.model.User;
import com.raf.cloud.model.enums.MachineOperation;
import com.raf.cloud.repository.ErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ErrorService {

    private final ErrorRepository errorRepository;

    public void saveError(MachineOperation machineOperation, String errorDescription, Machine machine, User user){
        var error = Error.builder()
                .machineOperation(machineOperation)
                .errorDescription(errorDescription)
                .machine(machine)
                .user(user)
                .build();

        errorRepository.save(error);
    }

    public Optional<List<Error>> getAll(){
        return errorRepository.findAllByUser_Id(getLoggedUser().getId());
    }

    private User getLoggedUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
