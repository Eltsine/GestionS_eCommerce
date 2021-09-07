package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MvtStkMapperTest {

    private MvtStkMapper mvtStkMapper;

    @BeforeEach
    public void setUp() {
        mvtStkMapper = new MvtStkMapperImpl();
    }
}
