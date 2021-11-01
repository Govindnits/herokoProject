package ppmp.ppmp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ppmp.ppmp.domain.Backlog;
import ppmp.ppmp.domain.Project;
import ppmp.ppmp.domain.ProjectTask;
import ppmp.ppmp.exceptions.ProjectNotFoundException;
import ppmp.ppmp.repository.BackLogRepository;
import ppmp.ppmp.repository.ProjectRepository;
import ppmp.ppmp.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private BackLogRepository backLogRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		try {
			// Exceptions: Project not found
			// Pts should be used to a specific project , project!=null, Bl exist.
			Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

			projectTask.setBacklog(backlog);
			Integer backLogSequence = backlog.getPTSequence();
			backLogSequence++;
			backlog.setPTSequence(backLogSequence);
			projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backLogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
				projectTask.setPriority(3);
			}
			// Initial status when it is null.
			if (projectTask.getStatus() == null || projectTask.getStatus().isEmpty()) {
				projectTask.setStatus("TO_DO");
			}
			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project Not found.");
		}
	}

	public Iterable<ProjectTask> getProjectTaskByBacklog(String backlog_id,String username) {
		projectService.findProjectByIdentifier(backlog_id, username);
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
	}

	public ProjectTask findPTbyProjectSequence(String backlog_id, String sequence) {
		Backlog backlog = backLogRepository.findByProjectIdentifier(backlog_id);
		if (backlog == null) {
			throw new ProjectNotFoundException("Project ID " + backlog_id + " does not exist.");
		}
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task " + sequence + " does not exist.");
		}
		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException(
					"Project Task " + sequence + " does not exist in Project:  " + backlog_id);
		}
		return projectTask;
	}

	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {
		ProjectTask projectTask = findPTbyProjectSequence(backlog_id, pt_id);
		projectTask = updatedTask;
		return projectTaskRepository.save(projectTask);
	}

	public void deleteProjectTaskBySequenceId(String backlog_id, String pt_id) {
		ProjectTask projectTask = findPTbyProjectSequence(backlog_id, pt_id);
		projectTaskRepository.delete(projectTask);
	}
}
