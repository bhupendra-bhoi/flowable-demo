package com.flowable.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowable.service.EmailService;

@RestController
public class SchedulerApi {
	@GetMapping("start")
	public String startScheduler() throws Exception{
		sratringScheduler();
		return "Started";
	}
	
	private static final Logger log = LoggerFactory.getLogger(SchedulerApi.class);
	
	private static final String CUSTOMER_ID_PV = "customerId";
	private static final String EMAIL_PV = "email";

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private EmailService emailService;
	
	void sratringScheduler() throws Exception {
		String customerId = "1";
		String email = "bhupendra.bhoi@gmail.com";
		String processInstanceId = this.beginCustomerEnrollment(customerId, email);
		System.out.println(("process instance ID: " + processInstanceId));
		log.info(("process instance ID: %s" + processInstanceId));
		//Assert.assertNotNull("the processInstanceId should not be null", processInstanceId);
		
		//Thread.sleep(1000);
		/* this.confirmEmail(customerId); */
		//
		List<Task> tasks = this.taskService
		.createTaskQuery()
		.taskName("confirm-email-task")
		.includeProcessVariables()
		.processVariableValueEquals(CUSTOMER_ID_PV, customerId)
		.list();
		
		//Assert.assertTrue("there should be one outstanding", tasks.size() >= 1);
		log.info("there should be one outstanding: %s", tasks.size() >= 1);
		
		// complete outstanding task
		
		tasks.forEach(task -> {
			this.taskService.claim(task.getId(), "bbhoi");
			this.taskService.complete(task.getId());
			}
		);
			
		// confirm that the email has been sent
		
		//Assert.assertEquals(this.emailService.sends.get(email).get(), 1);
		log.info(this.emailService.sends.get(email).get()+ "1");
	}
	
	String beginCustomerEnrollment (String customerId, String email) {
		Map<String, Object> vars = new HashMap<>();
		vars.put(CUSTOMER_ID_PV, customerId);
		vars.put(EMAIL_PV, email);
		ProcessInstance processInstance = this.runtimeService
				.startProcessInstanceByKey("signup-process", vars);
		return processInstance.getId(); //todo
	}
	
	void confirmEmail(String customerId) {
		System.out.println();
	}
	
}
