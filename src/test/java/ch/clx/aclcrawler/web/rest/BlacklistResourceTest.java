package ch.clx.aclcrawler.web.rest;

import ch.clx.aclcrawler.Application;
import ch.clx.aclcrawler.domain.Blacklist;
import ch.clx.aclcrawler.repository.BlacklistRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BlacklistResource REST controller.
 *
 * @see BlacklistResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BlacklistResourceTest {


    private static final Long DEFAULT_BLACKLIST_ID = 1l;
    private static final Long UPDATED_BLACKLIST_ID = 2l;
    private static final String DEFAULT_PARTIAL_QUALIFIED_NAME = "AAAAA";
    private static final String UPDATED_PARTIAL_QUALIFIED_NAME = "BBBBB";

    @Inject
    private BlacklistRepository blacklistRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBlacklistMockMvc;

    private Blacklist blacklist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BlacklistResource blacklistResource = new BlacklistResource();
        ReflectionTestUtils.setField(blacklistResource, "blacklistRepository", blacklistRepository);
        this.restBlacklistMockMvc = MockMvcBuilders.standaloneSetup(blacklistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        blacklist = new Blacklist();
        blacklist.setPartial_qualified_name(DEFAULT_PARTIAL_QUALIFIED_NAME);
    }

    @Test
    @Transactional
    public void createBlacklist() throws Exception {
        int databaseSizeBeforeCreate = blacklistRepository.findAll().size();

        // Create the Blacklist

        restBlacklistMockMvc.perform(post("/api/blacklists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(blacklist)))
                .andExpect(status().isCreated());

        // Validate the Blacklist in the database
        List<Blacklist> blacklists = blacklistRepository.findAll();
        assertThat(blacklists).hasSize(databaseSizeBeforeCreate + 1);
        Blacklist testBlacklist = blacklists.get(blacklists.size() - 1);
        assertThat(testBlacklist.getBlacklist_id()).isEqualTo(DEFAULT_BLACKLIST_ID);
        assertThat(testBlacklist.getPartial_qualified_name()).isEqualTo(DEFAULT_PARTIAL_QUALIFIED_NAME);
    }

    @Test
    @Transactional
    public void checkBlacklist_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = blacklistRepository.findAll().size();
        // set the field null
        blacklist.setBlacklist_id(null);

        // Create the Blacklist, which fails.

        restBlacklistMockMvc.perform(post("/api/blacklists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(blacklist)))
                .andExpect(status().isBadRequest());

        List<Blacklist> blacklists = blacklistRepository.findAll();
        assertThat(blacklists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBlacklists() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

        // Get all the blacklists
        restBlacklistMockMvc.perform(get("/api/blacklists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].blacklist_id").value(hasItem(blacklist.getBlacklist_id()
                    .intValue())))
                .andExpect(jsonPath("$.[*].partial_qualified_name").value(hasItem(DEFAULT_PARTIAL_QUALIFIED_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBlacklist() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

        // Get the blacklist
        restBlacklistMockMvc.perform(get("/api/blacklists/{id}", blacklist.getBlacklist_id()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.blacklist_id").value(blacklist.getBlacklist_id().intValue()))
            .andExpect(jsonPath("$.partial_qualified_name").value(DEFAULT_PARTIAL_QUALIFIED_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBlacklist() throws Exception {
        // Get the blacklist
        restBlacklistMockMvc.perform(get("/api/blacklists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlacklist() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

		int databaseSizeBeforeUpdate = blacklistRepository.findAll().size();

        // Update the blacklist
        blacklist.setPartial_qualified_name(UPDATED_PARTIAL_QUALIFIED_NAME);

        restBlacklistMockMvc.perform(put("/api/blacklists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(blacklist)))
                .andExpect(status().isOk());

        // Validate the Blacklist in the database
        List<Blacklist> blacklists = blacklistRepository.findAll();
        assertThat(blacklists).hasSize(databaseSizeBeforeUpdate);
        Blacklist testBlacklist = blacklists.get(blacklists.size() - 1);
        assertThat(testBlacklist.getBlacklist_id()).isEqualTo(UPDATED_BLACKLIST_ID);
        assertThat(testBlacklist.getPartial_qualified_name()).isEqualTo(UPDATED_PARTIAL_QUALIFIED_NAME);
    }

    @Test
    @Transactional
    public void deleteBlacklist() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

		int databaseSizeBeforeDelete = blacklistRepository.findAll().size();

        // Get the blacklist
        restBlacklistMockMvc.perform(delete("/api/blacklists/{id}", blacklist.getBlacklist_id())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Blacklist> blacklists = blacklistRepository.findAll();
        assertThat(blacklists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
