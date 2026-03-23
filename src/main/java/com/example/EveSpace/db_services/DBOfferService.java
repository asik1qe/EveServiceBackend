package com.example.EveSpace.db_services;

import com.example.EveSpace.DTO.*;
import com.example.EveSpace.entity.OffersNPCCorporation;
import com.example.EveSpace.entity.OffersNPCCorporationRequiredItem;
import com.example.EveSpace.repository.CorporationRepository;
import com.example.EveSpace.repository.OffersNPCCorporationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DBOfferService{
    private JdbcTemplate jdbcTemplate;
    private OffersNPCCorporationRepository offerRepo;
    private final CorporationRepository corporationRepo;

    public DBOfferService(JdbcTemplate jdbcTemplate, CorporationRepository corporationRepo,
                          OffersNPCCorporationRepository offerRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.offerRepo =  offerRepo;
        this.corporationRepo = corporationRepo;
    }


    // метод готовит список всех оферов конкретной корпорации
    @Transactional(readOnly = true)
    public List<ResponseLpShopDTO> getAllOffersForCardApi (long corporationId) {
        String sql = """
                SELECT t_o.offer_id,
                    a_i.name as main_name,
                    a_i.icon as main_icon,
                    t_o.quantity as main_quantity,
                    t_o.lp_cost,
                    t_o.isk_cost,
                    COALESCE(p_main.min_price, 0) as main_min_price,
                    a_is.name,
                    a_is.icon,
                    oncri.quantity,
                    p.min_price
                
                
                
                
                FROM test_offersnpc t_o
                LEFT JOIN all_items a_i ON a_i.item_id = t_o.type_id
                LEFT JOIN offers_npc_corporation_required_items oncri ON
                    oncri.offer_id = t_o.offer_id
                LEFT JOIN all_items a_is ON a_is.item_id = oncri.type_id
                LEFT JOIN (SELECT
                               item_id,
                               MIN(price) as min_price
                           FROM orders
                           WHERE location_id = 60003760 and type_order = FALSE
                           GROUP BY item_id) p ON p.item_id = oncri.type_id
                LEFT JOIN (SELECT
                               item_id,
                               MIN(price) as min_price
                           FROM orders
                           WHERE location_id = 60003760 and type_order = FALSE
                           GROUP BY item_id) p_main ON p_main.item_id = t_o.type_id
                WHERE corporation_id = ?
                """;
        return jdbcTemplate.query(sql, rs ->{
            Map<Long, ResponseLpShopDTO> offers = new HashMap<>();

            while (rs.next()) {
                long id = rs.getLong("offer_id");

                ResponseLpShopDTO dto = offers.get(id);
                if (dto == null) {
                    List<SupportResponseLpShopDTO> supports = new ArrayList<>();

                    dto = new ResponseLpShopDTO(
                            rs.getString("main_name"),
                            rs.getString("main_icon"),
                            rs.getLong("main_quantity"),
                            rs.getLong("lp_cost"),
                            rs.getLong("isk_cost"),
                            supports,
                            rs.getDouble("main_min_price"),
                            0.0
                    );

                    offers.put(id, dto);
                }

                String reqName = rs.getString("name");
                if (reqName != null) {
                    dto.required_items().add(new SupportResponseLpShopDTO(
                            reqName,
                            rs.getString("icon"),
                            rs.getLong("quantity"),
                            rs.getDouble("min_price")
                    ));
                }
            }

            List<ResponseLpShopDTO> result = new ArrayList<>();
            for (ResponseLpShopDTO dto : offers.values()) {
                double total_price = 0.0;

                for (SupportResponseLpShopDTO item : dto.required_items()) {
                    total_price += item.price_isk() * item.quantity();
                }

                double revenue = dto.market_price() * dto.quantity();
                double profit = revenue - total_price - dto.isk_cost();

                result.add(new ResponseLpShopDTO(
                        dto.name_get_item(),
                        dto.icon_get_item(),
                        dto.quantity(),
                        dto.lp_cost(),
                        dto.isk_cost(),
                        dto.required_items(),
                        dto.market_price(),
                        profit
                ));
            }
            return result;
        }, corporationId);
    }

    // метод обновляет данные об оферах NPC корпораций
    @Transactional
    public void upsertOfferNCPCorporation(OffersNpcCorporationDTO dto) {

        OffersNPCCorporation offer = offerRepo.findById(dto.offer_id())
                .orElseGet(() -> new OffersNPCCorporation(
                        dto.offer_id(),
                        dto.type_id(),
                        dto.isk_cost(),
                        dto.lp_cost(),
                        dto.quantity(),
                        dto.corporationId()
                ));

        offer.setType_id(dto.type_id());
        offer.setIsk_cost(dto.isk_cost());
        offer.setLp_cost(dto.lp_cost());
        offer.setQuantity(dto.quantity());

        offer.getRequiredItems().clear();

        for (var support : dto.required_items()) {
            offer.addRequiredItem(
                    new OffersNPCCorporationRequiredItem(
                            support.type_id(),
                            support.quantity()
                    )
            );
        }

        offerRepo.save(offer);
    }


    @Transactional(readOnly = true)
    public List<OffersNPCCorporation> getAllOffers() {
        return offerRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<CorporationDto> getCorporationsWithOffers() {
        return corporationRepo.findCorporationsWithOffers().stream()
                .map(corp -> new CorporationDto(
                        corp.getCorporationId(),
                        corp.getName(),
                        corp.getIcon256Url()
                )).toList();
    }
}
