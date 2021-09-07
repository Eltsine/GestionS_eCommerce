package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Entreprise;
import com.mycompany.myapp.repository.EntrepriseRepository;
import com.mycompany.myapp.service.dto.EntrepriseDTO;
import com.mycompany.myapp.service.mapper.EntrepriseMapper;
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
 * Integration tests for the {@link EntrepriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntrepriseResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_FISCAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_FISCAL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_TE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TE = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_WEB = "AAAAAAAAAA";
    private static final String UPDATED_SITE_WEB = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entreprises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private EntrepriseMapper entrepriseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntrepriseMockMvc;

    private Entreprise entreprise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprise createEntity(EntityManager em) {
        Entreprise entreprise = new Entreprise()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .codeFiscal(DEFAULT_CODE_FISCAL)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .email(DEFAULT_EMAIL)
            .numTe(DEFAULT_NUM_TE)
            .siteWeb(DEFAULT_SITE_WEB);
        return entreprise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprise createUpdatedEntity(EntityManager em) {
        Entreprise entreprise = new Entreprise()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .codeFiscal(UPDATED_CODE_FISCAL)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .email(UPDATED_EMAIL)
            .numTe(UPDATED_NUM_TE)
            .siteWeb(UPDATED_SITE_WEB);
        return entreprise;
    }

    @BeforeEach
    public void initTest() {
        entreprise = createEntity(em);
    }

    @Test
    @Transactional
    void createEntreprise() throws Exception {
        int databaseSizeBeforeCreate = entrepriseRepository.findAll().size();
        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);
        restEntrepriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isCreated());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeCreate + 1);
        Entreprise testEntreprise = entrepriseList.get(entrepriseList.size() - 1);
        assertThat(testEntreprise.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEntreprise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEntreprise.getCodeFiscal()).isEqualTo(DEFAULT_CODE_FISCAL);
        assertThat(testEntreprise.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEntreprise.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testEntreprise.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEntreprise.getNumTe()).isEqualTo(DEFAULT_NUM_TE);
        assertThat(testEntreprise.getSiteWeb()).isEqualTo(DEFAULT_SITE_WEB);
    }

    @Test
    @Transactional
    void createEntrepriseWithExistingId() throws Exception {
        // Create the Entreprise with an existing ID
        entreprise.setId(1L);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        int databaseSizeBeforeCreate = entrepriseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntrepriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEntreprises() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        // Get all the entrepriseList
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreprise.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].codeFiscal").value(hasItem(DEFAULT_CODE_FISCAL)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].numTe").value(hasItem(DEFAULT_NUM_TE)))
            .andExpect(jsonPath("$.[*].siteWeb").value(hasItem(DEFAULT_SITE_WEB)));
    }

    @Test
    @Transactional
    void getEntreprise() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        // Get the entreprise
        restEntrepriseMockMvc
            .perform(get(ENTITY_API_URL_ID, entreprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entreprise.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.codeFiscal").value(DEFAULT_CODE_FISCAL))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.numTe").value(DEFAULT_NUM_TE))
            .andExpect(jsonPath("$.siteWeb").value(DEFAULT_SITE_WEB));
    }

    @Test
    @Transactional
    void getNonExistingEntreprise() throws Exception {
        // Get the entreprise
        restEntrepriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEntreprise() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();

        // Update the entreprise
        Entreprise updatedEntreprise = entrepriseRepository.findById(entreprise.getId()).get();
        // Disconnect from session so that the updates on updatedEntreprise are not directly saved in db
        em.detach(updatedEntreprise);
        updatedEntreprise
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .codeFiscal(UPDATED_CODE_FISCAL)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .email(UPDATED_EMAIL)
            .numTe(UPDATED_NUM_TE)
            .siteWeb(UPDATED_SITE_WEB);
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(updatedEntreprise);

        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
        Entreprise testEntreprise = entrepriseList.get(entrepriseList.size() - 1);
        assertThat(testEntreprise.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEntreprise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEntreprise.getCodeFiscal()).isEqualTo(UPDATED_CODE_FISCAL);
        assertThat(testEntreprise.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEntreprise.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testEntreprise.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEntreprise.getNumTe()).isEqualTo(UPDATED_NUM_TE);
        assertThat(testEntreprise.getSiteWeb()).isEqualTo(UPDATED_SITE_WEB);
    }

    @Test
    @Transactional
    void putNonExistingEntreprise() throws Exception {
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();
        entreprise.setId(count.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntreprise() throws Exception {
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();
        entreprise.setId(count.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntreprise() throws Exception {
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();
        entreprise.setId(count.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntrepriseWithPatch() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();

        // Update the entreprise using partial update
        Entreprise partialUpdatedEntreprise = new Entreprise();
        partialUpdatedEntreprise.setId(entreprise.getId());

        partialUpdatedEntreprise
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .codeFiscal(UPDATED_CODE_FISCAL)
            .siteWeb(UPDATED_SITE_WEB);

        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntreprise))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
        Entreprise testEntreprise = entrepriseList.get(entrepriseList.size() - 1);
        assertThat(testEntreprise.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEntreprise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEntreprise.getCodeFiscal()).isEqualTo(UPDATED_CODE_FISCAL);
        assertThat(testEntreprise.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testEntreprise.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testEntreprise.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEntreprise.getNumTe()).isEqualTo(DEFAULT_NUM_TE);
        assertThat(testEntreprise.getSiteWeb()).isEqualTo(UPDATED_SITE_WEB);
    }

    @Test
    @Transactional
    void fullUpdateEntrepriseWithPatch() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();

        // Update the entreprise using partial update
        Entreprise partialUpdatedEntreprise = new Entreprise();
        partialUpdatedEntreprise.setId(entreprise.getId());

        partialUpdatedEntreprise
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .codeFiscal(UPDATED_CODE_FISCAL)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .email(UPDATED_EMAIL)
            .numTe(UPDATED_NUM_TE)
            .siteWeb(UPDATED_SITE_WEB);

        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntreprise))
            )
            .andExpect(status().isOk());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
        Entreprise testEntreprise = entrepriseList.get(entrepriseList.size() - 1);
        assertThat(testEntreprise.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEntreprise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEntreprise.getCodeFiscal()).isEqualTo(UPDATED_CODE_FISCAL);
        assertThat(testEntreprise.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testEntreprise.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testEntreprise.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEntreprise.getNumTe()).isEqualTo(UPDATED_NUM_TE);
        assertThat(testEntreprise.getSiteWeb()).isEqualTo(UPDATED_SITE_WEB);
    }

    @Test
    @Transactional
    void patchNonExistingEntreprise() throws Exception {
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();
        entreprise.setId(count.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entrepriseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntreprise() throws Exception {
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();
        entreprise.setId(count.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entrepriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntreprise() throws Exception {
        int databaseSizeBeforeUpdate = entrepriseRepository.findAll().size();
        entreprise.setId(count.incrementAndGet());

        // Create the Entreprise
        EntrepriseDTO entrepriseDTO = entrepriseMapper.toDto(entreprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepriseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(entrepriseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprise in the database
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntreprise() throws Exception {
        // Initialize the database
        entrepriseRepository.saveAndFlush(entreprise);

        int databaseSizeBeforeDelete = entrepriseRepository.findAll().size();

        // Delete the entreprise
        restEntrepriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, entreprise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entreprise> entrepriseList = entrepriseRepository.findAll();
        assertThat(entrepriseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
