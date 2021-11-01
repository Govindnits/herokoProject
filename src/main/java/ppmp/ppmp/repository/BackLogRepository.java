package ppmp.ppmp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ppmp.ppmp.domain.Backlog;

@Repository
public interface BackLogRepository extends CrudRepository<Backlog, Long>{
	Backlog findByProjectIdentifier(String identifier);
}
