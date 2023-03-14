package org.aak.server.account.controller.api;

import jakarta.validation.Valid;
import org.aak.server.account.model.po.AccountPO;
import org.aak.server.account.model.dto.DocumentInitDTO;
import org.aak.server.account.model.dto.DocumentSetDTO;
import org.aak.server.account.model.vo.ResultVO;
import org.aak.server.account.service.DocumentService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    DocumentService dataService;

    @PostMapping("/init-document")
    public ResultVO<AccountPO> initDocument(@RequestBody @Valid DocumentInitDTO dto) {
        return dataService.initDocument(dto);
    }

    @GetMapping("/get-document/{collection}/{document-id}")
    public ResultVO<Document> getDocument(@PathVariable("collection") String collection,
                                          @PathVariable("document-id") String documentId) {
        return dataService.getDocument(collection, documentId);
    }

    @PostMapping("/set-document")
    public ResultVO<Document> setDocument(@RequestBody @Valid DocumentSetDTO dto) {
        return dataService.setDocument(dto);
    }
}
