package org.aak.server.account.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSetDTO implements Serializable {

    @NotBlank
    private String collection;

    @NotBlank
    private String documentId;

    @NotBlank
    private String key;

    @NotNull
    private Object value;
}
