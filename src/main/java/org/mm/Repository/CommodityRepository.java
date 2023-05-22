package org.mm.Repository;

import org.mm.Entity.Provider;
import org.mm.Entity.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class CommodityRepository extends JpaRepository<Commodity, Integer> {

    List<Commodity> findByProviders(Provider provider);

    List<Commodity> findByNameContainingIgnoreCase(String name);

    List<Commodity> findByPriceLike(String price);

    List<Commodity> findByCategories(String categories);

    List<Commodity> findByNameContainingIgnoreCaseOrderByPriceDesc(String name);

    List<Commodity> findByNameContainingIgnoreCaseOrderByNameDesc(String name);

    List<Commodity> findByCategoriesOrderByPriceDesc(String genre);

    List<Commodity> findByCategoriesOrderByNameDesc(String genre);

    List<Commodity> findByOrderByPriceDesc();

    List<Commodity> findByOrderByNameDesc();

}