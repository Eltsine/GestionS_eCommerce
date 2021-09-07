package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Personnel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Personnel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {}
