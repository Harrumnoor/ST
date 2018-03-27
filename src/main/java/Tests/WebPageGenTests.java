package Tests;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import Activiti.OrderedBusinessFlow;
import AppGenFiles.EmailServiceTask;
import AppGenFiles.NewDriver;
import AppGenFiles.TaskInfo;
import AppGenFiles.UserTask;
import GUI.WebPageGen;
import junit.framework.TestCase;
import AppGenFiles.NewDriver;
public class WebPageGenTests {

	
@Test
	public void setParallelStatusTest()
	{
		new NewDriver().startUp();
		WebPageGen w = new WebPageGen();
		w.setParallelStatus();
		assertEquals(w.parallelStatus.size(),6);
	}
	
	@Test
	public void selectedExclusivePathTest ()
	{
		//public String selectedExclusivePath (UserTask usertask, HashMap<String, String> enumIds_input)
		HashMap<String, String> enumIds_input =new HashMap<String, String>();
		enumIds_input.put("true", "Yes");
		enumIds_input.put("acceptORreject", "No");
		String output=new WebPageGen().selectedExclusivePath (new NewDriver().userTaskList.get(2), enumIds_input,0);
		assertEquals("SendRejectionEmail",output);
		HashMap<String, String> enumIds_input1 =new HashMap<String, String>();
		enumIds_input1.put("true", "Yes");
		enumIds_input1.put("false", "No");
		output=new WebPageGen().selectedExclusivePath (new NewDriver().userTaskList.get(0), enumIds_input1,0);
		assertEquals("Not Found",output);	
		HashMap<String, String> enumIds_input2 =new HashMap<String, String>();
		enumIds_input2.put("true", "Yes");
		enumIds_input2.put("acceptORreject", "No");
		output=new WebPageGen().selectedExclusivePath (new NewDriver().userTaskList.get(2), enumIds_input2,1);
		assertEquals("Not Found",output);	
	}
	
	@Test
	public void findNumberOfBranchesTest()
	{
		OrderedBusinessFlow flowObj = new OrderedBusinessFlow();
		flowObj.assignee="None";
		flowObj.source="exclusivegateway1";
		int num=new WebPageGen().findNumberOfBranches (flowObj);
		assertEquals(1, num);
	}
	
	@Test
	public void getOrdObjectTest()
	{
		
		OrderedBusinessFlow obj = new OrderedBusinessFlow();
		new NewDriver().startUp();
	    obj=new WebPageGen().getOrdObject(new WebPageGen().orderedFlowList.get(0).source);
	    assertEquals(new WebPageGen().orderedFlowList.get(0), obj);
	    obj=new WebPageGen().getOrdObject("hi");
	    assertEquals(null, obj);
		
	}
	
	@Test
	public void getPrevObj()
	{
		OrderedBusinessFlow obj = new OrderedBusinessFlow();
		new NewDriver().startUp();
	    obj=new WebPageGen().getOrdObject(new WebPageGen().orderedFlowList.get(0).source);
	  //  System.out.println("_**********************________________src is = "+ new WebPageGen().orderedFlowList.get(2).source);
	    obj =new WebPageGen().getPrevObj(new WebPageGen().orderedFlowList.get(2));
	    assertEquals("SubmitApplication", obj.source);
	    obj.source="hi";
	    obj=new WebPageGen().getPrevObj(obj);
	    assertEquals(null, obj);
	}
	
	@Test
	
	public void getAllExclusiveDestinationsTest()
	{
		OrderedBusinessFlow flowObj = new OrderedBusinessFlow();
		flowObj.assignee="None";
		flowObj.source="exclusivegateway1";
		List<OrderedBusinessFlow> mylist =new WebPageGen().getAllExclusiveDestinations (flowObj);
		assertEquals(1, mylist.size());
	}
	
	@Test
	public void getAllDestinationsTest()
	{
		OrderedBusinessFlow flowObj = new OrderedBusinessFlow();
		flowObj.assignee="None";
		flowObj.source="exclusivegateway1";
		List<OrderedBusinessFlow> mylist =new WebPageGen().getAllDestinations (flowObj);
		assertEquals(3, mylist.size());
	}
	
	@Test
	public void getAllSourcesTest()
	{
		OrderedBusinessFlow flowObj = new OrderedBusinessFlow();
		flowObj.assignee="None";
		flowObj.source="exclusivegateway1";
		List<OrderedBusinessFlow> mylist =new WebPageGen().getAllSources (flowObj);
		assertEquals(1, mylist.size());
	}
	
	@Test
	public void displaySpecificTest()
	{
		try
		{
		new NewDriver().startUp();
		new WebPageGen().displaySpecific(new WebPageGen().orderedFlowList.get(2),0);
		new WebPageGen().displaySpecific(new WebPageGen().orderedFlowList.get(0),0);
		new WebPageGen().displaySpecific(new WebPageGen().orderedFlowList.get(1),0);
		new WebPageGen().displaySpecific(new WebPageGen().orderedFlowList.get(1),1);



		}
		catch(Exception e)
		{
			fail("Display Specific Has Failed");
		}
	}
	
