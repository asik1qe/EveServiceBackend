package com.example.EveSpace.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "offers_npc_corporation_required_items")
public class OffersNPCCorporationRequiredItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // связь на оффер
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", nullable = false)
    private OffersNPCCorporation offer;

    @Column(name = "type_id", nullable = false)
    private Long type_id;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected OffersNPCCorporationRequiredItem() {}

    public OffersNPCCorporationRequiredItem(Long type_id, Long quantity) {
        this.type_id = type_id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public OffersNPCCorporation getOffer() {
        return offer;
    }

    public Long getTypeId() {
        return type_id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOffer(OffersNPCCorporation offer) {
        this.offer = offer;
    }

    public void setTypeId(Long type_id) {
        this.type_id = type_id;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
