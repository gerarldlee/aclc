package ch.clx.aclcrawler.web.rest;

import ch.clx.aclcrawler.Application;
import ch.clx.aclcrawler.domain.Password_history;
import ch.clx.aclcrawler.repository.Password_historyRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the Password_historyResource REST controller.
 *
 * @see Password_historyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Password_historyResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_PASSWORD_HISTORY_ID = 1l;
    private static final Long UPDATED_PASSWORD_HISTORY_ID = 2l;

    private static final Long DEFAULT_USER_ID = 1l;
    private static final Long UPDATED_USER_ID = 2l;
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    @Inject
    private Password_historyRepository password_historyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPassword_historyMockMvc;

    private Password_history password_history;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Password_historyResource password_historyResource = new Password_historyResource();
        ReflectionTestUtils.setField(password_historyResource, "password_historyRepository", password_historyRepository);
        this.restPassword_historyMockMvc = MockMvcBuilders.standaloneSetup(password_historyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        password_history = new Password_history();
        password_history.setUser_id(DEFAULT_USER_ID);
        password_history.setPassword(DEFAULT_PASSWORD);
        password_history.setCreate_date(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createPassword_history() throws Exception {
        int databaseSizeBeforeCreate = password_historyRepository.findAll().size();

        // Create the Password_history

        restPassword_historyMockMvc.perform(post("/api/password_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(password_history)))
                .andExpect(status().isCreated());

        // Validate the Password_history in the database
        List<Password_history> password_historys = password_historyRepository.findAll();
        assertThat(password_historys).hasSize(databaseSizeBeforeCreate + 1);
        Password_history testPassword_history = password_historys.get(password_historys.size() - 1);
        assertThat(testPassword_history.getPassword_history_id()).isEqualTo(DEFAULT_PASSWORD_HISTORY_ID);
        assertThat(testPassword_history.getUser_id()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPassword_history.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testPassword_history.getCreate_date()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void checkPassword_history_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = password_historyRepository.findAll().size();
        // set the field null
        password_history.setPassword_history_id(null);

        // Create the Password_history, which fails.

        restPassword_historyMockMvc.perform(post("/api/password_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(password_history)))
                .andExpect(status().isBadRequest());

        List<Password_history> password_historys = password_historyRepository.findAll();
        assertThat(password_historys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUser_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = password_historyRepository.findAll().size();
        // set the field null
        password_history.setUser_id(null);

        // Create the Password_history, which fails.

        restPassword_historyMockMvc.perform(post("/api/password_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(password_history)))
                .andExpect(status().isBadRequest());

        List<Password_history> password_historys = password_historyRepository.findAll();
        assertThat(password_historys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreate_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = password_historyRepository.findAll().size();
        // set the field null
        password_history.setCreate_date(null);

        // Create the Password_history, which fails.

        restPassword_historyMockMvc.perform(post("/api/password_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(password_history)))
                .andExpect(status().isBadRequest());

        List<Password_history> password_historys = password_historyRepository.findAll();
        assertThat(password_historys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPassword_historys() throws Exception {
        // Initialize the database
        password_historyRepository.saveAndFlush(password_history);

        // Get all the password_historys
        restPassword_historyMockMvc.perform(get("/api/password_historys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].password_history_id").value(hasItem(password_history.getPassword_history_id().intValue())))
                .andExpect(jsonPath("$.[*].user_id").value(hasItem(DEFAULT_USER_ID)))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].create_date").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getPassword_history() throws Exception {
        // Initialize the database
        password_historyRepository.saveAndFlush(password_history);

        // Get the password_history
        restPassword_historyMockMvc.perform(get("/api/password_historys/{id}", password_history.getPassword_history_id()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.password_history_id").value(password_history.getPassword_history_id().intValue()))
            .andExpect(jsonPath("$.user_id").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.create_date").value(DEFAULT_CREATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPassword_history() throws Exception {
        // Get the password_history
        restPassword_historyMockMvc.perform(get("/api/password_historys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassword_history() throws Exception {
        // Initialize the database
        password_historyRepository.saveAndFlush(password_history);

		int databaseSizeBeforeUpdate = password_historyRepository.findAll().size();

        // Update the password_history
        password_history.setUser_id(UPDATED_USER_ID);
        password_history.setPassword(UPDATED_PASSWORD);
        password_history.setCreate_date(UPDATED_CREATE_DATE);

        restPassword_historyMockMvc.perform(put("/api/password_historys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(password_history)))
                .andExpect(status().isOk());

        // Validate the Password_history in the database
        List<Password_history> password_historys = password_historyRepository.findAll();
        assertThat(password_historys).hasSize(databaseSizeBeforeUpdate);
        Password_history testPassword_history = password_historys.get(password_historys.size() - 1);
        assertThat(testPassword_history.getPassword_history_id()).isEqualTo(UPDATED_PASSWORD_HISTORY_ID);
        assertThat(testPassword_history.getUser_id()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPassword_history.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testPassword_history.getCreate_date()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void deletePassword_history() throws Exception {
        // Initialize the database
        password_historyRepository.saveAndFlush(password_history);

		int databaseSizeBeforeDelete = password_historyRepository.findAll().size();

        // Get the password_history
        restPassword_historyMockMvc.perform(delete("/api/password_historys/{id}",
            password_history.getPassword_history_id())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Password_history> password_historys = password_historyRepository.findAll();
        assertThat(password_historys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
