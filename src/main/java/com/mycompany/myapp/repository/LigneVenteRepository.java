package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LigneVente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LigneVente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneVenteRepository extends JpaRepository<LigneVente, Long> {}
