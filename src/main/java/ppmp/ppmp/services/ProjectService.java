package ppmp.ppmp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ppmp.ppmp.domain.Backlog;
import ppmp.ppmp.domain.Project;
import ppmp.ppmp.domain.User;
import ppmp.ppmp.exceptions.ProjectIdException;
import ppmp.ppmp.exceptions.ProjectNotFoundException;
import ppmp.ppmp.repository.ProjectRepository;
import ppmp.ppmp.repository.UserRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	public Project saveOrUpdateProject(Project project, String username) {
		Project existingProject = null;
		if (project.getId() != null) {
			existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
			if (existingProject == null) {
				throw new ProjectNotFoundException("Project with ID: " + project.getProjectIdentifier()
						+ " can not be updated as it does not exist.");
			}
			if (existingProject != null && !existingProject.getProjectLeader().equals(username)) {
				throw new ProjectNotFoundException("Project not found in your account.");
			}
		}
		try {
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(username);
			String identifier = project.getProjectIdentifier().toUpperCase();
			project.setProjectIdentifier(identifier);
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(identifier);
			}
			if (existingProject != null) {
				project.setBacklog(existingProject.getBacklog());
			}
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project ID " + project.getProjectIdentifier() + " already exist.");
		}

	}

	public Project findProjectByIdentifier(String projectIdentifier, String username) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project ID " + projectIdentifier + " does not exist.");
		}
		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("project not found in your account.");
		}
		return project;
	}

	public Iterable<Project> findAllProjects(String username) {
		return projectRepository.findAllByProjectLeader(username);
	}

	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId);
		if (project == null) {
			throw new ProjectIdException("Project does not exist.");
		}
		projectRepository.delete(project);
	}
}
