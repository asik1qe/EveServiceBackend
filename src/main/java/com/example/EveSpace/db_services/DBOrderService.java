package com.example.EveSpace.db_services;

import com.example.EveSpace.DTO.ItemOrdersDTO;
import com.example.EveSpace.repository.OrdersRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DBOrderService {
    private JdbcTemplate jdbcTemplate;
    private final OrdersRepository ordersRepo;

    public DBOrderService(JdbcTemplate jdbcTemplate, OrdersRepository ordersRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.ordersRepo = ordersRepo;
    }

    @Transactional
    public void updateOrders(List<ItemOrdersDTO> all_orders) {
        jdbcTemplate.execute("TRUNCATE orders_stage");

        Set<Long> seen = new HashSet<>();
        for (ItemOrdersDTO order : all_orders) {
            if (!seen.add(order.order_id())) {
                throw new IllegalStateException("Duplicate order_id in input: " + order.order_id());
            }
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO orders_stage (order_id, type_order, price, volume_remain, system_id, location_id, item_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                all_orders,
                5000,
                (ps, r) -> {
                    ps.setLong(1, r.order_id());
                    ps.setBoolean(2, r.is_buy_order());
                    ps.setDouble(3, r.price());
                    ps.setLong(4, r.volume_remain());
                    ps.setLong(5, r.system_id());
                    ps.setLong(6, r.location_id());
                    ps.setLong(7, r.type_id());
                }
        );

        jdbcTemplate.update("""
            INSERT INTO orders (order_id, type_order, price, volume_remain, system_id, location_id, item_id)
            SELECT order_id, type_order, price, volume_remain, system_id, location_id, item_id
            FROM orders_stage
            ON CONFLICT (order_id) DO UPDATE
            SET type_order    = EXCLUDED.type_order,
                price         = EXCLUDED.price,
                volume_remain = EXCLUDED.volume_remain,
                system_id     = EXCLUDED.system_id,
                location_id   = EXCLUDED.location_id,
                item_id       = EXCLUDED.item_id
            WHERE orders.type_order    IS DISTINCT FROM EXCLUDED.type_order
               OR orders.price         IS DISTINCT FROM EXCLUDED.price
               OR orders.volume_remain IS DISTINCT FROM EXCLUDED.volume_remain
               OR orders.system_id     IS DISTINCT FROM EXCLUDED.system_id
               OR orders.location_id   IS DISTINCT FROM EXCLUDED.location_id
               OR orders.item_id       IS DISTINCT FROM EXCLUDED.item_id
            """);

        jdbcTemplate.update("""
            DELETE FROM orders o
            WHERE NOT EXISTS (
              SELECT 1 FROM orders_stage s WHERE s.order_id = o.order_id
            )
            """);
    }

    @Transactional
    public long getCountOrders() {
        return jdbcTemplate.queryForObject("""
                SELECT count(o.order_id)
                FROM orders o
                """, Long.class);
    }
}
