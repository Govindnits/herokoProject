package ppmp.ppmp.exceptions;

public class ProjectNotFoundExceptionResponse {
	private String projectNotFound;

	public ProjectNotFoundExceptionResponse(String projectNotFound) {
		this.setProjectNotFound(projectNotFound);
	}

	public String getProjectNotFound() {
		return projectNotFound;
	}

	public void setProjectNotFound(String projectNotFound) {
		this.projectNotFound = projectNotFound;
	}
	

}
