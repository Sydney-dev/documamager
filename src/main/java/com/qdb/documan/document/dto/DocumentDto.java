package com.qdb.documan.document.dto;

import java.io.Serializable;

public class DocumentDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String uri;

    public DocumentDto(String name, String uri, Long id) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
