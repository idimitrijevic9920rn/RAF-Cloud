package com.raf.cloud.request;


import com.raf.cloud.model.User;
import com.raf.cloud.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineRequest {

    private String name;

}
