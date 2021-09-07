package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LigneCommandeClientMapperTest {

    private LigneCommandeClientMapper ligneCommandeClientMapper;

    @BeforeEach
    public void setUp() {
        ligneCommandeClientMapper = new LigneCommandeClientMapperImpl();
    }
}
