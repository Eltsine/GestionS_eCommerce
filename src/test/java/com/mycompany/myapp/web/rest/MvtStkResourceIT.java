package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MvtStk;
import com.mycompany.myapp.domain.enumeration.TypeMvtStk;
import com.mycompany.myapp.repository.MvtStkRepository;
import com.mycompany.myapp.service.dto.MvtStkDTO;
import com.mycompany.myapp.service.mapper.MvtStkMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link MvtStkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MvtStkResourceIT {

    private static final Instant DEFAULT_DATE_MVT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MVT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(2);

    private static final TypeMvtStk DEFAULT_TYPE_MVT_STK = TypeMvtStk.ENTREE;
    private static final TypeMvtStk UPDATED_TYPE_MVT_STK = TypeMvtStk.SORTIE;

    private static final String ENTITY_API_URL = "/api/mvt-stks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MvtStkRepository mvtStkRepository;

    @Autowired
    private MvtStkMapper mvtStkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMvtStkMockMvc;

    private MvtStk mvtStk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MvtStk createEntity(EntityManager em) {
        MvtStk mvtStk = new MvtStk().dateMvt(DEFAULT_DATE_MVT).quantite(DEFAULT_QUANTITE).typeMvtStk(DEFAULT_TYPE_MVT_STK);
        return mvtStk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MvtStk createUpdatedEntity(EntityManager em) {
        MvtStk mvtStk = new MvtStk().dateMvt(UPDATED_DATE_MVT).quantite(UPDATED_QUANTITE).typeMvtStk(UPDATED_TYPE_MVT_STK);
        return mvtStk;
    }

    @BeforeEach
    public void initTest() {
        mvtStk = createEntity(em);
    }

    @Test
    @Transactional
    void createMvtStk() throws Exception {
        int databaseSizeBeforeCreate = mvtStkRepository.findAll().size();
        // Create the MvtStk
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);
        restMvtStkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mvtStkDTO)))
            .andExpect(status().isCreated());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeCreate + 1);
        MvtStk testMvtStk = mvtStkList.get(mvtStkList.size() - 1);
        assertThat(testMvtStk.getDateMvt()).isEqualTo(DEFAULT_DATE_MVT);
        assertThat(testMvtStk.getQuantite()).isEqualByComparingTo(DEFAULT_QUANTITE);
        assertThat(testMvtStk.getTypeMvtStk()).isEqualTo(DEFAULT_TYPE_MVT_STK);
    }

    @Test
    @Transactional
    void createMvtStkWithExistingId() throws Exception {
        // Create the MvtStk with an existing ID
        mvtStk.setId(1L);
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);

        int databaseSizeBeforeCreate = mvtStkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMvtStkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mvtStkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMvtStks() throws Exception {
        // Initialize the database
        mvtStkRepository.saveAndFlush(mvtStk);

        // Get all the mvtStkList
        restMvtStkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mvtStk.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateMvt").value(hasItem(DEFAULT_DATE_MVT.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(sameNumber(DEFAULT_QUANTITE))))
            .andExpect(jsonPath("$.[*].typeMvtStk").value(hasItem(DEFAULT_TYPE_MVT_STK.toString())));
    }

    @Test
    @Transactional
    void getMvtStk() throws Exception {
        // Initialize the database
        mvtStkRepository.saveAndFlush(mvtStk);

        // Get the mvtStk
        restMvtStkMockMvc
            .perform(get(ENTITY_API_URL_ID, mvtStk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mvtStk.getId().intValue()))
            .andExpect(jsonPath("$.dateMvt").value(DEFAULT_DATE_MVT.toString()))
            .andExpect(jsonPath("$.quantite").value(sameNumber(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.typeMvtStk").value(DEFAULT_TYPE_MVT_STK.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMvtStk() throws Exception {
        // Get the mvtStk
        restMvtStkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMvtStk() throws Exception {
        // Initialize the database
        mvtStkRepository.saveAndFlush(mvtStk);

        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();

        // Update the mvtStk
        MvtStk updatedMvtStk = mvtStkRepository.findById(mvtStk.getId()).get();
        // Disconnect from session so that the updates on updatedMvtStk are not directly saved in db
        em.detach(updatedMvtStk);
        updatedMvtStk.dateMvt(UPDATED_DATE_MVT).quantite(UPDATED_QUANTITE).typeMvtStk(UPDATED_TYPE_MVT_STK);
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(updatedMvtStk);

        restMvtStkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mvtStkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mvtStkDTO))
            )
            .andExpect(status().isOk());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
        MvtStk testMvtStk = mvtStkList.get(mvtStkList.size() - 1);
        assertThat(testMvtStk.getDateMvt()).isEqualTo(UPDATED_DATE_MVT);
        assertThat(testMvtStk.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testMvtStk.getTypeMvtStk()).isEqualTo(UPDATED_TYPE_MVT_STK);
    }

    @Test
    @Transactional
    void putNonExistingMvtStk() throws Exception {
        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();
        mvtStk.setId(count.incrementAndGet());

        // Create the MvtStk
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMvtStkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mvtStkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mvtStkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMvtStk() throws Exception {
        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();
        mvtStk.setId(count.incrementAndGet());

        // Create the MvtStk
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMvtStkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mvtStkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMvtStk() throws Exception {
        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();
        mvtStk.setId(count.incrementAndGet());

        // Create the MvtStk
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMvtStkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mvtStkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMvtStkWithPatch() throws Exception {
        // Initialize the database
        mvtStkRepository.saveAndFlush(mvtStk);

        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();

        // Update the mvtStk using partial update
        MvtStk partialUpdatedMvtStk = new MvtStk();
        partialUpdatedMvtStk.setId(mvtStk.getId());

        restMvtStkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMvtStk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMvtStk))
            )
            .andExpect(status().isOk());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
        MvtStk testMvtStk = mvtStkList.get(mvtStkList.size() - 1);
        assertThat(testMvtStk.getDateMvt()).isEqualTo(DEFAULT_DATE_MVT);
        assertThat(testMvtStk.getQuantite()).isEqualByComparingTo(DEFAULT_QUANTITE);
        assertThat(testMvtStk.getTypeMvtStk()).isEqualTo(DEFAULT_TYPE_MVT_STK);
    }

    @Test
    @Transactional
    void fullUpdateMvtStkWithPatch() throws Exception {
        // Initialize the database
        mvtStkRepository.saveAndFlush(mvtStk);

        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();

        // Update the mvtStk using partial update
        MvtStk partialUpdatedMvtStk = new MvtStk();
        partialUpdatedMvtStk.setId(mvtStk.getId());

        partialUpdatedMvtStk.dateMvt(UPDATED_DATE_MVT).quantite(UPDATED_QUANTITE).typeMvtStk(UPDATED_TYPE_MVT_STK);

        restMvtStkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMvtStk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMvtStk))
            )
            .andExpect(status().isOk());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
        MvtStk testMvtStk = mvtStkList.get(mvtStkList.size() - 1);
        assertThat(testMvtStk.getDateMvt()).isEqualTo(UPDATED_DATE_MVT);
        assertThat(testMvtStk.getQuantite()).isEqualByComparingTo(UPDATED_QUANTITE);
        assertThat(testMvtStk.getTypeMvtStk()).isEqualTo(UPDATED_TYPE_MVT_STK);
    }

    @Test
    @Transactional
    void patchNonExistingMvtStk() throws Exception {
        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();
        mvtStk.setId(count.incrementAndGet());

        // Create the MvtStk
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMvtStkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mvtStkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mvtStkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMvtStk() throws Exception {
        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();
        mvtStk.setId(count.incrementAndGet());

        // Create the MvtStk
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMvtStkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mvtStkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMvtStk() throws Exception {
        int databaseSizeBeforeUpdate = mvtStkRepository.findAll().size();
        mvtStk.setId(count.incrementAndGet());

        // Create the MvtStk
        MvtStkDTO mvtStkDTO = mvtStkMapper.toDto(mvtStk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMvtStkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mvtStkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MvtStk in the database
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMvtStk() throws Exception {
        // Initialize the database
        mvtStkRepository.saveAndFlush(mvtStk);

        int databaseSizeBeforeDelete = mvtStkRepository.findAll().size();

        // Delete the mvtStk
        restMvtStkMockMvc
            .perform(delete(ENTITY_API_URL_ID, mvtStk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MvtStk> mvtStkList = mvtStkRepository.findAll();
        assertThat(mvtStkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
