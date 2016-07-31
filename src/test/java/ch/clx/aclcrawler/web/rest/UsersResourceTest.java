package ch.clx.aclcrawler.web.rest;

import ch.clx.aclcrawler.Application;
import ch.clx.aclcrawler.domain.Users;
import ch.clx.aclcrawler.repository.UsersRepository;

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

import ch.clx.aclcrawler.domain.enumeration.UserStatus;

/**
 * Test class for the UsersResource REST controller.
 *
 * @see UsersResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UsersResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_USER_ID = 1l;
    private static final Long UPDATED_USER_ID = 2l;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SURNAME = "AAAAA";
    private static final String UPDATED_SURNAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";


private static final UserStatus DEFAULT_STATUS = UserStatus.ALL;
    private static final UserStatus UPDATED_STATUS = UserStatus.ACTIVE;

    private static final Long DEFAULT_CREATE_BY = 1l;
    private static final Long UPDATED_CREATE_BY = 2l;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final Long DEFAULT_MODIFY_BY = 1l;
    private static final Long UPDATED_MODIFY_BY = 2l;

    private static final ZonedDateTime DEFAULT_MODIFY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFY_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFY_DATE);

    @Inject
    private UsersRepository usersRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUsersMockMvc;

    private Users users;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UsersResource usersResource = new UsersResource();
        ReflectionTestUtils.setField(usersResource, "usersRepository", usersRepository);
        this.restUsersMockMvc = MockMvcBuilders.standaloneSetup(usersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        users = new Users();
        users.setName(DEFAULT_NAME);
        users.setEmail(DEFAULT_EMAIL);
        users.setPassword(DEFAULT_PASSWORD);
        users.setStatus(DEFAULT_STATUS);
        users.setCreate_by(DEFAULT_CREATE_BY);
        users.setCreate_date(DEFAULT_CREATE_DATE);
        users.setModify_by(DEFAULT_MODIFY_BY);
        users.setModify_date(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createUsers() throws Exception {
        int databaseSizeBeforeCreate = usersRepository.findAll().size();

        // Create the Users

        restUsersMockMvc.perform(post("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isCreated());

        // Validate the Users in the database
        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeCreate + 1);
        Users testUsers = userss.get(userss.size() - 1);
        assertThat(testUsers.getUser_id()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUsers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUsers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsers.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUsers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUsers.getCreate_by()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testUsers.getCreate_date()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testUsers.getModify_by()).isEqualTo(DEFAULT_MODIFY_BY);
        assertThat(testUsers.getModify_date()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void checkUser_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setUser_id(null);

        // Create the Users, which fails.

        restUsersMockMvc.perform(post("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isBadRequest());

        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setName(null);

        // Create the Users, which fails.

        restUsersMockMvc.perform(post("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isBadRequest());

        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setEmail(null);

        // Create the Users, which fails.

        restUsersMockMvc.perform(post("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isBadRequest());

        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setStatus(null);

        // Create the Users, which fails.

        restUsersMockMvc.perform(post("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isBadRequest());

        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreate_byIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setCreate_by(null);

        // Create the Users, which fails.

        restUsersMockMvc.perform(post("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isBadRequest());

        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreate_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersRepository.findAll().size();
        // set the field null
        users.setCreate_date(null);

        // Create the Users, which fails.

        restUsersMockMvc.perform(post("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isBadRequest());

        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserss() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get all the userss
        restUsersMockMvc.perform(get("/api/userss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].user_id").value(hasItem(users.getUser_id().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].create_by").value(hasItem(DEFAULT_CREATE_BY)))
                .andExpect(jsonPath("$.[*].create_date").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].modify_by").value(hasItem(DEFAULT_MODIFY_BY)))
                .andExpect(jsonPath("$.[*].modify_date").value(hasItem(DEFAULT_MODIFY_DATE_STR)));
    }

    @Test
    @Transactional
    public void getUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc.perform(get("/api/userss/{id}", users.getUser_id()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.user_id").value(users.getUser_id().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.create_by").value(DEFAULT_CREATE_BY))
            .andExpect(jsonPath("$.create_date").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.modify_by").value(DEFAULT_MODIFY_BY))
            .andExpect(jsonPath("$.modify_date").value(DEFAULT_MODIFY_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get("/api/userss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

		int databaseSizeBeforeUpdate = usersRepository.findAll().size();

        // Update the users
        users.setName(UPDATED_NAME);
        users.setEmail(UPDATED_EMAIL);
        users.setPassword(UPDATED_PASSWORD);
        users.setStatus(UPDATED_STATUS);
        users.setCreate_by(UPDATED_CREATE_BY);
        users.setCreate_date(UPDATED_CREATE_DATE);
        users.setModify_by(UPDATED_MODIFY_BY);
        users.setModify_date(UPDATED_MODIFY_DATE);

        restUsersMockMvc.perform(put("/api/userss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(users)))
                .andExpect(status().isOk());

        // Validate the Users in the database
        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeUpdate);
        Users testUsers = userss.get(userss.size() - 1);
        assertThat(testUsers.getUser_id()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUsers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUsers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsers.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUsers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUsers.getCreate_by()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testUsers.getCreate_date()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testUsers.getModify_by()).isEqualTo(UPDATED_MODIFY_BY);
        assertThat(testUsers.getModify_date()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void deleteUsers() throws Exception {
        // Initialize the database
        usersRepository.saveAndFlush(users);

		int databaseSizeBeforeDelete = usersRepository.findAll().size();

        // Get the users
        restUsersMockMvc.perform(delete("/api/userss/{id}", users.getUser_id())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Users> userss = usersRepository.findAll();
        assertThat(userss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
