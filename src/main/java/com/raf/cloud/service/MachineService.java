package com.raf.cloud.service;

import com.raf.cloud.config.ErrorMessage;
import com.raf.cloud.model.Machine;
import com.raf.cloud.model.User;
import com.raf.cloud.model.enums.MachineOperation;
import com.raf.cloud.model.enums.Status;
import com.raf.cloud.repository.MachineRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ErrorService errorService;

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

        Machine machine = findMachineById(id);

        try {
            if(machine.getStatus().equals(Status.STOPPED)){
                if(!machine.isActive()){
                    return HttpStatus.CONFLICT;
                }
                machine.setActive(false);
                machineRepository.save(machine);
                return HttpStatus.OK;
            }

        } catch (ObjectOptimisticLockingFailureException exception){
            errorService.saveError(MachineOperation.DESTROY, ErrorMessage.destroyMessage, machine, getloggedUser());
        }

        return HttpStatus.CONFLICT;
    }

    public HttpStatus restartMachine(Integer id) {
        Machine machine = this.findMachineById(id);

        if (machine == null || machine.getStatus() != Status.RUNNING || !machine.isActive()) {
            return HttpStatus.CONFLICT;
        }

        User user = getloggedUser();
        new Thread(() -> restartMachineProcess(id, user)).start();

        return HttpStatus.OK;
    }

    private void restartMachineProcess(Integer id, User user) {

        Machine machine = this.findMachineById(id);

        try {
            Thread.sleep(5000);

            machine.setStatus(Status.STOPPED);
            notifyFrontend(machine);

            Thread.sleep(5000);

            machine.setStatus(Status.RUNNING);
            machineRepository.save(machine);
            notifyFrontend(machine);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException e) {
            errorService.saveError(MachineOperation.RESTART, ErrorMessage.restartMessage, machine, user);
        }
    }

    public HttpStatus startMachine(Integer id){
        Machine machine = this.findMachineById(id);

        if (machine == null || machine.getStatus() != Status.STOPPED || !machine.isActive()) {
            return HttpStatus.CONFLICT;
        }

        User user = getloggedUser();

        new Thread(() -> startMachineProcess(id, user)).start();
        return HttpStatus.OK;
    }

    private void startMachineProcess(Integer id, User user){

        Machine machine = this.findMachineById(id);

        try {
            machine.setStatus(Status.RUNNING);
            Thread.sleep(10000);
            machineRepository.save(machine);

            notifyFrontend(machine);

        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ObjectOptimisticLockingFailureException e) {
            errorService.saveError(MachineOperation.START, ErrorMessage.startMessage, machine, user);
        }

    }

    public HttpStatus stopMachine(Integer id){
        Machine machine = this.findMachineById(id);

        if (machine == null || machine.getStatus() != Status.RUNNING || !machine.isActive()) {
            return HttpStatus.CONFLICT;
        }

        User user = getloggedUser();
        new Thread(() -> stopMachineProcess(id, user)).start();
        return HttpStatus.OK;
    }

    private void stopMachineProcess(Integer id, User user){
        Machine machine = findMachineById(id);

        try {
            machine.setStatus(Status.STOPPED);
            Thread.sleep(10000);
            machineRepository.save(machine);
            notifyFrontend(machine);

        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ObjectOptimisticLockingFailureException e){
            errorService.saveError(MachineOperation.STOP, ErrorMessage.stopMessage, machine, user);
        }
    }

    public List<Machine> filter(String name, Status status, LocalDate dateFrom, LocalDate dateTo){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return this.machineRepository.filterMachines(name, status, dateFrom, dateTo, user.getId());
    }

    public List<Machine> getAll(){
        User user = getloggedUser();
        return machineRepository.findByUser_Id(user.getId());
    }

    private Machine findMachineById(Integer id){
        Optional<Machine> optionalMachine = machineRepository.findMachineById(id);
        return optionalMachine.orElse(null);
    }

    private void notifyFrontend(Machine machine){
        simpMessagingTemplate.convertAndSend("/topic/machine-status", machine);
    }

    private User getloggedUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }



}
