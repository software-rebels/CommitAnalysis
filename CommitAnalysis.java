import java.util.*;
import java.io.*;

public class CommitAnalysis
{
	DirectedGraph graph;
	HashMap<String, String> fileNames;
	Set<String> sourceFiles;
	Set<String> buildFiles;

	public String traceGdfLocation;
	public String destinationFolderForAnalysedCommits;
	public String commitLogLocation;
	public String gitRepositoryLocation;
	public int noOfCommitsToAnalyse;

	
	public static void main(String[] args)
	{
		try
		{
			CommitAnalysis analysis = new CommitAnalysis();
			analysis.getPropValues();

			ExecuteShellCommand com = new ExecuteShellCommand();
			System.out.println(com.executeCommand("./Scripts/allFiles.sh "+analysis.gitRepositoryLocation));

			String filePath = new File("").getAbsolutePath();
			analysis.parseBuildFiles(filePath+"/Data/buildfiles.txt");
			analysis.parseSourceFiles(filePath+"/Data/sourcefiles.txt");

			System.out.println("Creating Graph from trace file.");
			analysis.createGraph(analysis.traceGdfLocation);
			System.out.println("Graph Created.");

			System.out.println("Computing Page Ranks.");
			analysis.pageRanks();
			System.out.println("Page Ranks Computed.");

			System.out.println("Analysing Commits.");
			analysis.analyseCommits(analysis.destinationFolderForAnalysedCommits,analysis.commitLogLocation);
			System.out.println("Commits Analysed.");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

	}
	
	//Constructor
	public CommitAnalysis()
	{
		this.graph = new DirectedGraph();
		this.fileNames = new HashMap<String, String>();
		this.sourceFiles = new HashSet<String>();
		this.buildFiles = new HashSet<String>();
	}

	public void parseBuildFiles(String buildFilesLocation)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(buildFilesLocation)));
			String curLine = "";

			while((curLine = br.readLine()) != null)
			{
				buildFiles.add(curLine);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void parseSourceFiles(String sourceFilesLocation)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(sourceFilesLocation)));
			String curLine = "";

			while((curLine = br.readLine()) != null)
			{
				sourceFiles.add(curLine);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void analyseCommits(String destinationFolder, String commitLog)
	{
		boolean sourceChanged = false;
		boolean buildChanged = false;
		ExecuteShellCommand command = new ExecuteShellCommand();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(commitLog)));
			int i = 0;
			String curLine = "";

			while(true)
			{
				curLine = br.readLine();

				if(curLine.contains("Commit:"))
				{
					String[] temp = curLine.split(":");
					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(destinationFolder+"/"+temp[1])));
					
					System.out.println("Analysing Commit "+temp[1]);

					bw.write(curLine);
					bw.newLine();
					curLine = br.readLine();
					bw.write(curLine);
					bw.newLine();

					Set<String> changedFiles = new HashSet<String>();

					while((curLine=br.readLine()) != null && (curLine.isEmpty() == false))
					{
						changedFiles.add(curLine);
					}

					boolean[] checks = checkFiles(changedFiles);
					sourceChanged = checks[0];
					buildChanged = checks[1];

					if(checks[0] && checks[1])
					{
						//Both source and build files changed.
						//Run script that reverts to the commit version under analysis and build again.
						//Then calculate dependencies of files changed.
						System.out.println("Starting gitBuild script.");
						System.out.println(command.executeCommand("./Scripts/gitBuild.sh "+this.gitRepositoryLocation+" "+temp[1]));
						System.out.println("gitBuild script Ended.");
						this.graph.clear();
						this.fileNames.clear();
						System.out.println("Creating Graph");
						this.createGraph(this.traceGdfLocation);
						System.out.println("Graph Created.");
						System.out.println("Calculating Pageranks.");
						this.pageRanks();
						System.out.println("Pageranks calculated.");

						Iterator<String> j = changedFiles.iterator();
						while(j.hasNext())
						{
							String temp2 = j.next();
							String nodeName = findNodeName(temp2);
							if(nodeName.equals("na"))
							{
								bw.write("The target "+temp2+" could not be found.");
								bw.newLine();
							}
							else
							{
								bw.write("The target "+temp2+" has the following dependencies: ");
								bw.newLine();
								LinkedList<Node> dependencies = graph.findDep(nodeName);
								Iterator iter = dependencies.iterator();
								
								LinkedList<Node> repeats = new LinkedList<Node>();

								while(iter.hasNext())
								{
									Object element = iter.next();
									if(((Node)element).getRank() != -1.0 && !(repeats.contains((Node)element)))
									{
										bw.write(((Node)element).toString());
										bw.newLine();
										repeats.add((Node)element);
									}
								}
							}
							bw.newLine();
						}
					bw.close();
					}
					else if(checks[0] && !checks[1])
					{
						//Only source files changed.
						//Calculate dependencies of files changed.
						System.out.println("Only source files changed. Calculating dependencies.");
						Iterator<String> j = changedFiles.iterator();
						while(j.hasNext())
						{
							String temp2 = j.next();
							String nodeName = findNodeName(temp2);
							if(nodeName.equals("na"))
							{
								bw.write("The target "+temp2+" could not be found.");
								bw.newLine();
							}
							else
							{
								bw.write("The target "+temp2+" has the following dependencies: ");
								bw.newLine();
								LinkedList<Node> dependencies = graph.findDep(nodeName);
								Iterator iter = dependencies.iterator();
								
								LinkedList<Node> repeats = new LinkedList<Node>();

								while(iter.hasNext())
								{
									Object element = iter.next();
									if(((Node)element).getRank() != -1.0 && !(repeats.contains((Node)element)))
									{
										bw.write(((Node)element).toString());
										bw.newLine();
										repeats.add((Node)element);
									}
								}
							}
							bw.newLine();
						}
					bw.close();
					System.out.println("Dependencies calculated");
					}
					else if(!checks[0] && checks[1])
					{
						//Only build files changed.
						//Run script that reverts to commit version under analysis and build again.
						System.out.println("Only build files changed.");
						System.out.println("Starting gitBuild script.");
						System.out.println(command.executeCommand("./Scripts/gitBuild.sh "+this.gitRepositoryLocation+" "+temp[1]));
						System.out.println("Gitbuild ended.");
						this.graph.clear();
						this.fileNames.clear();
						System.out.println("Creating graph.");
						this.createGraph(this.traceGdfLocation);
						System.out.println("Graph Created.");
						System.out.println("Calculating page ranks");
						this.pageRanks();
						System.out.println("Pageranks calculated.");
						bw.write("In this commit only build files were changed.");
						bw.newLine();
						bw.close();
					}	
				}
				System.out.println(command.executeCommand("./Scripts/status.sh"));
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean[] checkFiles(Set<String> filesChanged)
	{
		//checks[0] is for source files.
		//checks[1] is for build files.

		boolean[] checks = new boolean[2];
		checks[0] = false;
		checks[1] = false;

		Iterator<String> iter = filesChanged.iterator();
		while(iter.hasNext() && (checks[0]==false || checks[1]==false))
		{
			String temp = iter.next();

			if(this.sourceFiles.contains(temp.toLowerCase()))
			{
				checks[0] = true;
			}

			if(this.buildFiles.contains(temp.toLowerCase()))
			{
				checks[1] = true;
			}
		}

		return checks;
	}


	public String findNodeName(String localName)
	{
		Iterator<String> iter = fileNames.keySet().iterator();
		while(iter.hasNext())
		{
			String temp = iter.next();
			if(temp.toLowerCase().contains(localName.trim().toLowerCase()) || temp.toLowerCase().trim().equals(localName.trim().toLowerCase()))
			{
				return fileNames.get(temp);
			}
		}

		return "na";
	}

	//Traverses the trace.gdf file and creates a graph using hashmaps.
	public void createGraph(String traceFile)
	{
		String readLine = "";
		String[] temp = new String[15];
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(new File(traceFile)));
			
			while(readLine.contains("nodedef>")==false)
			{
				readLine = br.readLine();
			}
			
			while(true)
			{
				readLine = br.readLine();
				if(readLine.contains("edgedef>"))
				{
					break;
				}
				temp = readLine.split(",");
				graph.addVertex(temp[0]);
				graph.setVisited(temp[0]);
				this.fileNames.put(temp[1],temp[0]);
				
				if(temp[8].equals("1"))
				{
					graph.setPageRank(temp[0], (double)(-1));
				}
				else if(temp[8].equals("0"))
				{
					graph.setPageRank(temp[0], (double)(1));
				}
			}
			
			while(true)
			{
				readLine = br.readLine();
				if(readLine == null)
				{
					break;
				}
				
				temp = readLine.split(",");
				graph.addEdge(temp[0], temp[1]);
				graph.addInDegree(temp[0], temp[1]);
			}
			
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//Calculates pageranks.
	public void pageRanks()
	{		
		LinkedList<String> vertices = graph.getVertices();
		
		double d = 0.85;
		
		for(int j=0; j<100; j++)
		{
			Iterator<String> k = vertices.iterator();
			
			while(k.hasNext())
			{
				String curNode = k.next();
				double pagerank = 1-d;
				
				if(graph.getPageRank(curNode) != -1.0)
				{
					//LinkedList<String> inDegrees = graph.getInDegrees(curNode);
					LinkedList<String> inDegrees = graph.getNeighbours(curNode);

					if(inDegrees != null)
					{				
						Iterator<String> iter = inDegrees.iterator();
						
						while(iter.hasNext())
						{
							String neighIn = iter.next();
							if(graph.getPageRank(neighIn) != -1.0)
							{
								//Two Different Formulas
								//First one is the one used by google to rank webpages according to their authority.
								//Second one is a slight modification of the first since the direction of arrows have different meanings
								//in file dependency graphs than they do in graph for the www.
								//pagerank += d*((graph.getPageRank(neighIn))/(graph.getOutDegree(neighIn)));
								pagerank += d*((graph.getPageRank(neighIn))/(graph.getInDegree(neighIn)));
							}
						}
					}	
					graph.setPageRank(curNode, pagerank);
				}
			}
		}
		
	}
	
	public void getPropValues() throws IOException
	{
		Properties prop = new Properties();
		String propFileName = "config.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if(inputStream != null)
		{
			prop.load(inputStream);
		}
		else
		{
			throw new FileNotFoundException("property file '"+propFileName+"' npt found in the classpath.");
		}

		inputStream.close();

		this.traceGdfLocation = prop.getProperty("traceGdfLocation");
		this.destinationFolderForAnalysedCommits = prop.getProperty("destinationFolderForAnalysedCommits");
		this.commitLogLocation = prop.getProperty("commitLogLocation");
		this.noOfCommitsToAnalyse = Integer.parseInt(prop.getProperty("noOfCommitsToAnalyse"));
		this.gitRepositoryLocation = prop.getProperty("gitRepositoryLocation");

	}
}