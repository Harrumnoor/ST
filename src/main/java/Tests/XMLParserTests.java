package Tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Activiti.OrderedBusinessFlow;
import AppGenFiles.EmailServiceTask;
import AppGenFiles.SequenceFlowInfo;
import AppGenFiles.TaskInfo;
import AppGenFiles.UserTask;
import AppGenFiles.XMLParser;
import GUI.WebPageGen;

public class XMLParserTests {

	@Test
	public void isPresentTest() {
		List<OrderedBusinessFlow> orderedList= new ArrayList<OrderedBusinessFlow>();
		OrderedBusinessFlow flowObj = new OrderedBusinessFlow();
		flowObj.assignee="Customer";
		flowObj.destination="GetIncomeDetails";
		flowObj.source="SubmitApplication";
		orderedList.add(flowObj);
		boolean output = new XMLParser().isPresent(orderedList, flowObj);
		assertEquals(true, output);
		OrderedBusinessFlow flowObj1 = new OrderedBusinessFlow();
		flowObj1.assignee="Customer";
		flowObj1.destination="GetIncomeDe";
		flowObj1.source="SubmitApplication";
		output = new XMLParser().isPresent(orderedList, flowObj1);
		assertEquals(false, output);

	}
	@Test
	public void getExclusivePathsTest()
	{
		List<TaskInfo> TaskList= new ArrayList<TaskInfo>();
		List<OrderedBusinessFlow> orderedList= new ArrayList<OrderedBusinessFlow>();
		List<SequenceFlowInfo> seqFlowList =new ArrayList<SequenceFlowInfo>();
		try
		{
		TaskInfo t = new TaskInfo();
		t.taskAssignee="Manager";
		t.taskId="SendAcceptanceEmail";
		t.taskName="Send Acceptance Email";
		t.taskJavaFile="AppGenFiles.EmailService";
		TaskList.add(t);
		OrderedBusinessFlow flowObj = new OrderedBusinessFlow();
		flowObj.assignee="Customer";
		flowObj.destination="GetIncomeDetails";
		flowObj.source="SubmitApplication";
		orderedList.add(flowObj);
		SequenceFlowInfo sq = new SequenceFlowInfo();
		sq.destination="GetIncomeDetails";
		sq.source="SubmitApplication";
		seqFlowList.add(sq);
		new XMLParser().getExclusivePaths(TaskList,orderedList,seqFlowList,"SubmitApplication",0);
		new XMLParser().getExclusivePaths(TaskList,orderedList,seqFlowList,null,0);
		new XMLParser().getExclusivePaths(TaskList,orderedList,seqFlowList,"SubmitApplication",1);


		}
		catch(Exception e )
		{
			fail("Test Failed");
		}

	}
	@Test 
	public void SequenceFlowTest()
	{
		List<SequenceFlowInfo> sequenceFlowList = new ArrayList<SequenceFlowInfo>();
		sequenceFlowList=new XMLParser().extractSequenceFlow("sequenceFlow");
		List<TaskInfo> taskList = new ArrayList<TaskInfo>();
		XMLParser parser = new XMLParser();
		taskList = new WebPageGen().allTasks;
		List<OrderedBusinessFlow> obf = new ArrayList<OrderedBusinessFlow>();
		obf=parser.orderSequenceFlow(sequenceFlowList, taskList);
		if(obf.size()<=0)
			fail("Test Has Failed");

	}
	
