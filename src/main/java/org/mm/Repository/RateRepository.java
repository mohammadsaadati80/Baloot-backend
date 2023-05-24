package org.mm.Repository;

import org.mm.Entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
    Rate findByUsernameAndCommodityId(String username, Integer commodityId);
}