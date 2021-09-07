package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MvtStk;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MvtStk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MvtStkRepository extends JpaRepository<MvtStk, Long> {}
