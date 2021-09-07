package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LigneCommandeFournisseurMapperTest {

    private LigneCommandeFournisseurMapper ligneCommandeFournisseurMapper;

    @BeforeEach
    public void setUp() {
        ligneCommandeFournisseurMapper = new LigneCommandeFournisseurMapperImpl();
    }
}
