package org.aak.server.account.controller.api;

import jakarta.validation.Valid;
import org.aak.server.account.model.po.AccountPO;
import org.aak.server.account.model.dto.*;
import org.aak.server.account.model.vo.ResultVO;
import org.aak.server.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/get-account/{email}")
    public ResultVO<AccountPO> getAccount(@PathVariable("email") String email) {
        return accountService.getAccount(email);
    }

    @PostMapping("/login")
    public ResultVO<AccountPO> login(@RequestBody @Valid AccountLoginDTO dto) {
        return accountService.login(dto);
    }

    @PostMapping("/register")
    public ResultVO<String> register(@RequestBody @Valid AccountRegisterDTO dto) {
        return accountService.register(dto);
    }

    @GetMapping("/verify-register/{captcha}")
    public ResultVO<AccountPO> verifyRegister(@PathVariable("captcha") String captcha) {
        return accountService.verifyRegister(captcha);
    }

    @PostMapping("/forgot-password")
    public ResultVO<String> forgotPassword(@RequestBody @Valid AccountForgotPasswordDTO dto) {
        return accountService.forgotPassword(dto);
    }

    @GetMapping("/verify-forgot-password/{captcha}")
    public ResultVO<AccountPO> verifyForgotPassword(@PathVariable String captcha) {
        return accountService.verifyForgotPassword(captcha);
    }

    @PostMapping("/reset-password")
    public ResultVO<AccountPO> resetPassword(@RequestBody @Valid AccountResetPasswordDTO dto) {
        return accountService.resetPassword(dto);
    }

    @PostMapping("/logout")
    public ResultVO<AccountPO> logout(@RequestBody @Valid AccountLogoutDTO dto) {
        return accountService.logout(dto);
    }
}
