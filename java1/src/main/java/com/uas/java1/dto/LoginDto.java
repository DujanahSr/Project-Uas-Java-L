package com.uas.java1.dto;

// import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    // @Schema(defaultValue = "admin")
    private String username;
    // @Schema(defaultValue = "admin123")
    private String password;
}
