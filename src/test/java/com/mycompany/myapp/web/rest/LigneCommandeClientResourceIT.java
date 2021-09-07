package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LigneCommandeClient;
import com.mycompany.myapp.repository.LigneCommandeClientRepository;
import com.mycompany.myapp.service.dto.LigneCommandeClientDTO;
import com.mycompany.myapp.service.mapper.LigneCommandeClientMapper;
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
 * Integration tests for the {@link LigneCommandeClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LigneCommandeClientResourceIT {

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/ligne-commande-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LigneCommandeClientRepository ligneCommandeClientRepository;

    @Autowired
    private LigneCommandeClientMapper ligneCommandeClientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneCommandeClientMockMvc;

    private LigneCommandeClient ligneCommandeClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneCommandeClient createEntity(EntityManager em) {
        LigneCommandeClient ligneCommandeClient = new LigneCommandeClient().quantite(DEFAULT_QUANTITE).prixUnitaire(DEFAULT_PRIX_UNITAIRE);
        return ligneCommandeClient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneCommandeClient createUpdatedEntity(EntityManager em) {
        LigneCommandeClient ligneCommandeClient = new LigneCommandeClient().quantite(UPDATED_QUANTITE).prixUnitaire(UPDATED_PRIX_UNITAIRE);
        return ligneCommandeClient;
    }

    @BeforeEach
    public void initTest() {
        ligneCommandeClient = createEntity(em);
    }

    @Test
    @Transactional
    void createLigneCommandeClient() throws Exception {
        int databaseSizeBeforeCreate = ligneCommandeClientRepository.findAll().size();
        // Create the LigneCommandeClient
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);
        restLigneCommandeClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeCreate + 1);
        LigneCommandeClient testLigneCommandeClient = ligneCommandeClientList.get(ligneCommandeClientList.size() - 1);
        assertThat(testLigneCommandeClient.getQuantite()).isEqualByComparingTo(DEFAULT_QUANTITE);
        assertThat(testLigneCommandeClient.getPrixUnitaire()).isEqualByComparingTo(DEFAULT_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void createLigneCommandeClientWithExistingId() throws Exception {
        // Create the LigneCommandeClient with an existing ID
        ligneCommandeClient.setId(1L);
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);

        int databaseSizeBeforeCreate = ligneCommandeClientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneCommandeClientMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLigneCommandeClients() throws Exception {
        // Initialize the database
        ligneCommandeClientRepository.saveAndFlush(ligneCommandeClient);

        // Get all the ligneCommandeClientList
        restLigneCommandeClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneCommandeClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(sameNumber(DEFAULT_QUANTITE))))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(sameNumber(DEFAULT_PRIX_UNITAIRE))));
    }

    @Test
    @Transactional
    void getLigneCommandeClient() throws Exception {
        // Initialize the database
        ligneCommandeClientRepository.saveAndFlush(ligneCommandeClient);

        // Get the ligneCommandeClient
        restLigneCommandeClientMockMvc
            .perform(get(ENTITY_API_URL_ID, ligneCommandeClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneCommandeClient.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(sameNumber(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.prixUnitaire").value(sameNumber(DEFAULT_PRIX_UNITAIRE)));
    }

    @Test
    @Transactional
    void getNonExistingLigneCommandeClient() throws Exception {
        // Get the ligneCommandeClient
        restLigneCommandeClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLigneCommandeClient() throws Exception {
        // Initialize the database
        ligneCommandeClientRepository.saveAndFlush(ligneCommandeClient);

        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();

        // Update the ligneCommandeClient
        LigneCommandeClient updatedLigneCommandeClient = ligneCommandeClientRepository.findById(ligneCommandeClient.getId()).get();
        // Disconnect from session so that the updates on updatedLigneCommandeClient are not directly saved in db
        em.detach(updatedLigneCommandeClient);
        updatedLigneCommandeClient.quantite(UPDATED_QUANTITE).prixUnitaire(UPDATED_PRIX_UNITAIRE);
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(updatedLigneCommandeClient);

        restLigneCommandeClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneCommandeClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
        LigneCommandeClient testLigneCommandeClient = ligneCommandeClientList.get(ligneCommandeClientList.size() - 1);
        assertThat(testLigneCommandeClient.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testLigneCommandeClient.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void putNonExistingLigneCommandeClient() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();
        ligneCommandeClient.setId(count.incrementAndGet());

        // Create the LigneCommandeClient
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneCommandeClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneCommandeClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigneCommandeClient() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();
        ligneCommandeClient.setId(count.incrementAndGet());

        // Create the LigneCommandeClient
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigneCommandeClient() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();
        ligneCommandeClient.setId(count.incrementAndGet());

        // Create the LigneCommandeClient
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeClientMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigneCommandeClientWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeClientRepository.saveAndFlush(ligneCommandeClient);

        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();

        // Update the ligneCommandeClient using partial update
        LigneCommandeClient partialUpdatedLigneCommandeClient = new LigneCommandeClient();
        partialUpdatedLigneCommandeClient.setId(ligneCommandeClient.getId());

        restLigneCommandeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneCommandeClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommandeClient))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
        LigneCommandeClient testLigneCommandeClient = ligneCommandeClientList.get(ligneCommandeClientList.size() - 1);
        assertThat(testLigneCommandeClient.getQuantite()).isEqualByComparingTo(DEFAULT_QUANTITE);
        assertThat(testLigneCommandeClient.getPrixUnitaire()).isEqualByComparingTo(DEFAULT_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void fullUpdateLigneCommandeClientWithPatch() throws Exception {
        // Initialize the database
        ligneCommandeClientRepository.saveAndFlush(ligneCommandeClient);

        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();

        // Update the ligneCommandeClient using partial update
        LigneCommandeClient partialUpdatedLigneCommandeClient = new LigneCommandeClient();
        partialUpdatedLigneCommandeClient.setId(ligneCommandeClient.getId());

        partialUpdatedLigneCommandeClient.quantite(UPDATED_QUANTITE).prixUnitaire(UPDATED_PRIX_UNITAIRE);

        restLigneCommandeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneCommandeClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLigneCommandeClient))
            )
            .andExpect(status().isOk());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
        LigneCommandeClient testLigneCommandeClient = ligneCommandeClientList.get(ligneCommandeClientList.size() - 1);
        assertThat(testLigneCommandeClient.getQuantite()).isEqualByComparingTo(UPDATED_QUANTITE);
        assertThat(testLigneCommandeClient.getPrixUnitaire()).isEqualByComparingTo(UPDATED_PRIX_UNITAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingLigneCommandeClient() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();
        ligneCommandeClient.setId(count.incrementAndGet());

        // Create the LigneCommandeClient
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneCommandeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligneCommandeClientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigneCommandeClient() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();
        ligneCommandeClient.setId(count.incrementAndGet());

        // Create the LigneCommandeClient
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigneCommandeClient() throws Exception {
        int databaseSizeBeforeUpdate = ligneCommandeClientRepository.findAll().size();
        ligneCommandeClient.setId(count.incrementAndGet());

        // Create the LigneCommandeClient
        LigneCommandeClientDTO ligneCommandeClientDTO = ligneCommandeClientMapper.toDto(ligneCommandeClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneCommandeClientMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ligneCommandeClientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneCommandeClient in the database
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLigneCommandeClient() throws Exception {
        // Initialize the database
        ligneCommandeClientRepository.saveAndFlush(ligneCommandeClient);

        int databaseSizeBeforeDelete = ligneCommandeClientRepository.findAll().size();

        // Delete the ligneCommandeClient
        restLigneCommandeClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligneCommandeClient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LigneCommandeClient> ligneCommandeClientList = ligneCommandeClientRepository.findAll();
        assertThat(ligneCommandeClientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
