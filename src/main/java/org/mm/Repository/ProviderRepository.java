package org.mm.Repository;

import org.mm.Entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProviderRepository extends JpaRepository<Provider, Integer> {

}