	@Test 
	public void getAssigneeTest ()
	{
		List<TaskInfo> taskList = new ArrayList<TaskInfo>();
		TaskInfo t = new TaskInfo();
		
		XMLParser parser = new XMLParser();
		taskList = new WebPageGen().allTasks;
		String ID="HI";
		String output=new XMLParser().getAssignee (ID, taskList);
		System.out.println("output is = "+ output);
		if(!output.equals("Not found"))
			fail("Test Has Failed");
		ID="GetIncomeDetails";
		output=new XMLParser().getAssignee (ID, taskList);
		if(output.equals("Not Found"))
			fail("Test Has Failed");
		ID="parallelgateway";
		 taskList = new ArrayList<TaskInfo>();
		 t.taskId="parallelgateway";
		 taskList.add(t);
		output=new XMLParser().getAssignee (ID, taskList);
		System.out.println("output is = "+ output);
		if(!output.equals("None"))
			fail("Test Has Failed");
		ID="exclusivegateway";
	    taskList = new ArrayList<TaskInfo>();
	    t.taskId="exclusivegateway";
	    taskList.add(t);
		output=new XMLParser().getAssignee (ID, taskList);
		System.out.println("output is = "+ output);
		if(!output.equals("None"))
			fail("Test Has Failed");
		String newID = "SendAcceptanceEmail";
		List<TaskInfo> taskList1 = new ArrayList<TaskInfo>();
		t.taskId="SendAcceptanceEmail";
		t.taskAssignee="Manager";
		taskList1.add(t);
		output=new XMLParser().getAssignee (newID, taskList1);
		System.out.println("output is = "+ output);
		if(output.equals("None")||output.equals("Not found"))
			fail("Test Has Failed");
		}
	
	@Test
	public void getEmailPropertiesTest()
	{
		List<EmailServiceTask> emailTaskList=new ArrayList<EmailServiceTask>();
		
		emailTaskList = new XMLParser(). getEmailProperties(0);
		List<EmailServiceTask>outputTaskList = new ArrayList<EmailServiceTask>();
		EmailServiceTask t = new EmailServiceTask();
		t.taskId="SendAcceptanceEmail";
		t.assignee="Manager";
		t.taskName="Send Acceptance Email";
		outputTaskList.add(t);
		t.taskId="SendConfirmationLetter";
		t.assignee="Manager";
		t.taskName="Send Loan Confirmation Letter";
		outputTaskList.add(t);
		t.taskId="OpenNewAccount";
		t.assignee="Manager";
		t.taskName="Request to Open New Account";
		outputTaskList.add(t);
		t.taskId="SendRejectionEmail";
		t.assignee="Manager";
		t.taskName="Send Rejection Email";
		outputTaskList.add(t);
		assertEquals(emailTaskList.size(),outputTaskList.size());
		XMLParser xml = new XMLParser();
		xml.fileName="hello";
		xml.getEmailProperties(0);
		new XMLParser().getEmailProperties(1);

	
	}
	
	@Test
	public void getFormPropertiesTest()
	{
		UserTask userTaskObj=new UserTask();
		userTaskObj=new XMLParser().getFormProperties("userTask","SubmitApplication");
		assertEquals(userTaskObj.assignee,"Customer");
		assertEquals(userTaskObj.name,"Submit Application");
		XMLParser xml = new XMLParser();
		xml.fileName="hello";
		userTaskObj=xml.getFormProperties("userTask","SubmitApplication");

	}
	
	@Test
	public void extractTasksTest()
	{
		List<TaskInfo> taskList = new ArrayList<TaskInfo>();

		taskList= new XMLParser().extractTasks("userTask");
		assertEquals(taskList.size(),19);
		taskList= new XMLParser().extractTasks("serviceTask");
		assertEquals(taskList.size(),4);
		taskList= new XMLParser().extractTasks("parallelgateway");
		assertEquals(taskList.size(),0);
		XMLParser xml = new XMLParser();
		xml.fileName="hello";
		taskList= xml.extractTasks("parallelgateway");
	}
	
	@Test
	public void extractSequenceFlowTest()
	{
		List<SequenceFlowInfo> SequenceFlowInfoList=new ArrayList<SequenceFlowInfo>();
		SequenceFlowInfoList=new XMLParser().extractSequenceFlow("userTask");
		assertEquals(SequenceFlowInfoList.size(),19);
		SequenceFlowInfoList=new XMLParser().extractSequenceFlow("serviceTask");
		assertEquals(SequenceFlowInfoList.size(),4);
		SequenceFlowInfoList= new XMLParser().extractSequenceFlow("parallelGateway");
		assertEquals(SequenceFlowInfoList.size(),6);
		XMLParser xml = new XMLParser();
		xml.fileName="hello";
		SequenceFlowInfoList= xml.extractSequenceFlow("arallelgateway");		
	}


}
