package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInputDTO {
    private String name;
    private String username;
    private String email;
}
