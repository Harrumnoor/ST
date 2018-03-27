package GUI;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.vaadin.server.VaadinSession;
import Activiti.OrderedBusinessFlow;
import AppGenFiles.EmailServiceTask;
import AppGenFiles.SequenceFlowInfo;
import AppGenFiles.TaskInfo;
import AppGenFiles.UserTask;
import AppGenFiles.XMLParser;
import my.vaadin.FirstActivitiGUI.EndView;
import my.vaadin.FirstActivitiGUI.FormProperties;

public class WebPageGen 
{
	public static List <UserTask> userTaskList=new ArrayList<UserTask>();//for all user tasks
	public static List<EmailServiceTask> emailTaskList=new ArrayList<EmailServiceTask>(); //for all email tasks
	public static HashMap<String, String> allCandidates=new HashMap<String, String>(); // for unique candidates only
	public static List<TaskInfo>userTaskTable =new ArrayList<TaskInfo>(); //for TASKS Table
	public static List<TaskInfo> serviceTaskTable=new ArrayList<TaskInfo>();
	public static List<TaskInfo> exclusiveGatewaysTable=new ArrayList<TaskInfo>();	
	public static List<TaskInfo> parallelGatewaysTable=new ArrayList<TaskInfo>();	
	public static List<TaskInfo> allTasks=new ArrayList<TaskInfo>();
	public static List<SequenceFlowInfo> sequenceFlowList = new ArrayList<SequenceFlowInfo>();
	public static List<OrderedBusinessFlow> orderedFlowList=new ArrayList<OrderedBusinessFlow>();
	public static boolean execution=false;
	public static int count=0;
	public static int assigneeSet=0;
	public static String originalAssignee="";
	public static int taskNo=0;
	public String toBeDone="";//="null"; //to store task that needs to be dispalyed on web page
	public static int parallelCounter=0;//keeps count of pararelll
	public static int startPairCounter=0;
	public static int endPairCounter=0;
	public static int originalChildCounter=0;
	public static HashMap<String, Boolean> parallelStatus=new HashMap<String, Boolean>(); // for unique candidates only
	//public String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
	
	public void setParallelStatus()
	{
		for (TaskInfo task:parallelGatewaysTable)
		{
			parallelStatus.put(task.taskId, false);
		}
	}
	
	
	public String selectedExclusivePath (UserTask usertask, HashMap<String, String> enumIds_input,int debugflag)
	{
		
		 HashMap<String, String> enumVariables_input=new HashMap<String, String>();
	
		 for (FormProperties f: usertask.formPropertiesList)
       	 {
        	 if (f.enumFields!=null)
       		 {        			 
        		 for (String name: enumIds_input.keySet())
    			 { 
    				 String key =name.toString(); //enum id
        	         String value = enumIds_input.get(name).toString(); //input radio button id
        	         //System.out.println("f.enumFields.id"+f.enumFields.id);
        	         if (key.equals(f.enumFields.id))
        	         {
        	        	 //System.out.println("enum variableee: "+f.enumFields.enumVariable+" input valuee: "+value);
	        	         enumVariables_input.put(f.enumFields.enumVariable, value);
        	         }
    			 } 
       		 }
        	 
        	 else
        	 {
        		 return "Not Found";
        	 }
         }
 	     
		 String userTaskID="";
		 String selectedInput="";
		 for (String name: enumVariables_input.keySet())
		 { 
		
			 String key =name.toString(); //enum variable
	         String value = enumVariables_input.get(name).toString(); //input radio button id
	         if(debugflag==1)
	         {
	        	 key=" ";
	         }
	         if (!key.equals(" "))
	         {
	        	 userTaskID=getUserIdFromEnumVariable(key);
	        	 selectedInput=value;
	         }
	         System.out.println("Key is: "+ key + " " + "Values is: "+ value);
		 } 
		 
		 String newDestination=getGatewayOut(findNextDestination(userTaskID), selectedInput);
		 return newDestination;
	}
	

	
	
	
	public int findNumberOfBranches (OrderedBusinessFlow flowObj)
	{
		int branch=0;
		for (OrderedBusinessFlow flowInner: orderedFlowList)
		{	
			if (flowInner.destination.equals(flowObj.source))
			{
				branch++;
			}							
		}
		return branch;
	}
	
	
	
	
	public OrderedBusinessFlow getOrdObject(String id)
	{
		for (OrderedBusinessFlow findObj : orderedFlowList)
		{
			
			if (findObj.source.equals(id))
			{
				System.out.println("RETURNING-------------------->"+findObj.source);
				return findObj;
				
			}
		}
		return null;
	}
	
	
	public OrderedBusinessFlow getPrevObj(OrderedBusinessFlow currentObj)
	{
		for(OrderedBusinessFlow obj: orderedFlowList)
		{
			if(obj.destination.equals(currentObj.source))
				return obj;
		}
		return null;
		
	}
	
