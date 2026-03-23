DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    order_id      BIGINT PRIMARY KEY,
    type_order    BOOLEAN          NOT NULL,
    price         DOUBLE PRECISION NOT NULL,
    volume_remain BIGINT           NOT NULL,
    system_id     BIGINT           NOT NULL,
    location_id   BIGINT           NOT NULL,
    item_id       BIGINT
);