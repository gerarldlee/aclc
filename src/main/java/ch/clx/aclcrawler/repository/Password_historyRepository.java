package ch.clx.aclcrawler.repository;

import ch.clx.aclcrawler.domain.Password_history;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Password_history entity.
 */
public interface Password_historyRepository extends JpaRepository<Password_history,Long> {

}
