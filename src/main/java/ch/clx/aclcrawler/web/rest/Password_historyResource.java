package ch.clx.aclcrawler.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.clx.aclcrawler.domain.Password_history;
import ch.clx.aclcrawler.repository.Password_historyRepository;
import ch.clx.aclcrawler.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Password_history.
 */
@RestController
@RequestMapping("/api")
public class Password_historyResource {

    private final Logger log = LoggerFactory.getLogger(Password_historyResource.class);

    @Inject
    private Password_historyRepository password_historyRepository;

    /**
     * POST  /password_historys -> Create a new password_history.
     */
    @RequestMapping(value = "/password_historys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Password_history> createPassword_history(@Valid @RequestBody Password_history password_history) throws URISyntaxException {
        log.debug("REST request to save Password_history : {}", password_history);
        if (password_history.getPassword_history_id() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new password_history cannot already have an ID").body(null);
        }
        Password_history result = password_historyRepository.save(password_history);
        return ResponseEntity.created(new URI("/api/password_historys/" + result.getPassword_history_id()))
            .headers(HeaderUtil.createEntityCreationAlert("password_history", result.getPassword_history_id().toString()))
            .body(result);
    }

    /**
     * PUT  /password_historys -> Updates an existing password_history.
     */
    @RequestMapping(value = "/password_historys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Password_history> updatePassword_history(@Valid @RequestBody Password_history password_history) throws URISyntaxException {
        log.debug("REST request to update Password_history : {}", password_history);
        if (password_history.getPassword_history_id() == null) {
            return createPassword_history(password_history);
        }
        Password_history result = password_historyRepository.save(password_history);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("password_history", password_history.getPassword_history_id().toString()))
            .body(result);
    }

    /**
     * GET  /password_historys -> get all the password_historys.
     */
    @RequestMapping(value = "/password_historys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Password_history> getAllPassword_historys() {
        log.debug("REST request to get all Password_historys");
        return password_historyRepository.findAll();
    }

    /**
     * GET  /password_historys/:id -> get the "id" password_history.
     */
    @RequestMapping(value = "/password_historys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Password_history> getPassword_history(@PathVariable Long id) {
        log.debug("REST request to get Password_history : {}", id);
        return Optional.ofNullable(password_historyRepository.findOne(id))
            .map(password_history -> new ResponseEntity<>(
                password_history,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /password_historys/:id -> delete the "id" password_history.
     */
    @RequestMapping(value = "/password_historys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePassword_history(@PathVariable Long id) {
        log.debug("REST request to delete Password_history : {}", id);
        password_historyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("password_history", id.toString())).build();
    }
}
