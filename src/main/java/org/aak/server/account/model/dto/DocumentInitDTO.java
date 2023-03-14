package org.aak.server.account.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInitDTO implements Serializable {

    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    @NotBlank
    private String collection;
}
