package com.example.EveSpace.entity;

import jakarta.persistence.*;

@Entity
@Table(name="orders")
public class Orders {
    @Id
    @Column(name = "order_id")
    private Long orderId; // уникальный id ордера

    @Column(name = "type_order", nullable = false)
    private Boolean isBuyOrder;

    @Column(nullable = false)
    private Double price;

    @Column(name = "volume_remain", nullable = false)
    private Long volumeRemain;

    @Column(name = "system_id", nullable = false)
    private Long systemId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name="item_id")
    private Long itemId;

    protected Orders() {}

    public Orders(Long orderId, Boolean isBuyOrder, Double price, Long volumeRemain, Long systemId, Long locationId, Long item_id) {
        this.orderId = orderId;
        this.isBuyOrder = isBuyOrder;
        this.price = price;
        this.volumeRemain = volumeRemain;
        this.systemId = systemId;
        this.locationId = locationId;
        this.itemId = item_id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Boolean getBuyOrder() {
        return isBuyOrder;
    }

    public void setBuyOrder(Boolean buyOrder) {
        isBuyOrder = buyOrder;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getVolumeRemain() {
        return volumeRemain;
    }

    public void setVolumeRemain(Long volumeRemain) {
        this.volumeRemain = volumeRemain;
    }

    public Long getSystemId() {
        return systemId;
    }

    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getItem_id() {
        return itemId;
    }

    public void setItem_id(Long item_id) {
        this.itemId = item_id;
    }
}
