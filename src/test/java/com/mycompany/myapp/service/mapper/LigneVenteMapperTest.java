package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LigneVenteMapperTest {

    private LigneVenteMapper ligneVenteMapper;

    @BeforeEach
    public void setUp() {
        ligneVenteMapper = new LigneVenteMapperImpl();
    }
}