	public List<OrderedBusinessFlow> getAllExclusiveDestinations(OrderedBusinessFlow flowObj)
	{
		List<OrderedBusinessFlow> alldest = new ArrayList<OrderedBusinessFlow>();
		for (OrderedBusinessFlow flowInner: orderedFlowList)
		{	
			if (flowInner.destination.equals(flowObj.source))
			{
				alldest.add(flowInner);
				//System.out.println("Puttingg flow inner.source ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^E"+flowInner.source);
			}
			
		}
		return alldest;
	}
	
	
	public List<OrderedBusinessFlow> getAllDestinations(OrderedBusinessFlow flowObj)
	{
		List<OrderedBusinessFlow> alldest = new ArrayList<OrderedBusinessFlow>();
		for (OrderedBusinessFlow flowInner: orderedFlowList)
		{	
			if (flowInner.source.equals(flowObj.source))
			{
				alldest.add(flowInner);
				//System.out.println("Puttingg flow inner.source ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^E"+flowInner.source);
			}							
		}
		return alldest;
	}
	
	
	
	
	public List<OrderedBusinessFlow> getAllSources(OrderedBusinessFlow flowObj)
	{
		List<OrderedBusinessFlow> allSources = new ArrayList<OrderedBusinessFlow>();
		for (OrderedBusinessFlow flowInner:new XMLParser().orderSequenceFlow(sequenceFlowList, allTasks))
		{	
			if (flowInner.destination.equals(flowObj.source))
			{
				allSources.add(flowInner);
				//System.out.println("PATH DEST IS: "+flowInner.destination+" and  FOUND SOURCE"+flowInner.source);
			}							
		}
		return allSources;
	}
	
	
	public void displaySpecific(OrderedBusinessFlow objOrdered,int debugflag)
	{
		HashMap<OrderedBusinessFlow,String> retValues=new HashMap<OrderedBusinessFlow,String>();
		List<OrderedBusinessFlow> alldest = getAllDestinations(objOrdered);
		//String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
		String currentAssignee="Customer";
		for (OrderedBusinessFlow obf:alldest)
		{
			//System.out.println("All sourceee mai multipleee taskkkkkss:-----> "+obf.destination);	
			retValues.put(findTaskbySource(obf.destination),checkIfDone(obf.destination));
		}		
		for (OrderedBusinessFlow key : retValues.keySet())
		{					
			OrderedBusinessFlow keyStr=key;
			String value=retValues.get(keyStr);
			//System.out.println("TASK FROM WHERE TO START BACK TRACK:-----> "+obf.source);
			//System.out.println(" and key assignee is "+key.assignee+" and value "+value);
			
				if(debugflag==1)
				{
					value="Not Done";
					currentAssignee="Manager";
					
				}
			if (value.equals("Not Done") && (key.assignee.equals(currentAssignee)))
			{
						OrderedBusinessFlow sourceObj = findTaskbySource(key.source);
						System.out.println("Yeh-----------> walaaa task karooo: "+key.source);
						toBeDone=key.source;
						System.out.println("callingggg forward fucntionnn lineee 199");
						//GenerateWebPage(toBeDone);
						//return;
						System.out.println("\t\t\t***********************************************************************");
				
			}
		}
	}
	
	// check if the prv obj of curent obj is done or not


	
	public String getUserIdFromEnumVariable(String enumVariable)
	{
		for (UserTask task : userTaskList)
		{
			for (FormProperties prop : task.formPropertiesList)
			{
				if (prop.enumFields!=null)
				{
					if (prop.enumFields.enumVariable.equals(enumVariable))
					{
						
						return task.id;
					}
				}
				
			}
		}
		
		return "Not Found";
	}
	
