package org.aak.server.account.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mongodb.client.model.Filters;
import lombok.extern.slf4j.Slf4j;
import org.aak.server.account.dao.AccountDao;
import org.aak.server.account.model.dto.DocumentInitDTO;
import org.aak.server.account.model.dto.DocumentSetDTO;
import org.aak.server.account.model.po.AccountPO;
import org.aak.server.account.model.vo.ResultVO;
import org.aak.server.account.service.DocumentService;
import org.aak.server.account.utils.MongoUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    AccountDao accountDao;

    @Override
    public ResultVO<AccountPO> initDocument(DocumentInitDTO dto) {
        String email = dto.getEmail();
        String collection = dto.getCollection();

        AccountPO account = accountDao.getByEmail(email);
        JSONObject documentIds = JSONUtil.parseObj(account.getDocumentIdsStr());

        // 之前存在删除
        if (documentIds.get(collection) != null)
            MongoUtils.COLLECTION_MAP.get(collection).deleteOne(
                    Filters.eq("_id", new ObjectId((String) documentIds.get(collection))));
        // 初始化
        String documentId = String.valueOf(Objects.requireNonNull(MongoUtils.COLLECTION_MAP.
                get(collection).insertOne(new Document()).getInsertedId()).asObjectId().getValue());

        documentIds.set(collection, documentId);
        account.setDocumentIdsStr(JSONUtil.toJsonStr(documentIds));
        accountDao.updAccount(account);

        return new ResultVO<>(account);
    }

    @Override
    public ResultVO<Document> getDocument(String collection, String documentId) {
        return new ResultVO<>(MongoUtils.COLLECTION_MAP.get(collection).
                find(Filters.eq("_id", new ObjectId(documentId))).first());
    }

    @Override
    public ResultVO<Document> setDocument(DocumentSetDTO dto) {
        String collection = dto.getCollection();
        String documentId = dto.getDocumentId();
        String key = dto.getKey();
        Object value = dto.getValue();

        MongoUtils.COLLECTION_MAP.get(collection).updateOne(
                Filters.eq("_id", new ObjectId(documentId)),
                new Document("$set", new Document(key, value)));

        return getDocument(collection, documentId);
    }
}
