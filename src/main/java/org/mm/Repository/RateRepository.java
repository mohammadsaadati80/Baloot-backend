package org.mm.Repository;

import org.mm.Entity.Rate;
import org.mm.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RateRepository extends JpaRepository<Rate, Integer> {
    Rate findByUserIdAndCommodityId(String user_id, Integer commodity_id);
}