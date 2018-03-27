package AppGenFiles;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.CharacterData;
import Activiti.OrderedBusinessFlow;
import my.vaadin.FirstActivitiGUI.EnumField;
import my.vaadin.FirstActivitiGUI.FormProperties;

public class XMLParser 
{
	public String fileName="/Users/Harrum/Downloads/FirstActivitiGUI/src/main/resources/oldMajor.xml";
	public boolean isPresent(List<OrderedBusinessFlow> orderedList, OrderedBusinessFlow flowObj)//checks if obj is presnt or not
	{
		for(OrderedBusinessFlow obf: orderedList)
		{
			if(obf.source.equals(flowObj.source) && obf.destination.equals(flowObj.destination))
			{
				return true;
			}
		}
		return false;
	}
	
	//function that finds all furhter paths of an exclusive gateway
	// study Mock Objects
	// install ECLEmma coverage analyzer
	public void getExclusivePaths(List<TaskInfo> allTasks,List<OrderedBusinessFlow> businessFlowPath,List<SequenceFlowInfo> seqFlowInfo,String source, int debugflag)
	{
			for (SequenceFlowInfo flow: seqFlowInfo)
			{	
				if (flow.source.equals(source))
				{
					List<String> sources=new ArrayList<String>();
					OrderedBusinessFlow flowObj=null;
					for (SequenceFlowInfo flowInner: seqFlowInfo)
					{
						if(debugflag==1)
						{
							source=null;
							flowInner.source="123";
						}
						if (source!=null && source.equals(flowInner.source))
						{
					
							flowObj=new OrderedBusinessFlow();
							flowObj.source=source;
							flowObj.destination=flowInner.destination;
							flowObj.assignee=getAssignee(flowObj.source, allTasks);
							if(!isPresent(businessFlowPath,flowObj))
							businessFlowPath.add(flowObj);
							else
							{
								
							}
							
							sources.add(flowObj.destination);
							
						}
						else
						{
							
						}
					}
					
					for (String source_str : sources)
					{
						getExclusivePaths(allTasks, businessFlowPath, seqFlowInfo, source_str,0);
					}
				
				}			
				
			}
	}
	
	public List<OrderedBusinessFlow> orderSequenceFlow(List<SequenceFlowInfo> sequenceFlowList, List<TaskInfo> allTasks)
	{
		
		List<OrderedBusinessFlow> orderedFlow=new ArrayList<OrderedBusinessFlow>();
		String newSource="";
		String newSource2="";
		for (SequenceFlowInfo seq : sequenceFlowList)
		{
			OrderedBusinessFlow obj=new OrderedBusinessFlow();
			if(seq.source.contains("startevent")) //For Start Event only
			{
				obj.source=seq.source;
				obj.destination=seq.destination;
				obj.assignee="None";
				newSource=obj.destination;
				orderedFlow.add(obj);
			}
			else
			{
				
			}
			
			
			for (SequenceFlowInfo seq2 : sequenceFlowList) // for multiple destinations of exclusive and parallel gateways
			{
				List<String> exclusive_values=new ArrayList<String>(); 
				
				
				if (seq2.source.equals(newSource))
				{
					OrderedBusinessFlow obj2=new OrderedBusinessFlow();
					obj2.source=newSource;
					obj2.destination=seq2.destination;
					obj2.assignee=getAssignee(obj2.source, allTasks);
					
					newSource2=obj2.destination;
					if(!isPresent(orderedFlow,obj2))
				    	orderedFlow.add(obj2);
					else
					{
						
					}
					
					if ((newSource.contains("exclusivegateway")) || (newSource.contains("parallelgateway")))
					{
						exclusive_values.add(newSource2);
						getExclusivePaths(allTasks, orderedFlow, sequenceFlowList, newSource2,0);
						
					}
					else
					{
						
					}
					
				}	
				else
				{
					
				}
			}
			
			newSource=newSource2;			
			
		}
		
		for (OrderedBusinessFlow flow : orderedFlow)
		{
			//System.out.println("Source: "+flow.source + " Destination: " + flow.destination + " Assignee: "+ flow.assignee);
		}
		
		return orderedFlow;
		
	}
	
