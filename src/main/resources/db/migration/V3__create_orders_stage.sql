CREATE TABLE IF NOT EXISTS orders_stage
(
    order_id      BIGINT PRIMARY KEY,
    type_order    BOOLEAN          NOT NULL,
    price         DOUBLE PRECISION NOT NULL,
    volume_remain BIGINT           NOT NULL,
    system_id     BIGINT           NOT NULL,
    location_id   BIGINT           NOT NULL,
    item_id       BIGINT
);

CREATE INDEX IF NOT EXISTS idx_orders_stage_order_id ON orders_stage(order_id);