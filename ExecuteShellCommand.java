import java.util.*;
import java.io.*;

public class ExecuteShellCommand
{
	public ExecuteShellCommand()
	{

	}

	public String executeCommand(String command)
	{
		StringBuffer output = new StringBuffer();
		Process p;

		try
		{
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";

			while((line = br.readLine())!=null)
			{
				output.append(line + "\n");
			}

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return output.toString();
	}
}