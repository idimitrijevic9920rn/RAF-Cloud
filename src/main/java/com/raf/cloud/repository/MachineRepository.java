package com.raf.cloud.repository;

import com.raf.cloud.model.Machine;
import com.raf.cloud.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {

    Optional<Machine> findMachineById(Integer id);
    List<Machine> findByUser_Id(Integer id);

    @Query("SELECT m FROM Machine m WHERE " +
            "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name,'%'))) AND " +
            "(:status IS NULL OR m.status = :status) AND " +
            "(CAST(:dateFrom AS date) IS NULL OR m.creationDate >= :dateFrom) AND " +
            "(CAST(:dateTo AS date) IS NULL OR m.creationDate <= :dateTo) AND " +
            "(:id IS NULL OR m.user.id = :id)")
    List<Machine> filterMachines(@Param("name") String name,
                                 @Param("status") Status status,
                                 @Param("dateFrom") LocalDate dateFrom,
                                 @Param("dateTo") LocalDate dateTo,
                                 @Param("id") Integer id);



}
