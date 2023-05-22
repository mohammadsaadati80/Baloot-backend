package org.mm.Repository;

import org.mm.Entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DiscountRepository extends JpaRepository<Discount, Integer> {

}