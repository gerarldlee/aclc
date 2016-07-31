package ch.clx.aclcrawler.repository;

import ch.clx.aclcrawler.domain.Users;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Users entity.
 */
public interface UsersRepository extends JpaRepository<Users,Long> {

}
