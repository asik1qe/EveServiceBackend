package com.example.EveSpace.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "TestOffersNPC")
public class OffersNPCCorporation {
    @Id
    @Column(name="offer_id")
    private Long offer_id;

    @Column(name="type_id")
    private Long type_id;

    @Column(name="lp_cost", nullable = false)
    private Long lp_cost;

    @Column(name="isk_cost", nullable = false)
    private Long isk_cost;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "corporation_id", nullable = false)
    private Long corporationId;

    @OneToMany(
            mappedBy = "offer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OffersNPCCorporationRequiredItem> requiredItems = new ArrayList<>();

    protected OffersNPCCorporation() {
    }

    public OffersNPCCorporation(Long offer_id, Long type_id, Long isk_cost, Long lp_cost, Long quantity, Long corporationId) {
        this.offer_id = offer_id;
        this.type_id = type_id;
        this.isk_cost = isk_cost;
        this.lp_cost = lp_cost;
        this.quantity = quantity;
        this.corporationId = corporationId;
    }

    public void addRequiredItem(OffersNPCCorporationRequiredItem item) {
        requiredItems.add(item);
        item.setOffer(this);
    }

    public void removeRequiredItem(OffersNPCCorporationRequiredItem item) {
        requiredItems.remove(item);
        item.setOffer(null);
    }


    public Long getCorporationId() {
        return corporationId;
    }

    public Long getOffer_id() {
        return offer_id;
    }

    public Long getType_id() { return type_id; }

    public Long getLp_cost() {
        return lp_cost;
    }

    public Long getIsk_cost() {
        return isk_cost;
    }

    public Long getQuantity() {
        return quantity;
    }

    public List<OffersNPCCorporationRequiredItem> getRequiredItems() {
        return requiredItems;
    }

    public void setCorporationId(Long corporationId) {
        this.corporationId = corporationId;
    }

    public void setOffer_id(Long offer_id) {
        this.offer_id = offer_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public void setLp_cost(Long lp_cost) {
        this.lp_cost = lp_cost;
    }

    public void setIsk_cost(Long isk_cost) {
        this.isk_cost = isk_cost;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
