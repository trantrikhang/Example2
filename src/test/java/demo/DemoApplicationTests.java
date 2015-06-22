package demo;

import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import demo.repository.EmployeeRepository;
import demo.repository.ProjectRepository;
import demo.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class DemoApplicationTests {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Test
	public void contextLoads() {
		/*Employee manager = new Employee();
		manager.setName("ABCDEF");
		manager.setEmployee_id("e01");
		employeeRepository.save(manager);

		Project project = new Project();
		project.setName("project ABC");
		project.setManager_id(manager);
		projectRepository.save(project);

		for (int j=0;j<3;j++){
			Task task = new Task();
			task.setName("task "+j);
			task.setProject(project);
			taskRepository.save(task);

			for(int k=0;k<5;k++){
				Task task_child=new Task();
				task_child.setName("task child "+k);
				task_child.setProject(project);
				task_child.setTaskParent(task);
				taskRepository.save(task_child);
			}
		}*/
	}

}
