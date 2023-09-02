package com.raf.cloud.model;


import com.raf.cloud.model.enums.MachineOperation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_error")
public class Error {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    @ManyToOne
    @JoinColumn(name = "machine")
    private Machine machine;

    @Column(updatable = false)
    private LocalDate creationDate;

    @PrePersist
    protected void onCreate(){
        this.creationDate = LocalDate.now();
    }

    private String errorDescription;

    @Enumerated(EnumType.STRING)
    private MachineOperation machineOperation;



}
