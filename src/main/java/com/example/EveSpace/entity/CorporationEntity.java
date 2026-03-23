package com.example.EveSpace.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "corporations")
public class CorporationEntity {

    @Id
    private Long corporationId;

    private String name;

    private String icon256Url;

    public CorporationEntity() {
    }

    public CorporationEntity(Long corporationId, String name, String icon256Url) {
        this.corporationId = corporationId;
        this.name = name;
        this.icon256Url = icon256Url;
    }

    public Long getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(Long corporationId) {
        this.corporationId = corporationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon256Url() {
        return icon256Url;
    }

    public void setIcon256Url(String icon256Url) {
        this.icon256Url = icon256Url;
    }
}