	public String getGatewayOut(String userTaskId, String selectedInput)
	{
		for (SequenceFlowInfo seq : sequenceFlowList)
		{
			if (seq.source.equals(userTaskId) && (seq.gatewayValue.equals(selectedInput)))
			{
				return seq.destination;
			}
		}
		
		return "Not Found";
	}
	

	

	
	
	public void checkAssigneeTaskDone(String taskId, int debugflag)
	{

		String fileName = "taskStatus.txt";
		if(debugflag==1)
		{
			fileName="123";
		}
        String line = null;
        try 
        {
            FileReader fileReader=new FileReader(fileName);
            BufferedReader bufferedReader=new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
            	String taskid=line.substring(0, line.indexOf(" "));
            	String status=line.substring(line.indexOf(" "), line.length()-1);
                System.out.println("&&&&&&&&&&&&  SSTATUS IS  '" + status+ "'");                

            	boolean status_boolean=false;
            	if(debugflag==2)
            	{
            		status="123";
            	}
            	if (status.equals(" Don"))
            	status_boolean=true;
            	
            	for (TaskInfo task : allTasks)
            		
            	{
            		if(debugflag==3)
            		{
            			taskid="123";
            			task.Done=true;
            			//System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Status boolean is = "+status_boolean);
            		}
            		if (task.taskId.equals(taskid) && task.Done==status_boolean)
            		{
            			count++;
            		}
            	}
                
            }   

            bufferedReader.close();         
        }
        
        catch(Exception ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
       
		
	}
	

	
	public List<String> getUpdatedTaskStatus2(int debugflag) 
	{
		
		List<String> fileContent=new ArrayList<String>();
        String fileName = "taskStatus.txt";
        if(debugflag==2)
		{
			fileName="123";
		}
        String line = null;
        try 
        {
            FileReader fileReader=new FileReader(fileName);
            BufferedReader bufferedReader=new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
            	if(debugflag==1)
        		{
        			line=" ";
        		}
            	if (line!=" ") 
            	{
            		
                	String taskid=line.substring(0, line.indexOf(" "));
                	fileContent.add(taskid);
                	for (TaskInfo task : allTasks)
                	{
                		if (task.taskId.equals(taskid))
                		{
                			task.Done=true;
                		}
                	}
                  
            	}
            	
            }   

            bufferedReader.close();   
            
        }
        
        catch(Exception ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
   
        
        return fileContent;
	}

	public void getNextDestination(String taskId)
	{
		String destination="";
		for (OrderedBusinessFlow flow: orderedFlowList)
		{
			if (flow.source.equals(taskId))
			{
				destination=flow.destination;
			}
		}
		
		if (destination.contains("exclusivegateway"))
		{
			//GenerateWebPage(taskId);
		}
		
		else if (destination.contains("parallelgateway"))
		{
			parallelInDestination(destination,0);
		}
		
		else
		{
			//GenerateWebPage(destination);
		}
		
	}
	
	public void WritePararell(String pararellId, String assignee, int debugflag)
	{
	//	String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
		String currentAssignee="Manager";
		if(debugflag==1)
			assignee="Customer";
		if (currentAssignee.equals(assignee))
		{
			try
			{
				 String fileName = "parallel.txt";
				
			     BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
			     if(debugflag==2)
					 writer.close();
			     writer.write(pararellId);
			     writer.newLine();
			     
			    writer.close();
			}
			catch(Exception e)
			{
				
			}
		}
		
		else
		{
			return;
		}
		
	}
	
	public String getLastInstanceNotDone(int debugflag)
	{
		List<String> fileContent=new ArrayList<String>();
        String fileName = "parallel.txt";
        if(debugflag==1)
        	fileName="12";
        String line = null;
        try 
        {
            FileReader fileReader=new FileReader(fileName);
            BufferedReader bufferedReader=new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
            	if(debugflag==2)
            		line=" ";
            	if (line!=" ") 
            	{
            		
                	//String taskid=line.substring(0, line.indexOf(" "));
                	fileContent.add(line);
                  
            	}
            	
            }  
            
            for(int i=fileContent.size()-1;i>=0;i--)
            {
            	if(debugflag==3)
            	{
            		fileContent = new ArrayList<String>();
                	fileContent.add("Done");
                	

            	}
            		if(debugflag==4)
            		{
            			fileContent = new ArrayList<String>();
                    	fileContent.add("yup");
            		}
            	if(!fileContent.get(i).contains("Done"))
            	{
            		
                    System.out.println("UYYUYYGGGGJ" + i);                

            		return fileContent.get(i);
            	}
            	
            }
            bufferedReader.close();   
            
        }
        
