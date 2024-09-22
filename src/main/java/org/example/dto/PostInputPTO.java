package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostInputPTO {
    private String title;
    private String body;
    private Long userId;
}
