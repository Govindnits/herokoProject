package ppmp.ppmp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ppmp.ppmp.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);

	User getById(long id);

}
