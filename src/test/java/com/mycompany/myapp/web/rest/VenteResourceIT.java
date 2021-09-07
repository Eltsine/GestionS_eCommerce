package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Vente;
import com.mycompany.myapp.repository.VenteRepository;
import com.mycompany.myapp.service.dto.VenteDTO;
import com.mycompany.myapp.service.mapper.VenteMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VenteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VenteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_VENTE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_VENTE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ventes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private VenteMapper venteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVenteMockMvc;

    private Vente vente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vente createEntity(EntityManager em) {
        Vente vente = new Vente().code(DEFAULT_CODE).dateVente(DEFAULT_DATE_VENTE).commentaire(DEFAULT_COMMENTAIRE);
        return vente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vente createUpdatedEntity(EntityManager em) {
        Vente vente = new Vente().code(UPDATED_CODE).dateVente(UPDATED_DATE_VENTE).commentaire(UPDATED_COMMENTAIRE);
        return vente;
    }

    @BeforeEach
    public void initTest() {
        vente = createEntity(em);
    }

    @Test
    @Transactional
    void createVente() throws Exception {
        int databaseSizeBeforeCreate = venteRepository.findAll().size();
        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);
        restVenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isCreated());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeCreate + 1);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVente.getDateVente()).isEqualTo(DEFAULT_DATE_VENTE);
        assertThat(testVente.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void createVenteWithExistingId() throws Exception {
        // Create the Vente with an existing ID
        vente.setId(1L);
        VenteDTO venteDTO = venteMapper.toDto(vente);

        int databaseSizeBeforeCreate = venteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVentes() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        // Get all the venteList
        restVenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vente.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].dateVente").value(hasItem(DEFAULT_DATE_VENTE.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @Test
    @Transactional
    void getVente() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        // Get the vente
        restVenteMockMvc
            .perform(get(ENTITY_API_URL_ID, vente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vente.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.dateVente").value(DEFAULT_DATE_VENTE.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getNonExistingVente() throws Exception {
        // Get the vente
        restVenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVente() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeUpdate = venteRepository.findAll().size();

        // Update the vente
        Vente updatedVente = venteRepository.findById(vente.getId()).get();
        // Disconnect from session so that the updates on updatedVente are not directly saved in db
        em.detach(updatedVente);
        updatedVente.code(UPDATED_CODE).dateVente(UPDATED_DATE_VENTE).commentaire(UPDATED_COMMENTAIRE);
        VenteDTO venteDTO = venteMapper.toDto(updatedVente);

        restVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVente.getDateVente()).isEqualTo(UPDATED_DATE_VENTE);
        assertThat(testVente.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVenteWithPatch() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeUpdate = venteRepository.findAll().size();

        // Update the vente using partial update
        Vente partialUpdatedVente = new Vente();
        partialUpdatedVente.setId(vente.getId());

        partialUpdatedVente.code(UPDATED_CODE).commentaire(UPDATED_COMMENTAIRE);

        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVente))
            )
            .andExpect(status().isOk());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVente.getDateVente()).isEqualTo(DEFAULT_DATE_VENTE);
        assertThat(testVente.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateVenteWithPatch() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeUpdate = venteRepository.findAll().size();

        // Update the vente using partial update
        Vente partialUpdatedVente = new Vente();
        partialUpdatedVente.setId(vente.getId());

        partialUpdatedVente.code(UPDATED_CODE).dateVente(UPDATED_DATE_VENTE).commentaire(UPDATED_COMMENTAIRE);

        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVente))
            )
            .andExpect(status().isOk());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVente.getDateVente()).isEqualTo(UPDATED_DATE_VENTE);
        assertThat(testVente.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, venteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVente() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeDelete = venteRepository.findAll().size();

        // Delete the vente
        restVenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, vente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
