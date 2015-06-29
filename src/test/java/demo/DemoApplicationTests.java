package demo;

import demo.model.Company;
import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import demo.repository.CompanyRepository;
import demo.repository.EmployeeRepository;
import demo.repository.ProjectRepository;
import demo.repository.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class DemoApplicationTests {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Test
	public void contextLoads() {

		for(int i=0;i<3;i++){
			Task taskParent = new Task();
			taskParent.setTaskId("TC0"+i);
			taskParent.setTaskName("Task child name "+i);
			taskParent.setProjectId("P00");
			taskParent.setTaskParentId("TP00");
			taskRepository.save(taskParent);
		}

		for(int i=3;i<6;i++){
			Task taskParent = new Task();
			taskParent.setTaskId("TC0"+i);
			taskParent.setTaskName("Task child name "+i);
			taskParent.setProjectId("P00");
			taskParent.setTaskParentId("TP01");
			taskRepository.save(taskParent);
		}

		for(int i=6;i<9;i++){
			Task taskParent = new Task();
			taskParent.setTaskId("TC0"+i);
			taskParent.setTaskName("Task child name "+i);
			taskParent.setProjectId("P00");
			taskParent.setTaskParentId("TP02");
			taskRepository.save(taskParent);
		}

		for(int i=9;i<12;i++){
			Task taskParent = new Task();
			taskParent.setTaskId("TC0"+i);
			taskParent.setTaskName("Task child name "+i);
			taskParent.setProjectId("P01");
			taskParent.setTaskParentId("TP03");
			taskRepository.save(taskParent);
		}

		for(int i=3;i<6;i++){
			Task taskParent = new Task();
			taskParent.setTaskId("TC0"+i);
			taskParent.setTaskName("Task child name "+i);
			taskParent.setProjectId("P01");
			taskParent.setTaskParentId("TP04");
			taskRepository.save(taskParent);
		}

		for(int i=6;i<9;i++){
			Task taskParent = new Task();
			taskParent.setTaskId("TC0"+i);
			taskParent.setTaskName("Task child name "+i);
			taskParent.setProjectId("P01");
			taskParent.setTaskParentId("TP05");
			taskRepository.save(taskParent);
		}

	}

}
