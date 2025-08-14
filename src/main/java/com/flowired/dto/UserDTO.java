// src/main/java/com/flowired/dto/UserDTO.java
package com.flowired.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer idUser;
    private String username;
    private boolean enabled;
    private List<RolDTO> roles;
}
