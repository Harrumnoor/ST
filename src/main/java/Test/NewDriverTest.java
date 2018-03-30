package Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import AppGenFiles.NewDriver;
import junit.framework.TestCase;
import org.junit.Test;

public class NewDriverTest {
	
	@Test
	public void DriverTest()
	{
		 new NewDriver().startUp();
		 PrintWriter writerr;
		try {
			writerr = new PrintWriter("taskStatus.txt");
		
     	 writerr.print("");
     	 writerr.close();
		 new NewDriver().startUp();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
