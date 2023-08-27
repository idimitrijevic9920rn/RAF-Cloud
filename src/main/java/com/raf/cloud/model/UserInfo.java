package com.raf.cloud.model;

import com.raf.cloud.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Integer id;
    private String firstname;
    private String lastname;
    private List<Role> roles;


}
