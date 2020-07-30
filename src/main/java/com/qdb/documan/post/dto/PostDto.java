package com.qdb.documan.post.dto;

import java.io.Serializable;


public class PostDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private String title;
    private String body;
    private String documentId;

	public PostDto() {
	}

	public PostDto(Long id, Long userId, String title, String body, String documentId) {
        super();
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
        this.documentId = documentId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