        catch(Exception ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
     
		
        return "Finish";
	}
	
	public String WritePararellUpdated(String child, String assignee,int debugflag)
	{
		//String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
		String currentAssignee="Manager";
		if (!currentAssignee.equals(assignee))
		{
			return "";
		}
			
		List<String> fileContent=new ArrayList<String>();
        String fileName = "parallel.txt";
       
        String line = null;
        try 
        {
        	PrintWriter writerr = new PrintWriter(fileName);
        	writerr.print("FinalizeLoanRequest");
        	writerr.close();
		
            FileReader fileReader=new FileReader(fileName);
            if(debugflag==1)
            {
            	fileReader=null;
            }
            BufferedReader bufferedReader=new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
            	if(debugflag==2)
            		line=" ";
            	if (line!=" ") 
            	{
            		if(debugflag==4)
            			line="EEE";
                	if(line.equals(child))
                	{
                		fileContent.add(child+ " Done");
                	}
                
                	else
                	{
                		fileContent.add(line);
                	}
                  
            	}
            	
            }  

            bufferedReader.close();   
            
        }
        
        catch(Exception ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }

        for (String s:fileContent)
        {
        	System.out.println("filee content: "+s);
        }
        
        
        
        //Writing in file
        try{
        	PrintWriter writerr = new PrintWriter(fileName);
        	if(debugflag==4)
        		writerr=null;
        	writerr.print("");
        	writerr.close();
		
		     BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
		     for(String line2: fileContent)
		     {
		    	 System.out.println("Writing to file: "+line2);
		    	 writer.write(line2);
		    	 writer.newLine();
		    	 //writer.flush();
		     }
		     
		    writer.close();
		}
		catch(Exception e)
		{
			
		}
        
