package com.example.EveSpace.entity;


import jakarta.persistence.*;

@Entity
@Table(name="all_items")
public class PublishedItem {
    @Id
    @Column(name="item_id")
    Long id;

    String name;

    @Column(name="discription")
    String discription;

    String icon;

    public PublishedItem(Long id, String name, String discription, String icon) {
        this.id = id;
        this.name = name;
        this.discription = discription;
        this.icon = icon;
    }

    public PublishedItem() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
