package ch.clx.aclcrawler.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.clx.aclcrawler.domain.Users;
import ch.clx.aclcrawler.repository.UsersRepository;
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
 * REST controller for managing Users.
 */
@RestController
@RequestMapping("/api")
public class UsersResource {

    private final Logger log = LoggerFactory.getLogger(UsersResource.class);

    @Inject
    private UsersRepository usersRepository;

    /**
     * POST  /userss -> Create a new users.
     */
    @RequestMapping(value = "/userss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Users> createUsers(@Valid @RequestBody Users users) throws URISyntaxException {
        log.debug("REST request to save Users : {}", users);
        if (users.getUser_id() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new users cannot already have an ID").body(null);
        }
        Users result = usersRepository.save(users);
        return ResponseEntity.created(new URI("/api/userss/" + result.getUser_id()))
            .headers(HeaderUtil.createEntityCreationAlert("users", result.getUser_id().toString()))
            .body(result);
    }

    /**
     * PUT  /userss -> Updates an existing users.
     */
    @RequestMapping(value = "/userss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Users> updateUsers(@Valid @RequestBody Users users) throws URISyntaxException {
        log.debug("REST request to update Users : {}", users);
        if (users.getUser_id() == null) {
            return createUsers(users);
        }
        Users result = usersRepository.save(users);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("users", users.getUser_id().toString()))
            .body(result);
    }

    /**
     * GET  /userss -> get all the userss.
     */
    @RequestMapping(value = "/userss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Users>> getAllUserss(Pageable pageable)
        throws URISyntaxException {
        Page<Users> page = usersRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userss/:id -> get the "id" users.
     */
    @RequestMapping(value = "/userss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Users> getUsers(@PathVariable Long id) {
        log.debug("REST request to get Users : {}", id);
        return Optional.ofNullable(usersRepository.findOne(id))
            .map(users -> new ResponseEntity<>(
                users,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userss/:id -> delete the "id" users.
     */
    @RequestMapping(value = "/userss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUsers(@PathVariable Long id) {
        log.debug("REST request to delete Users : {}", id);
        usersRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("users", id.toString())).build();
    }
}
