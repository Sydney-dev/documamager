package com.qdb.documan.document.domain;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;
    @Column(name = "location", nullable = false)
    private String location;
    @Column(name = "userId", nullable = false)
    private String userId;
    @Column(name = "status", nullable = false)
    private boolean status;

    public Document() {

    }

    public Document(String fileName, Date dateCreated, String location,
                    String userId, boolean status) {
        super();
        this.id = id;
        this.fileName = fileName;
        this.dateCreated = dateCreated;
        this.location = location;
        this.userId = userId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userUuid) {
        this.userId = userUuid;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean isDeleted) {
        this.status = isDeleted;
    }

}

