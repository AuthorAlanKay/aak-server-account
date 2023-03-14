package org.aak.server.account.service;

import org.aak.server.account.model.po.AccountPO;
import org.aak.server.account.model.dto.DocumentInitDTO;
import org.aak.server.account.model.dto.DocumentSetDTO;
import org.aak.server.account.model.vo.ResultVO;
import org.bson.Document;

public interface DocumentService {

    ResultVO<AccountPO> initDocument(DocumentInitDTO dto);

    ResultVO<Document> getDocument(String collection, String documentId);

    ResultVO<Document> setDocument(DocumentSetDTO dto);
}
