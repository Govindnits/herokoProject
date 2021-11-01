package ppmp.ppmp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ppmp.ppmp.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	Iterable<Project> findAllById(Iterable<Long> ids);

	Project findByProjectIdentifier(String projectId);
	
	Iterable<Project> findAll();
	
	Iterable<Project> findAllByProjectLeader(String username);

}
