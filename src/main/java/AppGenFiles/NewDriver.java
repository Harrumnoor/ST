package AppGenFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.vaadin.server.VaadinSession;
import Activiti.OrderedBusinessFlow;
import GUI.WebPageGen;
import my.vaadin.FirstActivitiGUI.EndView;

public class NewDriver 
{
	//Remember all java class attributes should also be present in BPMN 
	public static List <UserTask> userTaskList=new ArrayList<UserTask>();//for all user tasks
	public static List<EmailServiceTask> emailTaskList=new ArrayList<EmailServiceTask>(); //for all email tasks
	

	
	public static void startUp() 
	{
		
		///////////////////////////////// FOR FIRST TABLE ////////////////////////////////////////
		XMLParser parserObj=new XMLParser();
		HashMap<String, String> candidateGroups=new HashMap<String, String>(); // for unique candidates only
		//creating TASKS Table
		List<TaskInfo>userTaskTable =parserObj.extractTasks("userTask");
		
		List<TaskInfo> serviceTaskTable=parserObj.extractTasks("serviceTask");
		
		List<TaskInfo> exclusiveGatewaysTable=parserObj.extractTasks("exclusiveGateway");
		List<TaskInfo> parallelGatewaysTable=parserObj.extractTasks("parallelGateway");
		
		List<TaskInfo> allTasks=new ArrayList<TaskInfo>();
		allTasks.addAll(userTaskTable);
		allTasks.addAll(serviceTaskTable);
		allTasks.addAll(exclusiveGatewaysTable);
		allTasks.addAll(parallelGatewaysTable);
	
		for(TaskInfo task:allTasks)
		{		
			candidateGroups.put(task.taskId,task.taskAssignee);
		}
		
		System.out.println("----------------- Printing all tasks table ---------------------");
		for(TaskInfo task:allTasks)
		{		
			System.out.println("Task id: "+task.taskId);
			System.out.println("Task name: "+task.taskName);
			System.out.println("Task assignee: "+task.taskAssignee);
			System.out.println("Task type: "+task.taskType);
			System.out.println();
		}
		
		
		////////////////////// FOR SECOND TABLE ///////////////////////////////////////////
		
		//creating sequence flow table
		List<SequenceFlowInfo> sequenceFlowList = new ArrayList<SequenceFlowInfo>();
		sequenceFlowList=parserObj.extractSequenceFlow("sequenceFlow");
		
		System.out.println();
		System.out.println();
		System.out.println("----------------- Printing sequence flow table ---------------------");
		for(SequenceFlowInfo task:sequenceFlowList)
		{		
			System.out.println("Source: "+task.source);
			System.out.println("Destination: "+task.destination);
			System.out.println("Gateway Value: "+task.gatewayValue);
			System.out.println();
		}
		
		//Getting form properties of all User Tasks only 
		for (TaskInfo t: userTaskTable)
		{
			if (t.taskType.equals("userTask"))
			{
				UserTask userTaskObj=parserObj.getFormProperties("userTask", t.taskId);
				userTaskList.add(userTaskObj); // all user tasks with properties
			}	
			else
			{
				
			}
		}
		
		//Getting form properties of all Email Service Tasks only 
		
		
		emailTaskList=parserObj.getEmailProperties(0);
			
		
		
		List<OrderedBusinessFlow> orderedFlowList=new ArrayList<OrderedBusinessFlow>();
		orderedFlowList=parserObj.orderSequenceFlow(sequenceFlowList, allTasks);
		WebPageGen w=new WebPageGen();
		WebPageGen.userTaskList=userTaskList; //for all user tasks
		WebPageGen.emailTaskList=emailTaskList; //for all email task
		WebPageGen.allCandidates=candidateGroups; // for unique candidates only
		WebPageGen.userTaskTable=userTaskTable; //for TASKS Table
		WebPageGen.serviceTaskTable=serviceTaskTable;
		WebPageGen.exclusiveGatewaysTable=exclusiveGatewaysTable;
		WebPageGen.parallelGatewaysTable=parallelGatewaysTable;	
		WebPageGen.allTasks=allTasks;
		WebPageGen.sequenceFlowList=sequenceFlowList;
		WebPageGen.orderedFlowList=orderedFlowList;
		w.setParallelStatus();
		//String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
		String currentAssignee="Customer";
		List<String> fileContent=w.getUpdatedTaskStatus2(0);
		if (fileContent.size()>0)
		{
			String taskId=fileContent.get(fileContent.size()-1);
			System.out.println("Task ID is: "+taskId);
			w.getNextDestination(taskId);
		}
		
		else
		{
			String assignee="";
			String firstTask="";
			for(OrderedBusinessFlow task:orderedFlowList)
			{		
				if (task.source.contains("startevent"))
				{
					firstTask=task.destination;
					OrderedBusinessFlow myTask=w.findTaskbySource(firstTask);
					assignee=myTask.assignee;
				}
			}
			
			/*if (assignee.equals(currentAssignee))
			{
				//w.GenerateWebPage(firstTask);
			}
			
			else
			{
				new EndView();
			}*/
			
		}
		
		
		
		
		
		
	}

}
