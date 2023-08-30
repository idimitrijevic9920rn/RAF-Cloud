package com.raf.cloud.service;

import com.raf.cloud.model.Machine;
import com.raf.cloud.model.User;
import com.raf.cloud.repository.MachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MachineAuthService {

    private final MachineRepository machineRepository;

    public boolean isUserAuthorizedToModifyMachine(Integer machineId) {
        Optional<Machine> machine = machineRepository.findMachineById(machineId);
        if(machine.isPresent()){
            return machine.get().getUser().getId().equals(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        }

        return false;
    }

}
