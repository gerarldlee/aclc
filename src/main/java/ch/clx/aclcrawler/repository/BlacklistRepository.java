package ch.clx.aclcrawler.repository;

import ch.clx.aclcrawler.domain.Blacklist;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Blacklist entity.
 */
public interface BlacklistRepository extends JpaRepository<Blacklist,Long> {

}
