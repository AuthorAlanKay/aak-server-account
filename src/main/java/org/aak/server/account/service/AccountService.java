package org.aak.server.account.service;

import org.aak.server.account.model.po.AccountPO;
import org.aak.server.account.model.dto.*;
import org.aak.server.account.model.vo.ResultVO;
import org.bson.Document;

public interface AccountService {

    ResultVO<AccountPO> getAccount(String email);

    ResultVO<AccountPO> login(AccountLoginDTO dto);

    ResultVO<String> register(AccountRegisterDTO dto);

    ResultVO<AccountPO> verifyRegister(String captcha);

    ResultVO<String> forgotPassword(AccountForgotPasswordDTO dto);

    ResultVO<AccountPO> verifyForgotPassword(String captcha);

    ResultVO<AccountPO> resetPassword(AccountResetPasswordDTO dto);

    ResultVO<AccountPO> logout(AccountLogoutDTO dto);
}
