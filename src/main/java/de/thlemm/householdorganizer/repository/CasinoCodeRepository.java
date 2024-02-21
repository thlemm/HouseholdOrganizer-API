package de.thlemm.householdorganizer.repository;

import de.thlemm.householdorganizer.model.CasinoCode;
import de.thlemm.householdorganizer.model.Item;
import de.thlemm.householdorganizer.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasinoCodeRepository extends JpaRepository<CasinoCode, Integer> {

    CasinoCode findById(Long id);
    CasinoCode findByCode(String code);
    boolean existsByCode(String code);

}
