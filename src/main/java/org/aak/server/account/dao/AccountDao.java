package org.aak.server.account.dao;

import org.aak.server.account.model.po.AccountPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccountDao {

    @Select("select * from accounts where email = #{email}")
    @Results(
            id = "account",
            value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "email", column = "email"),
                    @Result(property = "password", column = "password"),
                    @Result(property = "documentIdsStr", column = "document_ids_str"),
            }
    )
    AccountPO getByEmail(String email);


    @Select("select * from accounts where id = #{id}")
    @ResultMap("account")
    AccountPO getById(String id);

    @Insert("insert into accounts(id, email, password, document_ids_str) " +
            "values(#{id}, #{email}, #{password}, #{documentIdsStr})")
    void addAccount(AccountPO account);

    @Delete("delete from accounts where email = #{email}")
    void delAccount(String email);

    @Update("update accounts SET id = #{id}, email = #{email}, password = " +
            "#{password}, document_ids_str = #{documentIdsStr} where email = #{email}")
    void updAccount(AccountPO account);
}
