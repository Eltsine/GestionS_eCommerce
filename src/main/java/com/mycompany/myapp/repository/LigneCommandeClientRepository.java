package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LigneCommandeClient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LigneCommandeClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Long> {}
