package org.aak.server.account.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterDTO implements Serializable {

    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    @NotBlank
    @Length(min = 2, max = 12, message = "用户名长度为2-12位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 4, max = 12, message = "密码长度为4-12位")
    private String password;
}
