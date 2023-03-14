package org.aak.server.account.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountPO implements Serializable {

    private String id;

    private String email;

    private String password;

    /**
     * JSON 字符串
     */
    private String documentIdsStr;
}
