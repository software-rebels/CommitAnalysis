import java.util.*;
import java.io.*;

public class Parse
{
	String resultsLocationFolder;

	public static void main(String[] args)
	{
		String loc = args[0];
		System.out.println(loc);
		Parse parser = new Parse(loc);
		parser.parseFiles(loc+"/allFiles.txt");
	}

	public Parse(String location)
	{
		this.resultsLocationFolder = location;
	}

	public void parseFiles(String allFiles)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(allFiles)));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resultsLocationFolder+"/buildfiles.txt")));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(resultsLocationFolder+"/sourcefiles.txt")));
			BufferedWriter bw3 = new BufferedWriter(new FileWriter(new File(resultsLocationFolder+"/allFilesEdited.txt")));


			for(String curLine = br.readLine(); curLine!=null; curLine = br.readLine())
			{
				curLine = curLine.toLowerCase();

				if(curLine.endsWith(".cmake")||curLine.endsWith(".txt")||curLine.contains("cmake")||curLine.contains("makefile")||curLine.contains(".make"))//||curLine.contains("make")||curLine.endsWith("make"))
				{
					bw.write(curLine);
					bw.newLine();
				}
				else if(curLine.endsWith(".cxx")||curLine.endsWith(".cpp")||curLine.endsWith(".h")||curLine.endsWith(".hpp")||curLine.endsWith(".pl")||curLine.endsWith(".sip")||curLine.endsWith(".xpm")||
					curLine.endsWith(".c")||curLine.endsWith(".py")||curLine.endsWith(".tcl")||curLine.endsWith(".java")||curLine.endsWith(".md5")||curLine.endsWith(".lnt")||curLine.contains(".pfb")||curLine.contains(".ui")||
					curLine.endsWith(".sl")||curLine.endsWith(".txx")||curLine.endsWith(".sh")||curLine.endsWith(".md5")||curLine.endsWith(".mm")||curLine.contains(".glade")||curLine.endsWith(".pem")||
					curLine.contains(".tcl")||curLine.contains(".cxx")||curLine.contains(".cpp")||curLine.contains(".h")||curLine.contains(".hpp")||curLine.contains(".jar")||curLine.contains(".gnu")||
					curLine.contains(".c")||curLine.contains(".py")||curLine.contains(".nib")||curLine.endsWith(".nib")||curLine.endsWith(".tpl")||curLine.contains(".in")
					||curLine.contains(".so")||curLine.endsWith(".so")||curLine.contains(".md5")||curLine.contains(".js")||curLine.contains(".glsl")||curLine.contains(".asm")
					||curLine.contains(".txx")||curLine.contains(".sh")||curLine.contains(".java")||curLine.contains(".mm")||curLine.contains(".sl")||curLine.contains(".bash")||curLine.contains(".m"))
				{
					bw2.write(curLine);
					bw2.newLine();
				}
				else
				{
					bw3.write(curLine);
					bw3.newLine();
				}
			}
			
			bw.close();
			bw2.close();
			bw3.close();
			System.out.println("Done");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}