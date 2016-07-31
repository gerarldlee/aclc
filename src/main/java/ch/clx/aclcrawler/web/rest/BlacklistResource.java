package ch.clx.aclcrawler.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.clx.aclcrawler.domain.Blacklist;
import ch.clx.aclcrawler.repository.BlacklistRepository;
import ch.clx.aclcrawler.web.rest.util.HeaderUtil;
import ch.clx.aclcrawler.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Blacklist.
 */
@RestController
@RequestMapping("/api")
public class BlacklistResource {

    private final Logger log = LoggerFactory.getLogger(BlacklistResource.class);

    @Inject
    private BlacklistRepository blacklistRepository;

    /**
     * POST  /blacklists -> Create a new blacklist.
     */
    @RequestMapping(value = "/blacklists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Blacklist> createBlacklist(@Valid @RequestBody Blacklist blacklist) throws URISyntaxException {
        log.debug("REST request to save Blacklist : {}", blacklist);
        if (blacklist.getBlacklist_id() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new blacklist cannot already have an ID").body(null);
        }
        Blacklist result = blacklistRepository.save(blacklist);
        return ResponseEntity.created(new URI("/api/blacklists/" + result.getBlacklist_id()))
            .headers(HeaderUtil.createEntityCreationAlert("blacklist", result.getBlacklist_id().toString()))
            .body(result);
    }

    /**
     * PUT  /blacklists -> Updates an existing blacklist.
     */
    @RequestMapping(value = "/blacklists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Blacklist> updateBlacklist(@Valid @RequestBody Blacklist blacklist) throws URISyntaxException {
        log.debug("REST request to update Blacklist : {}", blacklist);
        if (blacklist.getBlacklist_id() == null) {
            return createBlacklist(blacklist);
        }
        Blacklist result = blacklistRepository.save(blacklist);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("blacklist", blacklist.getBlacklist_id().toString()))
            .body(result);
    }

    /**
     * GET  /blacklists -> get all the blacklists.
     */
    @RequestMapping(value = "/blacklists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Blacklist>> getAllBlacklists(Pageable pageable)
        throws URISyntaxException {
        Page<Blacklist> page = blacklistRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blacklists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /blacklists/:id -> get the "id" blacklist.
     */
    @RequestMapping(value = "/blacklists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Blacklist> getBlacklist(@PathVariable Long id) {
        log.debug("REST request to get Blacklist : {}", id);
        return Optional.ofNullable(blacklistRepository.findOne(id))
            .map(blacklist -> new ResponseEntity<>(
                blacklist,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /blacklists/:id -> delete the "id" blacklist.
     */
    @RequestMapping(value = "/blacklists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBlacklist(@PathVariable Long id) {
        log.debug("REST request to delete Blacklist : {}", id);
        blacklistRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("blacklist", id.toString())).build();
    }
}