	@Test
	public void getUserIdFromEnumVariableTest()
	{
		String s=new WebPageGen().getUserIdFromEnumVariable("Accept_Reject");
		assertEquals("QualifyForLoan",s);
		s=new WebPageGen().getUserIdFromEnumVariable("Accept_Rejectrrrr");
		assertEquals("Not Found",s);

	}
	
	@Test
	public void getGatewayOutTest()
	{
		String s =new WebPageGen().getGatewayOut("exclusivegateway1","No");
		assertEquals("SendRejectionEmail",s);
		s =new WebPageGen().getGatewayOut("   exclusivegateway1","No");
		assertEquals("Not Found",s);



	}
	
	@Test
	public void checkAssigneeTaskDoneTest()
	{
		try
		{
			new WebPageGen().checkAssigneeTaskDone("SubmitApplication",0);
			new WebPageGen().checkAssigneeTaskDone("SubmitApplication",1);
			new WebPageGen().checkAssigneeTaskDone("SubmitApplication",3);
			new WebPageGen().checkAssigneeTaskDone("SubmitApplication",2);



		}
		catch(Exception e)
		{
			fail("FAILED");
		}

		
	}
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	@Test
	public void getUpdatedTaskStatus2Test() 
	{
		try {
			int lines = countLines("taskStatus.txt");
		    List<String> tasks = new ArrayList<String>();
		    tasks=new WebPageGen().getUpdatedTaskStatus2(0);
		    assertEquals(lines,tasks.size());
		    tasks=new WebPageGen().getUpdatedTaskStatus2(1);
		    tasks=new WebPageGen().getUpdatedTaskStatus2(2);



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void findTaskbySourceTest()

	{
		new NewDriver().startUp();
		OrderedBusinessFlow obf = new WebPageGen().findTaskbySource(new WebPageGen().orderedFlowList.get(1).source);
		assertEquals(new WebPageGen().orderedFlowList.get(1), obf);
		obf = new WebPageGen().findTaskbySource("yes");
		assertEquals(null, obf);

	}
	
	@Test
	public void checkIfDoneTest()
	{
		String s=new WebPageGen().checkIfDone("startevent");
		assertEquals("Done", s);
		s=new WebPageGen().checkIfDone("SubmitApplication");
		assertEquals("Done", s);
		s=new WebPageGen().checkIfDone("sg");
		assertEquals("Not found", s);
		s=new WebPageGen().checkIfDone("GetIncomeDetails");
		assertEquals("Not Done", s);

	}
	@Test
	public void findTaskTypeTest()
	{
		new NewDriver().startUp();
		WebPageGen w =new WebPageGen();
		String s=w.findTaskType("SubmitApplication",0);
		assertEquals("userTask", s);
		String s1=w.findTaskType("klSuon",0);
		//assertEquals("null", s1);
		String s2=w.findTaskType("SendConfirmationLetter",0);
		//assertEquals("serviceTask", s2);
    	w.findTaskType("SendConfirmationLetter",1);

		
	}
	@Test
	public void getUserTaskPropertiesTest()
	{
		new NewDriver().startUp();
		WebPageGen w =new WebPageGen();
		UserTask u = new UserTask();
		u= w.getUserTaskProperties("SubmitApplication");
		assertEquals(w.userTaskList.get(0),u);
		u= w.getUserTaskProperties("ddddSubmitApplication");
		assertEquals(null,u);

		
	}
	@Test
	public void getEmailTaskPropertiesTest()
	{
		new NewDriver().startUp();
		WebPageGen w =new WebPageGen();
		EmailServiceTask e = new EmailServiceTask();
		e= w.getEmailTaskProperties("SendRejectionEmail");
		assertEquals(w.emailTaskList.get(0),e);
		e= w.getEmailTaskProperties("ddddSubmitApplication");
		assertEquals(null,e);
		w.emailTaskList=new ArrayList<EmailServiceTask>();
		e= w.getEmailTaskProperties("ddddSubmitApplication");
		assertEquals(null,e);

		
	}

	@Test
	public void findNextDestinationTest()
	{
		new NewDriver().startUp();
		WebPageGen w =new WebPageGen();
		String dest=w.findNextDestination("SubmitApplication");
		assertEquals("GetIncomeDetails",dest);
		dest=w.findNextDestination("S");
		assertEquals(null,dest);
		
	}
	@Test
	public void parallelInDestinationTest()
	{
		new NewDriver().startUp();
		WebPageGen w =new WebPageGen();
		w.setParallelStatus();
		w.parallelInDestination("parallelgateway1",0);
		w.parallelInDestination("parallelgateway4",0);
		   
	    
	}
	
	@Test
	public void getNotDoneFromStartTest()
	{
		new NewDriver().startUp();
		WebPageGen w =new WebPageGen();
		try
		
		{
			
			w.getNotDoneFromStart(0);
			assertEquals("FinalizeLoanRequest",w.getNotDoneFromStart(0));
			PrintWriter writerr = new PrintWriter("parallel.txt");
        	writerr.print("");
        	writerr.close();
			assertEquals("All Tasks Done",w.getNotDoneFromStart(0));
			w.getNotDoneFromStart(1);
			w.getNotDoneFromStart(2);


		}
		catch(Exception e)
		{
			
		}
		
		
	}
	
	@Test
	public void updataTaskStatusTest()
	{
		try
		{
			new NewDriver().startUp();
			WebPageGen w =new WebPageGen();
			w.updataTaskStatus("SubmitApplication",1);
		}
		catch(Exception e)
		{
			fail("Update task status has failed");
		}
	}
	@Test
	public void allOtherThanOriginalChildTest()
	{
		new NewDriver().startUp();
		WebPageGen w =new WebPageGen();
		try
		{
			String fileName = "parallel.txt";
	        
	            FileWriter fileWriter=new FileWriter(fileName,true);
	            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);

	            bufferedWriter.write("FinalizeLoanRequest");
	            bufferedWriter.newLine();
	            bufferedWriter.write("FinalizeLoanRequest");
	            bufferedWriter.newLine();
	            bufferedWriter.write("FinalizeLoanRequest");
	            bufferedWriter.newLine();
	            
	            bufferedWriter.close();
	     
			//PrintWriter writerr = new PrintWriter("parallel.txt");
        	//writerr.print("");
        	//writerr.close();
			assertEquals(false,w.allOtherThanOriginalChildDone(0));
			PrintWriter writerr = new PrintWriter("parallel.txt");
        	writerr.print("");
        	writerr.close();
			assertEquals(true,w.allOtherThanOriginalChildDone(0));
			w.allOtherThanOriginalChildDone(1);
			writerr = new PrintWriter("parallel.txt");
        	writerr.print("Done");
        	writerr.close();
			w.allOtherThanOriginalChildDone(2);


		}
		catch(Exception e)
		{
			
		}
		
	}

@Test
public void WritePararellUpdatedTest()
{
	new NewDriver().startUp();
	WebPageGen w =new WebPageGen();
	assertEquals("",w. WritePararellUpdated("FinalizeLoanRequest","Customer",0));
	assertEquals("FinalizeLoanRequest",w. WritePararellUpdated("FinalizeLoanRequest","Manager",0));
	w. WritePararellUpdated("FinalizeLoanRequest","Manager",1);
	w. WritePararellUpdated("FinalizeLoanRequest","Manager",2);
	w. WritePararellUpdated("FinalizeLoanRequest","Manager",4);




}

@Test
public void getLastInstanceNotDoneTest()
{
	String fileName = "parallel.txt";
    try 
    {
        FileWriter fileWriter=new FileWriter(fileName,true);
        BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);

        bufferedWriter.write("FinalizeLoanRequest");
        bufferedWriter.newLine();
        bufferedWriter.write("FinalizeLoanRequest");
        bufferedWriter.newLine();
        bufferedWriter.write("FinalizeLoanRequest");
        bufferedWriter.newLine();
        
        bufferedWriter.close();
    	new NewDriver().startUp();
    	WebPageGen w =new WebPageGen();
    	assertEquals("FinalizeLoanRequest",w.getLastInstanceNotDone(0));
    	w.getLastInstanceNotDone(1);
    	w.getLastInstanceNotDone(2);
    	w.getLastInstanceNotDone(3);
    	w.getLastInstanceNotDone(4);



    }
    catch(IOException ex) 
    {
        ex.printStackTrace();
    }
}
@Test
public void WritePararellTest()
{
	try
	{
		int lines =countLines("parallel.txt");
		new NewDriver().startUp();
    	WebPageGen w =new WebPageGen();
		w.WritePararell("FinalizeLoanRequest","Manager",0);
		int lines1 =countLines("parallel.txt");
		assertEquals(lines+1,lines1);
		w.WritePararell("FinalizeLoanRequest","Manager",1);
		w.WritePararell("FinalizeLoanRequest","Manager",2);


		
	}

catch(IOException ex) 
{
    ex.printStackTrace();
}
	
}
@Test
public void getNextDestinationTest()
{
	try
	{
	new NewDriver().startUp();
	WebPageGen w =new WebPageGen();
	w.getNextDestination("SendAcceptanceEmail");
	w.getNextDestination("SubmitApplication");
	w.getNextDestination("QualifyForLoan");
	}
	catch(Exception e)
	{
		fail("Test has failed");
	}


}


}
