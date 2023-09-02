package com.raf.cloud.request;

import com.raf.cloud.model.enums.MachineOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {

    private Integer id;

    private MachineOperation operation;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date date;


}
