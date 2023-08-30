package com.raf.cloud.service;

import com.raf.cloud.model.Machine;
import com.raf.cloud.model.User;
import com.raf.cloud.model.enums.Status;
import com.raf.cloud.repository.MachineRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@Builder
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Machine addMachine(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        var machine = Machine.builder()
                .status(Status.STOPPED)
                .user(user)
                .active(true)
                .build();

        return machineRepository.save(machine);
    }

    public Machine destroyMachine(Integer id){
        Optional<Machine> optionalMachine = machineRepository.findMachineById(id);
        if(optionalMachine.isPresent()){
            Machine machine = optionalMachine.get();
            if(machine.getStatus().equals(Status.STOPPED)){
                machine.setActive(false);
                return machine;
            }
            return null;
        }
        return null;
    }

    public HttpStatus restartMachine(Integer id) {
        Machine machine = this.findMachineById(id);

        if (machine == null || machine.getStatus() != Status.RUNNING) {
            return HttpStatus.BAD_GATEWAY;
        }

        new Thread(() -> restartMachineProcess(id)).start();

        return HttpStatus.OK;
    }

    private void restartMachineProcess(Integer id) {
        try {
            Machine machine = this.findMachineById(id);
            Thread.sleep(5000);

            machine.setStatus(Status.STOPPED);
            machineRepository.save(machine);

            Thread.sleep(5000);

            machine = this.findMachineById(id);
            machine.setStatus(Status.RUNNING);
            machineRepository.save(machine);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException e) {
            restartMachineProcess(id);
        }
    }




    private Machine findMachineById(Integer id){
        Optional<Machine> optionalMachine = machineRepository.findMachineById(id);
        return optionalMachine.orElse(null);
    }


}
