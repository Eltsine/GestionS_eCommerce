package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LigneVente;
import com.mycompany.myapp.repository.LigneVenteRepository;
import com.mycompany.myapp.service.dto.LigneVenteDTO;
import com.mycompany.myapp.service.mapper.LigneVenteMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link LigneVenteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LigneVenteResourceIT {

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/ligne-ventes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LigneVenteRepository ligneVenteRepository;

    @Autowired
    private LigneVenteMapper ligneVenteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneVenteMockMvc;

    private LigneVente ligneVente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneVente createEntity(EntityManager em) {
        LigneVente ligneVente = new LigneVente().quantite(DEFAULT_QUANTITE).prixUnitaire(DEFAULT_PRIX_UNITAIRE);
        return ligneVente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneVente createUpdatedEntity(EntityManager em) {
        LigneVente ligneVente = new LigneVente().quantite(UPDATED_QUANTITE).prixUnitaire(UPDATED_PRIX_UNITAIRE);
        return ligneVente;
    }

    @BeforeEach
    public void initTest() {
        ligneVente = createEntity(em);
    }

    @Test
    @Transactional
    void createLigneVente() throws Exception {
        int databaseSizeBeforeCreate = ligneVenteRepository.findAll().size();
        // Create the LigneVente
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);
        restLigneVenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO)))
            .andExpect(status().isCreated());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeCreate + 1);
        LigneVente testLigneVente = ligneVenteList.get(ligneVenteList.size() - 1);
        assertThat(testLigneVente.getQuantite()).isEqualByComparingTo(DEFAULT_QUANTITE);
        assertThat(testLigneVente.getPrixUnitaire()).isEqualByComparingTo(DEFAULT_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void createLigneVenteWithExistingId() throws Exception {
        // Create the LigneVente with an existing ID
        ligneVente.setId(1L);
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);

        int databaseSizeBeforeCreate = ligneVenteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneVenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLigneVentes() throws Exception {
        // Initialize the database
        ligneVenteRepository.saveAndFlush(ligneVente);

        // Get all the ligneVenteList
        restLigneVenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneVente.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(sameNumber(DEFAULT_QUANTITE))))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(sameNumber(DEFAULT_PRIX_UNITAIRE))));
    }

    @Test
    @Transactional
    void getLigneVente() throws Exception {
        // Initialize the database
        ligneVenteRepository.saveAndFlush(ligneVente);

        // Get the ligneVente
        restLigneVenteMockMvc
            .perform(get(ENTITY_API_URL_ID, ligneVente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneVente.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(sameNumber(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.prixUnitaire").value(sameNumber(DEFAULT_PRIX_UNITAIRE)));
    }

    @Test
    @Transactional
    void getNonExistingLigneVente() throws Exception {
        // Get the ligneVente
        restLigneVenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLigneVente() throws Exception {
        // Initialize the database
        ligneVenteRepository.saveAndFlush(ligneVente);

        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();

        // Update the ligneVente
        LigneVente updatedLigneVente = ligneVenteRepository.findById(ligneVente.getId()).get();
        // Disconnect from session so that the updates on updatedLigneVente are not directly saved in db
        em.detach(updatedLigneVente);
        updatedLigneVente.quantite(UPDATED_QUANTITE).prixUnitaire(UPDATED_PRIX_UNITAIRE);
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(updatedLigneVente);

        restLigneVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneVenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO))
            )
            .andExpect(status().isOk());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
        LigneVente testLigneVente = ligneVenteList.get(ligneVenteList.size() - 1);
        assertThat(testLigneVente.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneVente.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void putNonExistingLigneVente() throws Exception {
        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();
        ligneVente.setId(count.incrementAndGet());

        // Create the LigneVente
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneVenteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigneVente() throws Exception {
        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();
        ligneVente.setId(count.incrementAndGet());

        // Create the LigneVente
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigneVente() throws Exception {
        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();
        ligneVente.setId(count.incrementAndGet());

        // Create the LigneVente
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneVenteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigneVenteWithPatch() throws Exception {
        // Initialize the database
        ligneVenteRepository.saveAndFlush(ligneVente);

        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();

        // Update the ligneVente using partial update
        LigneVente partialUpdatedLigneVente = new LigneVente();
        partialUpdatedLigneVente.setId(ligneVente.getId());

        restLigneVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneVente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneVente))
            )
            .andExpect(status().isOk());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
        LigneVente testLigneVente = ligneVenteList.get(ligneVenteList.size() - 1);
        assertThat(testLigneVente.getQuantite()).isEqualByComparingTo(DEFAULT_QUANTITE);
        assertThat(testLigneVente.getPrixUnitaire()).isEqualByComparingTo(DEFAULT_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void fullUpdateLigneVenteWithPatch() throws Exception {
        // Initialize the database
        ligneVenteRepository.saveAndFlush(ligneVente);

        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();

        // Update the ligneVente using partial update
        LigneVente partialUpdatedLigneVente = new LigneVente();
        partialUpdatedLigneVente.setId(ligneVente.getId());

        partialUpdatedLigneVente.quantite(UPDATED_QUANTITE).prixUnitaire(UPDATED_PRIX_UNITAIRE);

        restLigneVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneVente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneVente))
            )
            .andExpect(status().isOk());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
        LigneVente testLigneVente = ligneVenteList.get(ligneVenteList.size() - 1);
        assertThat(testLigneVente.getQuantite()).isEqualByComparingTo(UPDATED_QUANTITE);
        assertThat(testLigneVente.getPrixUnitaire()).isEqualByComparingTo(UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingLigneVente() throws Exception {
        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();
        ligneVente.setId(count.incrementAndGet());

        // Create the LigneVente
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligneVenteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigneVente() throws Exception {
        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();
        ligneVente.setId(count.incrementAndGet());

        // Create the LigneVente
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigneVente() throws Exception {
        int databaseSizeBeforeUpdate = ligneVenteRepository.findAll().size();
        ligneVente.setId(count.incrementAndGet());

        // Create the LigneVente
        LigneVenteDTO ligneVenteDTO = ligneVenteMapper.toDto(ligneVente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneVenteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ligneVenteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneVente in the database
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLigneVente() throws Exception {
        // Initialize the database
        ligneVenteRepository.saveAndFlush(ligneVente);

        int databaseSizeBeforeDelete = ligneVenteRepository.findAll().size();

        // Delete the ligneVente
        restLigneVenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligneVente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LigneVente> ligneVenteList = ligneVenteRepository.findAll();
        assertThat(ligneVenteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