	public String getAssignee (String taskId, List<TaskInfo> allTasks)
	{
		for(TaskInfo task : allTasks)
		{
			//System.out.println("task ID = "+ task.taskId +" and taskid = " +taskId);

			if(task.taskId.equals(taskId))
			{
				if (taskId.contains("parallelgateway") || taskId.contains("exclusivegateway"))
				{
					return "None";
				}
				else
				{
				return task.taskAssignee;
				}
			}
			else
			{
				
			}
		}
		
		
		return "Not found";
	}
	
	public List<EmailServiceTask> getEmailProperties(int debugflag)
	{
		boolean x=true;
		List<EmailServiceTask> emailTaskList=new ArrayList<EmailServiceTask>(); //for all email tasks
		String taskType="serviceTask";
		EmailServiceTask emailTaskObj=null;
		try 
		{
	         File inputFile = new File(fileName);
	         
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName(taskType);
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) //iterate over user tasks
	         {
	        	emailTaskObj=new EmailServiceTask();
	            Node nNode = nList.item(temp);
	            String retTaskId=nNode.getAttributes().getNamedItem("id").getNodeValue();
	            emailTaskObj.taskId=retTaskId;
	            if(debugflag==1)
	            	x=false;
	           // System.out.println("node val is  "+nNode.getAttributes().getNamedItem("activiti:class").getNodeValue() + " the task id is = "+emailTaskObj.taskId);
	            if (!(nNode.getAttributes().getNamedItem("activiti:class").getNodeValue().equals("AppGenFiles.EmailService")&&x))
	            	continue;
	            
	            System.out.println("GOtten-----------------> "+emailTaskObj.taskId);
	            emailTaskObj.taskName=nNode.getAttributes().getNamedItem("name").getNodeValue();
	            emailTaskObj.assignee=nNode.getAttributes().getNamedItem("activiti:assignee").getNodeValue();
	         
	            NodeList childList = nNode.getChildNodes();
            	System.out.println("child list length =" + childList.getLength());	            
	            emailTaskList.add(emailTaskObj);
	            }//outermost for loop ends here	      
	      }
		
	      catch(Exception e)
	      {
	        	 
	      }

