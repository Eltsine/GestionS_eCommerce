package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Personnel;
import com.mycompany.myapp.repository.PersonnelRepository;
import com.mycompany.myapp.service.dto.PersonnelDTO;
import com.mycompany.myapp.service.mapper.PersonnelMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PersonnelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonnelResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOT_DE_PASS = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/personnel";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private PersonnelMapper personnelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonnelMockMvc;

    private Personnel personnel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnel createEntity(EntityManager em) {
        Personnel personnel = new Personnel()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .email(DEFAULT_EMAIL)
            .motDePass(DEFAULT_MOT_DE_PASS)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return personnel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personnel createUpdatedEntity(EntityManager em) {
        Personnel personnel = new Personnel()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .motDePass(UPDATED_MOT_DE_PASS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return personnel;
    }

    @BeforeEach
    public void initTest() {
        personnel = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonnel() throws Exception {
        int databaseSizeBeforeCreate = personnelRepository.findAll().size();
        // Create the Personnel
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);
        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnelDTO)))
            .andExpect(status().isCreated());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeCreate + 1);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonnel.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testPersonnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonnel.getMotDePass()).isEqualTo(DEFAULT_MOT_DE_PASS);
        assertThat(testPersonnel.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPersonnel.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPersonnelWithExistingId() throws Exception {
        // Create the Personnel with an existing ID
        personnel.setId(1L);
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);

        int databaseSizeBeforeCreate = personnelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonnelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get all the personnelList
        restPersonnelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].motDePass").value(hasItem(DEFAULT_MOT_DE_PASS)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @Test
    @Transactional
    void getPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        // Get the personnel
        restPersonnelMockMvc
            .perform(get(ENTITY_API_URL_ID, personnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personnel.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.motDePass").value(DEFAULT_MOT_DE_PASS))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    void getNonExistingPersonnel() throws Exception {
        // Get the personnel
        restPersonnelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel
        Personnel updatedPersonnel = personnelRepository.findById(personnel.getId()).get();
        // Disconnect from session so that the updates on updatedPersonnel are not directly saved in db
        em.detach(updatedPersonnel);
        updatedPersonnel
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .motDePass(UPDATED_MOT_DE_PASS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        PersonnelDTO personnelDTO = personnelMapper.toDto(updatedPersonnel);

        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personnelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personnelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonnel.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonnel.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonnel.getMotDePass()).isEqualTo(UPDATED_MOT_DE_PASS);
        assertThat(testPersonnel.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPersonnel.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // Create the Personnel
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personnelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // Create the Personnel
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // Create the Personnel
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personnelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonnelWithPatch() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel using partial update
        Personnel partialUpdatedPersonnel = new Personnel();
        partialUpdatedPersonnel.setId(personnel.getId());

        partialUpdatedPersonnel.prenom(UPDATED_PRENOM).dateNaissance(UPDATED_DATE_NAISSANCE);

        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonnel.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonnel.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonnel.getMotDePass()).isEqualTo(DEFAULT_MOT_DE_PASS);
        assertThat(testPersonnel.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPersonnel.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePersonnelWithPatch() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();

        // Update the personnel using partial update
        Personnel partialUpdatedPersonnel = new Personnel();
        partialUpdatedPersonnel.setId(personnel.getId());

        partialUpdatedPersonnel
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .motDePass(UPDATED_MOT_DE_PASS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonnel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonnel))
            )
            .andExpect(status().isOk());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
        Personnel testPersonnel = personnelList.get(personnelList.size() - 1);
        assertThat(testPersonnel.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonnel.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonnel.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonnel.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonnel.getMotDePass()).isEqualTo(UPDATED_MOT_DE_PASS);
        assertThat(testPersonnel.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPersonnel.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // Create the Personnel
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personnelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // Create the Personnel
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personnelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonnel() throws Exception {
        int databaseSizeBeforeUpdate = personnelRepository.findAll().size();
        personnel.setId(count.incrementAndGet());

        // Create the Personnel
        PersonnelDTO personnelDTO = personnelMapper.toDto(personnel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonnelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personnelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personnel in the database
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonnel() throws Exception {
        // Initialize the database
        personnelRepository.saveAndFlush(personnel);

        int databaseSizeBeforeDelete = personnelRepository.findAll().size();

        // Delete the personnel
        restPersonnelMockMvc
            .perform(delete(ENTITY_API_URL_ID, personnel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personnel> personnelList = personnelRepository.findAll();
        assertThat(personnelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
