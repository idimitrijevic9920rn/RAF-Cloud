package com.raf.cloud.repository;

import com.raf.cloud.model.Error;
import com.raf.cloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ErrorRepository extends JpaRepository<Error, Integer> {

    Optional<List<Error>> findAllByUser_Id(Integer id);

}
