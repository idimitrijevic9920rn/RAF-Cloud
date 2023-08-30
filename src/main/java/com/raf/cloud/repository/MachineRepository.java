package com.raf.cloud.repository;

import com.raf.cloud.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {

    Optional<Machine> findMachineById(Integer id);


}
