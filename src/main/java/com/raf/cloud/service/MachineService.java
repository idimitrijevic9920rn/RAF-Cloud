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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@Builder
@RequiredArgsConstructor
public class MachineService {

    private final MachineRepository machineRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Machine addMachine(String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        var machine = Machine.builder()
                .name(name)
                .status(Status.STOPPED)
                .user(user)
                .active(true)
                .build();

        return machineRepository.save(machine);
    }

    public HttpStatus destroyMachine(Integer id){

        try {
            Optional<Machine> optionalMachine = machineRepository.findMachineById(id);
            if(optionalMachine.isPresent() && optionalMachine.get().getStatus().equals(Status.STOPPED)){
                Machine machine = optionalMachine.get();
                if(!machine.isActive()){
                    return HttpStatus.CONFLICT;
                }
                machine.setActive(false);
                return HttpStatus.OK;
            }

        } catch (ObjectOptimisticLockingFailureException exception){
            this.destroyMachine(id);
        }

        return HttpStatus.CONFLICT;
    }

    public HttpStatus restartMachine(Integer id) {
        Machine machine = this.findMachineById(id);

        if (machine == null || machine.getStatus() != Status.RUNNING || !machine.isActive()) {
            return HttpStatus.CONFLICT;
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

    public HttpStatus startMachine(Integer id){
        Machine machine = this.findMachineById(id);

        if (machine == null || machine.getStatus() != Status.STOPPED || !machine.isActive()) {
            System.out.println(machine == null);
            System.out.println(machine.getStatus() != Status.STOPPED);
            return HttpStatus.CONFLICT;
        }

        new Thread(() -> startMachineProcess(id)).start();
        return HttpStatus.OK;
    }

    private void startMachineProcess(Integer id){
        try {
            Machine machine = this.findMachineById(id);

            machine.setStatus(Status.RUNNING);
            Thread.sleep(10000);
            machineRepository.save(machine);

        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException e) {
            startMachineProcess(id);
        }

    }

    public HttpStatus stopMachine(Integer id){
        Machine machine = this.findMachineById(id);

        if (machine == null || machine.getStatus() != Status.RUNNING || !machine.isActive()) {
            return HttpStatus.CONFLICT;
        }

        new Thread(() -> stopMachineProcess(id)).start();
        return HttpStatus.OK;
    }

    private void stopMachineProcess(Integer id){
        try {
            Machine machine = findMachineById(id);
            machine.setStatus(Status.STOPPED);
            Thread.sleep(10000);
            machineRepository.save(machine);

        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ObjectOptimisticLockingFailureException e){
            stopMachineProcess(id);
        }
    }

    public List<Machine> getAll(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return machineRepository.findByUser_Id(user.getId());
    }

    private Machine findMachineById(Integer id){
        Optional<Machine> optionalMachine = machineRepository.findMachineById(id);
        return optionalMachine.orElse(null);
    }


}