        return child;
		
	}
	
	public Boolean allOtherThanOriginalChildDone(int debugflag)
	{
		System.out.println("Origi counter = +"+ originalChildCounter);
		List<String> fileContent=new ArrayList<String>();
        String fileName = "parallel.txt";
        String line = null;
        try 
        {
            FileReader fileReader=new FileReader(fileName);
            if(debugflag==1)
            	fileReader=null;
            BufferedReader bufferedReader=new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
            	if (line!=" ") 
            	{
            		fileContent.add(line); 
            	}    	
            }  
            
            for(int i=fileContent.size()-1; i>originalChildCounter; i--)
            {

            	if(!fileContent.get(i).contains("Done"))
            	{
            		return false;
            	}
            }
            
            bufferedReader.close();   
            
        }
        
        catch(Exception ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
		
        return true;

	}
	
	public String getNotDoneFromStart(int debugflag)
	{
		List<String> fileContent=new ArrayList<String>();
        String fileName = "parallel.txt";
        String line = null;
        try 
        {
            FileReader fileReader=new FileReader(fileName);
            if(debugflag==1)
            	fileReader=null;
            BufferedReader bufferedReader=new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) 
            {
            	if(debugflag==2)
            		line=" ";
            	if (line!=" ") 
            	{
            		fileContent.add(line); 
            	}    	
            }  
            
            for(int i=0;i<fileContent.size();i++)
            {
            	
            	if(!fileContent.get(i).contains("Done"))
            	{
            		return fileContent.get(i);
            	}
            }
            
            bufferedReader.close();   
            
        }
        
        catch(Exception ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
		
        return "All Tasks Done";

	}

	
	public void parallelInDestination(String taskId,int debugflag)
	{
		//write pararell parent to file
		List<String> parentAllDestinations=new ArrayList<String>();
		for (OrderedBusinessFlow flowInner:orderedFlowList)
		{	
			if (flowInner.source.equals(taskId))
			{
				parentAllDestinations.add(flowInner.destination);
				//WritePararell(flowInner.destination);
				System.out.println("parent sourcesss: "+flowInner.destination);
			}							
		}
		
		if (originalChildCounter==0)
		{
			originalChildCounter=parentAllDestinations.size();
		}
		
		if(parentAllDestinations.size()==1)//end pair
		{
			if (parallelStatus.get(taskId)==false)
			{
				endPairCounter+=1;
				parallelStatus.put(taskId, true);
			}
		}
		
		else
		{
			if (parallelStatus.get(taskId)==false)
			{
				startPairCounter+=1;
				parallelStatus.put(taskId, true);
			}
		}
		
	/*	if(debugflag==1)
		{
			startPairCounter=3;
			endPairCounter=3;
		}

		System.out.println("parent des = "+parentAllDestinations.size() + "stat coun"+startPairCounter+ "end co"+endPairCounter+ "all other"+allOtherThanOriginalChildDone());
		if(parentAllDestinations.size()==1 && (startPairCounter==endPairCounter) && !allOtherThanOriginalChildDone()) //end pair
		{
			System.out.println("parallel counter: "+parallelCounter);
			String readyForGUI=getLastInstanceNotDone();
			System.out.println("inn size 1 conditionn: "+readyForGUI);
//			//GenerateWebPage(readyForGUI);
		}

		else if(parentAllDestinations.size()==1 && !allOtherThanOriginalChildDone()) //end pair
		{
			System.out.println("parallel counter: "+parallelCounter);
			String readyForGUI=getLastInstanceNotDone();
			System.out.println("inn size 1 conditionn: "+readyForGUI);
			WritePararellUpdated(readyForGUI,findTaskbySource(readyForGUI).assignee);
			//GenerateWebPage(readyForGUI);
		}
		
		else if (parentAllDestinations.size()==1 && (startPairCounter==endPairCounter) && allOtherThanOriginalChildDone())
		{
			String taskToDo=getNotDoneFromStart();
			if (taskToDo.equals("All Tasks Done"))
			{
				originalChildCounter=0;
				setParallelStatus();
				startPairCounter=0;
				endPairCounter=0;
				//GenerateWebPage(findTaskbySource(taskId).destination);
			}
			
			else
			{
				System.out.println("All tasks not done: "+ taskToDo);
				WritePararellUpdated(taskToDo,findTaskbySource(taskToDo).assignee);
			//	GenerateWebPage(taskToDo);
			}
		}
		
		else if (parentAllDestinations.size()==1 && allOtherThanOriginalChildDone())
		{
			WritePararell(findTaskbySource(taskId).destination,findTaskbySource(taskId).assignee);
			WritePararellUpdated(findTaskbySource(taskId).destination,findTaskbySource(taskId).assignee);
			System.out.println("All tasks done: "+ findTaskbySource(taskId).destination);
			//GenerateWebPage(findTaskbySource(taskId).destination);
			
		}
		
		
		
		else //start pair
		{
			System.out.println("parallel counter: "+parallelCounter);
			for (String flowInner:parentAllDestinations)
			{	
				WritePararell(flowInner,findTaskbySource(flowInner).assignee);
			}							
			
			String readyForGUI=WritePararellUpdated(parentAllDestinations.get(0),findTaskbySource(parentAllDestinations.get(0)).assignee);
			//GenerateWebPage(readyForGUI);
		}	*/
	}

		
	
	
	
	
	public void updataTaskStatus(String taskId,int debugflag)
	{
        
		String fileName = "taskStatus.txt";
        try 
        {
            FileWriter fileWriter=new FileWriter(fileName,true);
            if(debugflag==1)
            	fileWriter=null;
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);

            bufferedWriter.write(taskId);
            bufferedWriter.write(" Done");
            bufferedWriter.newLine();
            
            bufferedWriter.close();
        }
        catch(Exception ex) 
        {
            ex.printStackTrace();
        }
	}
	

	
	//returns task object by matching source
	public OrderedBusinessFlow findTaskbySource(String taskSource)
	{
		System.out.println("size = "+ orderedFlowList.size());

		for (OrderedBusinessFlow flow : orderedFlowList)
		{
			System.out.println("flo src = "+ flow.source);

			
		}
		System.out.println("task ID IS = "+ taskSource);
		String taskId;
		String firstTask;
		for (OrderedBusinessFlow flow : orderedFlowList)
		{
			if(flow.source.equals(taskSource))
			{
				return flow;
			}
			
		}
		
		return null;
	}


	
	
	public String checkIfDone (String taskId)
	{
		for (TaskInfo task : allTasks)
		{	
			
			if (taskId.contains("startevent")) return "Done";
			
			if (task.taskId.equals(taskId))
			{
				
				
				if(task.Done == true)
				{
					return "Done";
				}
				
				else
				{
					return "Not Done";
				}
			}
		}
		
		return "Not found";
	}


	
	
	public String findTaskType(String taskId,int debugflag)
	{
		if(debugflag!=1)
			
		{
		for (TaskInfo task : allTasks)
		{
			if (taskId.equals(task.taskId));
			{
				return task.taskType;
			}
		}
		}
		
		
		return "None";
	}
	
	public UserTask getUserTaskProperties (String taskId)
	{
		for (UserTask task : userTaskList)
		{
			if (task.id.equals(taskId))
			{
				return task;
			}
		}
		
		return null;
	}
	
	public EmailServiceTask getEmailTaskProperties (String taskId)
	{
		if(emailTaskList.size()==0)
		{
			System.out.println("lisst emptyy hao");
		}
		for (EmailServiceTask task : emailTaskList)
		{
			System.out.println("IN FOOOR LOOOOOOOOOOOOOOOp"+task.taskId);
			if (task.taskId.equals(taskId))
			{
				System.out.println("IF MATCHES"+task.taskId);
				return task;
			}
		}
		
		return null;
	}
	
	public String findNextDestination(String taskId)
	{
		String dest;
		for (SequenceFlowInfo seq : sequenceFlowList)
		{
			if (seq.source.equals(taskId))
			{
				dest=seq.destination;
				return dest;
			}
		}
		return null;
	}
	


	
	
	/*public void GenerateWebPage(String taskId)
	{ 
		System.out.println("Gen page pee Task ID is:--> "+taskId);
		for (int i=0;i<allCandidates.size();i++)
		{
			System.out.println(allCandidates.get(i));
		}
		
		UserTaskInterface userTaskGui=new UserTaskInterface();
		EmailServiceInterface emailGui=new EmailServiceInterface();
		System.out.println("current session: "+VaadinSession.getCurrent().getAttribute("user")); 
		String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
		
		for (TaskInfo task : allTasks)
		{
			
			//if ((taskId.equals(task.taskId)) && (task.Done == false) && (task.taskAssignee.equals(currentAssignee))) //For Start Event
			if ((taskId.equals(task.taskId)) && (task.taskAssignee.equals(currentAssignee)))
			{
				execution=true;
				if (task.taskType.equals("userTask"))
				{
					userTaskGui.displayUserTaskForm(getUserTaskProperties(task.taskId)); //Sending first User Task properties for GUI
					task.Done=true;
					System.out.println("Task done jo hogaya hai: "+task.taskId);
				}
					
				else if (task.taskType.equals("serviceTask") && (task.taskJavaFile.equals("AppGenFiles.EmailService"))) //Service + Email Task
				{
					System.out.println("email tasssk mai agya " + taskId);
					emailGui.displayEmailForm(getEmailTaskProperties(taskId));
					task.Done=true;
				
					System.out.println("Task done jo hogaya hai: "+task.taskId);
				}
				
			}
			
			
			else if ((taskId.equals(task.taskId)) && (!task.taskAssignee.equals(currentAssignee)))
			{
				if (taskId.contains("parallelgateway") || taskId.contains("exclusivegateway"))
				{
					//new UserTaskInterface().goForward(findTaskbySource(taskId),null, null, null);
				}
				else
				{
					
					System.out.println("endddd kar raha hun naa");
					EndView end=new EndView();
					
				}
				
			}
			
			else if((taskId.equals(task.taskId)) && (task.taskAssignee.equals(currentAssignee))&& checkIfDone(taskId).equals("Done"))
			{
				//userTaskGui.goForward(findTaskbySource(taskId), null, null, null);
			}
			
			
		}//for loop ends here
				
		new UserTaskInterface().enumIds_input.clear();
	}*/
}