		  return emailTaskList;
	}
	
	public UserTask getFormProperties(String taskType,String taskId)
	{
		UserTask userTaskObj=new UserTask(); //User task form properties also Enum properties
		List<UserTask> userTaskList=new ArrayList<UserTask>(); 
		String enumID;
		try 
		{
	         File inputFile = new File(fileName);
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName(taskType);
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) //iterate over user tasks
	         {
	            Node nNode = nList.item(temp);
	            String retTaskId=nNode.getAttributes().getNamedItem("id").getNodeValue();
	            userTaskObj.id=taskId;
	            
	            if (!retTaskId.equals(taskId))
	            {
	            	continue;
	            }
	            
	            userTaskObj.name=nNode.getAttributes().getNamedItem("name").getNodeValue();
	            userTaskObj.assignee=nNode.getAttributes().getNamedItem("activiti:assignee").getNodeValue();
	         
	            NodeList childList = nNode.getChildNodes();
	            
	            for (int attr = 0; attr < childList.getLength(); attr++) //iterate over form properties of this user task
	            {	            	
	            	Node childNode = childList.item(attr); //get form attributes node
	            	String childNodeName=childNode.getNodeName();
	            	if (childNodeName.equals("extensionElements"))
	            	{
	            		NodeList formAttrs=childNode.getChildNodes(); //get all child nodes of extension elements
	            		for (int field = 0; field < formAttrs.getLength(); field++) //iterate over form properties of this user task
	     	            {
	            			FormProperties fieldProperties=new FormProperties();
	            			Node attrNode=formAttrs.item(field);
	     
	            			if (attrNode.getNodeType() == Node.ELEMENT_NODE) 
				            {
	            				
				                Element eElement = (Element) attrNode;
				               
				                fieldProperties.id=eElement.getAttribute("id");
				                enumID=eElement.getAttribute("id");
				                String enumFieldName=eElement.getAttribute("name"); //Like do you accept or reject?			               
				                fieldProperties.name=eElement.getAttribute("name");			           
				                fieldProperties.type=eElement.getAttribute("type");
				                
				                String fieldType=eElement.getAttribute("type");
				                String enumVariable=eElement.getAttribute("variable");;
				                List<String> enumValues=new ArrayList<String>();
				                if (fieldType.equals("enum"))
				                {
				            	    EnumField enumObj=new EnumField();
				            	    NodeList enumAttrs=attrNode.getChildNodes(); //get all child nodes of Enum field
				            	    for (int enum_field = 0; enum_field < enumAttrs.getLength(); enum_field++) //iterate over form properties of this user task
				     	            {				            			
				            			Node enumNode=enumAttrs.item(enum_field);
				            			if (enumNode.getNodeType() == Node.ELEMENT_NODE) 
							            {				            				
							                Element element = (Element) enumNode;						               
							                enumObj.id=enumID;
							                enumObj.name=enumFieldName;							                
							                enumObj.enumVariable=enumVariable;
							                enumObj.enumValues.put(element.getAttribute("id"), element.getAttribute("name"));							               
							            }  			            			
				     	         }
				            	   
				            	 fieldProperties.enumFields=enumObj;			            	   
				              }
				                
				              userTaskObj.formPropertiesList.add(fieldProperties);     
				           }
	     	            }
	            	}	
	            }
	           
		        userTaskList.add(userTaskObj); 
	         }
	         
	       
	      }
		
	      catch(Exception e)
	      {
	        	 
	      }
		
		  return userTaskObj;
	}
	
	public List<TaskInfo> extractTasks(String taskType)
	{
		List<TaskInfo> taskInfoList=new ArrayList<TaskInfo>();
		try 
		{
	         File inputFile = new File(fileName);
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName(taskType);
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) 
	         {
	             Node nNode = nList.item(temp);  
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) 
	             {
	                 Element eElement = (Element) nNode;
	                 //setting all attributes of TaskInfo Object
	                 TaskInfo task_info_obj=new TaskInfo();
	                 task_info_obj.taskName=eElement.getAttribute("name"); 
	                 task_info_obj.taskAssignee=eElement.getAttribute("activiti:assignee"); 
	                 task_info_obj.taskJavaFile=eElement.getAttribute("activiti:class"); 
	                 task_info_obj.taskType=taskType; 
	                 task_info_obj.taskId=eElement.getAttribute("id"); 
	               
	                 taskInfoList.add(task_info_obj);
	             }
	             else
	             {
	            	 
	             }
	         }
	    } 
		catch (Exception e) 
		{
	       // e.printStackTrace();
	    }

		return taskInfoList;	
	}// function extract tasks ends here
	
	public List< SequenceFlowInfo> extractSequenceFlow(String taskType)
	{
		List<SequenceFlowInfo> SequenceFlowInfoList=new ArrayList<SequenceFlowInfo>();
		try 
		{
	         File inputFile = new File(fileName);
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName(taskType); 
	         
	         for (int temp = 0; temp < nList.getLength(); temp++) 
	         {
	            Node nNode = nList.item(temp);
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) 
	            {
	               Element eElement = (Element) nNode;
	               //setting all attributes of TaskInfo Object
	               SequenceFlowInfo sequence_info_obj=new SequenceFlowInfo();
	               sequence_info_obj.taskType=taskType;
	               sequence_info_obj.source=eElement.getAttribute("sourceRef"); 
	               sequence_info_obj.destination=eElement.getAttribute("targetRef"); 
	               
	               NodeList childAttr=nNode.getChildNodes();
	               for (int field = 0; field < childAttr.getLength(); field++) //iterate over form properties of this user task
    	           {	
           		    	Node attrNode=childAttr.item(field);
           		    
           		    	Node child=attrNode.getFirstChild();
           		    	
           		    	if (child instanceof CharacterData)
           		    	{
           		    		CharacterData cd=(CharacterData) child;
           		    		 
           		    		String gatewayVal=cd.getData().substring(cd.getData().indexOf("'")+1, cd.getData().length()-2);
           		    		sequence_info_obj.gatewayValue=gatewayVal;
           		    
           		    	}
           		    	else
           		    	{
           		    		
           		    	}
    	           }
	               
	               SequenceFlowInfoList.add(sequence_info_obj);
	            }
	            else
	            {
	            	
	            }
	         }
	    }	
		catch (Exception e) 
		{
	        //e.printStackTrace();
	    }

		return SequenceFlowInfoList;	
	}// function extract tasks ends here

}//class parse ends here
