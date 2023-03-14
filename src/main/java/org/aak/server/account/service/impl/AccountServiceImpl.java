package org.aak.server.account.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.aak.server.account.dao.AccountDao;
import org.aak.server.account.emuns.Email;
import org.aak.server.account.emuns.ResultCode;
import org.aak.server.account.model.po.AccountPO;
import org.aak.server.account.model.dto.*;
import org.aak.server.account.model.vo.ResultVO;
import org.aak.server.account.service.AccountService;
import org.aak.server.account.utils.MailUtils;
import org.aak.server.account.utils.MongoUtils;
import org.aak.server.account.utils.RedisUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountDao accountDao;

    @Override
    public ResultVO<AccountPO> getAccount(String email) {
        AccountPO account = accountDao.getByEmail(email);
        if (account == null) return new ResultVO<>(ResultCode.VALIDATE_FAILED, "该用户不存在", null);
        // 删除密码
        account.setPassword(null);
        return new ResultVO<>(account);
    }

    @Override
    public ResultVO<AccountPO> login(AccountLoginDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        // 用户是否存在和密码是否匹配（加密）
        AccountPO account = accountDao.getByEmail(email);
        if (account == null || !account.getPassword().equals(DigestUtil.md5Hex(password)))
            return new ResultVO<>(ResultCode.VALIDATE_FAILED, "邮箱或密码错误", null);

        // 用户匹配，返回删除密码后的用户数据
        account.setPassword(null);
        return new ResultVO<>(account);
    }

    @Override
    public ResultVO<String> register(AccountRegisterDTO dto) {
        String username = dto.getUsername();
        String email = dto.getEmail();

        // 邮箱是否已注册
        AccountPO account = accountDao.getByEmail(email);
        if (account != null) return new ResultVO<>(ResultCode.VALIDATE_FAILED, "该邮箱已被注册");

        // 邮箱未被注册，生成随机数，缓存注册信息
        String captcha = RandomUtil.randomNumbers(6);
        RedisUtils.JEDIS.setex("account-register::" + captcha,
                30 * 60, JSONUtil.toJsonStr(dto));

        // 邮件通知用户接受验证码
        String subject = Email.SEND_CAPTCHA.getSubject();
        String text = Email.SEND_CAPTCHA.getText().
                replace("${username}", username).replace("${captcha}", captcha);
        MailUtils.sendMail(dto.getEmail(), subject, text);

        return new ResultVO<>("验证码刚刚已通过电子邮件发送到了 %s".formatted(email));
    }

    @Override
    public ResultVO<AccountPO> verifyRegister(String captcha) {
        String key = "account-register::" + captcha;
        String dtoStr = RedisUtils.JEDIS.get(key);

        // 验证码是否过期
        if (dtoStr == null)
            return new ResultVO<>(ResultCode.VALIDATE_FAILED, "验证码无效或过期", null);

        // 验证码匹配
        AccountRegisterDTO dto = JSONUtil.toBean(dtoStr, AccountRegisterDTO.class);
        String username = dto.getUsername();
        String email = dto.getEmail();
        String password = dto.getPassword();

        // 账号是否已激活
        AccountPO account = accountDao.getByEmail(email);
        if (account != null)
            return new ResultVO<>(ResultCode.VALIDATE_FAILED, "该邮箱已被激活", null);

        // 账号初始化
        Document document = new Document();
        document.put("username", username);
        String documentId = String.valueOf(
                Objects.requireNonNull(MongoUtils.COLLECTION_MAP.get("account").insertOne(document).
                        getInsertedId()).asObjectId().getValue());

        // 密码加密
        JSONObject documentIds = new JSONObject();
        documentIds.set("account", documentId);
        account = new AccountPO(DigestUtil.md5Hex("aak::" + email), email,
                DigestUtil.md5Hex(password), JSONUtil.toJsonStr(documentIds));
        accountDao.addAccount(account);

        // 邮件通知用户注册成功
        String subject = Email.VERIFIED_REGISTER.getSubject();
        String text = Email.VERIFIED_REGISTER.getText().
                replace("${username}", username);
        MailUtils.sendMail(dto.getEmail(), subject, text);

        //
        account.setPassword(null);
        RedisUtils.JEDIS.del(key);
        return new ResultVO<>("注册账号成功，使用者邮箱：%s".formatted(email), account);
    }

    @Override
    public ResultVO<String> forgotPassword(AccountForgotPasswordDTO dto) {
        String email = dto.getEmail();

        // 邮箱是否已被注册
        AccountPO account = accountDao.getByEmail(email);
        if (account == null) return new ResultVO<>(ResultCode.VALIDATE_FAILED, "该邮箱还未注册");

        // 邮箱未被注册，生成验证码，缓存重置密码信息
        String captcha = RandomUtil.randomNumbers(6);
        RedisUtils.JEDIS.setex("account-forget-password::" + captcha,
                30 * 60, JSONUtil.toJsonStr(dto));

        // 邮件通知用户接受验证码
        String subject = Email.SEND_CAPTCHA.getSubject();
        String text = Email.SEND_CAPTCHA.getText().
                replace("${username}", email).replace("${captcha}", captcha);
        MailUtils.sendMail(dto.getEmail(), subject, text);

        return new ResultVO<>("验证码刚刚已通过电子邮件发送到了 %s".formatted(email));
    }

    @Override
    public ResultVO<AccountPO> verifyForgotPassword(String captcha) {
        String key = "account-forget-password::" + captcha;
        String dtoStr = RedisUtils.JEDIS.get(key);

        // 验证码是否过期
        if (dtoStr == null)
            return new ResultVO<>(ResultCode.VALIDATE_FAILED, "验证码无效或过期", null);

        // 验证码通过
        AccountForgotPasswordDTO dto = JSONUtil.toBean(dtoStr, AccountForgotPasswordDTO.class);
        String email = dto.getEmail();
        AccountPO account = accountDao.getByEmail(email);

        // 删除缓存
        account.setPassword(null);
        RedisUtils.JEDIS.del(key);
        return new ResultVO<>("忘记密码认证成功，使用者邮箱：%s".formatted(email), account);
    }

    @Override
    public ResultVO<AccountPO> resetPassword(AccountResetPasswordDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        // 密码重置
        AccountPO account = accountDao.getByEmail(email);
        account.setPassword(DigestUtil.md5Hex(password));
        accountDao.updAccount(account);

        // 邮件通知用户密码重置
        String subject = Email.RESET_PASSWORD.getSubject();
        String text = Email.RESET_PASSWORD.getText().
                replace("${username}", email);
        MailUtils.sendMail(dto.getEmail(), subject, text);

        //
        account.setPassword(null);
        return new ResultVO<>("密码重置成功，使用者邮箱：%s".formatted(email), account);
    }

    @Override
    public ResultVO<AccountPO> logout(AccountLogoutDTO dto) {
        String email = dto.getEmail();

        // 邮箱是否已被注册
        AccountPO account = accountDao.getByEmail(email);
        if (account == null)
            return new ResultVO<>(ResultCode.VALIDATE_FAILED, null);

        // 清除痕迹
        // mongo
        JSONObject documentIds = JSONUtil.parseObj(account.getDocumentIdsStr());
        documentIds.forEach(entry ->
                MongoUtils.COLLECTION_MAP.get(entry.getKey()).deleteOne(
                        Filters.eq("_id", new ObjectId((String) entry.getValue())))
        );
        // mysql
        accountDao.delAccount(email);

        // 邮件通知用户账号注销
        String subject = Email.LOGOUT.getSubject();
        String text = Email.LOGOUT.getText().
                replace("${username}", email);
        MailUtils.sendMail(dto.getEmail(), subject, text);

        return new ResultVO<>("账号注销成功，使用者邮箱：%s".formatted(email), new AccountPO());
    }
}
