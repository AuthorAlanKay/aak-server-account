package org.aak.server.account.emuns;

import lombok.Getter;

@Getter
public enum Email {

    SEND_CAPTCHA("AAK 验证码（有效期三十分钟）", """
            <div>
                <p>尊敬的<strong>@${username}</strong>：</p>
                <p>我们收到了一项请求，要求通过您的电子邮件地址访问您的 AAK 帐号，您的 AAK 验证码为：</p>
                <p><big><strong>${captcha}</strong></big></p>
                <p>如果您并未请求此验证码，则可能是他人正在尝试访问以下 AAK 帐号。请勿将此验证码转发给或提供给任何人。</p>
                <p>您之所以会收到此邮件，是因为此电子邮件地址已被设为 AAK 帐号的邮箱。如果这不是您，请忽略此邮件。</p>
                <p>此致</p>
                <p>© authoralankay</p>
            </div>
                                    """),

    VERIFIED_REGISTER("感谢您成为 AAK 用户", """
            <div>
                <p>感谢您成为 AAK 用户, <strong>@${username}!</strong></p>
                <p>您之所以会收到此邮件，是因为此电子邮件地址已被设为 AAK 帐号的邮箱。如果这不是您，请忽略此邮件。</p>
                <p>此致</p>
                <p>© authoralankay</p>
            </div>
                                    """),

    RESET_PASSWORD("您的账号密码已更改", """
            <div>
                <p>您的账号密码已更改, <strong>@${username}!</strong></p>
                <p>您之所以会收到此邮件，是因为此电子邮件地址已被设为 AAK 帐号的邮箱。如果这不是您，请忽略此邮件。</p>
                <p>此致</p>
                <p>© authoralankay</p>
            </div>
                                    """),

    LOGOUT("您的账号已注销", """
            <div>
                <p>您的账号已注销, <strong>@${username}!</strong></p>
                <p>您之所以会收到此邮件，是因为此电子邮件地址已被设为 AAK 帐号的邮箱。如果这不是您，请忽略此邮件。</p>
                <p>此致</p>
                <p>© authoralankay</p>
            </div>
                                    """);

    private final String subject;

    private final String text;

    Email(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }
}
