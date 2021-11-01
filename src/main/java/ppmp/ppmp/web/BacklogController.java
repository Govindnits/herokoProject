package ppmp.ppmp.web;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ppmp.ppmp.domain.ProjectTask;
import ppmp.ppmp.services.MapValidationErrorService;
import ppmp.ppmp.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

	@Autowired
	private ProjectTaskService projectTaskService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@RequestMapping("/{backlog_id}")
	public ResponseEntity<?> addPtToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable String backlog_id,Principal principal) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
		if (errorMap != null) {
			return errorMap;
		}
		ProjectTask projectTaskRes = projectTaskService.addProjectTask(backlog_id, projectTask,principal.getName());
		return new ResponseEntity<ProjectTask>(projectTaskRes, HttpStatus.CREATED);
	}

	@GetMapping("/{backlog_id}")
	public Iterable<ProjectTask> getProjectTaskBacklog(@PathVariable String backlog_id,Principal principal) {
		return projectTaskService.getProjectTaskByBacklog(backlog_id,principal.getName());
	}

	@GetMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id) {
		ProjectTask projectTask = projectTaskService.findPTbyProjectSequence(backlog_id, pt_id);
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
	}

	@PatchMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?> updatePtToBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable String backlog_id, @PathVariable String pt_id) {
		ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
		if (errorMap != null) {
			return errorMap;
		}
		ProjectTask projectTaskRes = projectTaskService.updateByProjectSequence(projectTask, backlog_id, pt_id);

		return new ResponseEntity<ProjectTask>(projectTaskRes, HttpStatus.OK);

	}
	@DeleteMapping("/{backlog_id}/{pt_id}")
	public ResponseEntity<?>  deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id) {
		projectTaskService.deleteProjectTaskBySequenceId(backlog_id,pt_id);
		return new ResponseEntity<String>("Project Task "+ pt_id + " got deleted successfully",HttpStatus.OK);
	}

}
