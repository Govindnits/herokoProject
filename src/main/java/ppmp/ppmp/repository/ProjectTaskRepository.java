package ppmp.ppmp.repository;

import org.springframework.data.repository.CrudRepository;

import ppmp.ppmp.domain.ProjectTask;

public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
	Iterable<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

	ProjectTask findByProjectSequence(String sequence);
}
