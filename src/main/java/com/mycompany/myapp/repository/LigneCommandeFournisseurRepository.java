package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LigneCommandeFournisseur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneCommandeFournisseurRepository extends JpaRepository<LigneCommandeFournisseur, Long> {}
