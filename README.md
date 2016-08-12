# CommitAnalysis

This repository contains the code that cycles over commits within a given range, rebuilds the software, calculates the pageranks and analyses the build dependencies.

# How To Use:

1. Downlaod and install MAKAO and the guess graph exploration software by following the instructions on http://mcis.polymtl.ca/makao.html.
2. Clone this repository.
3. Place the most recent trace.gdf file created using MAKAO in the build folder.
4. In the config.properties file update the locations as instructed in the file.
5. Using the terminal change directory to the cloned repository folder.
6. To properly run MAKAO you need a working version of java 6. For instructions on how to install java 6 use Google.
7. Then open a terminal session and export the following variables:
    1. JAVA_HOME=/location_of_java6_folder/
    2. MAKAO=/location_of_makao_folder/
    3. GUESS_HOME=/location_of_guess_folder/
8. Run the command javac *.java
9. Run the command java CommitAnalysis

# Notes
1. This tool utilises the trace.gdf file produced by MAKAO to create a graph using HashMaps which can then easily be used to calculate pageranks and also to trace down all intermediate and deliverable files affected by a changing a particular file.
2. The output can be of three types: 
    1. If only source code files are changed then the output text file will contain the names of the targets modified during that commit followed by all the files that were impacted along with their pageranks.
    2. If only build files were changed the tool will reset the build of the software to that configuration and the output text file will read that 'only build files were changed during this commit'
    3. If both build and source files are changed the tool will reset the build to that configuration and the resulting text file will contain the names of the targets modified during that commit followed by all the files that were impacted along with their pageranks.
3. During my research I analysed the open source software called VTK(Visualization Toolkit) on Ubuntu version 16.04. The build of the software system depends on the OS running on the machine. The results of this tool for a linux based machine can be found under the folder Linux. 
4. It is recommended that the same analysis be carried out on an OS other than Linux e.g on a MAC. The results from a MAC were obtained but due to a shortage of time could not be analysed graphically. They can be found here 
https://www.dropbox.com/s/h9aq88z4utryuba/Results.zip?dl=0
5. Another suggested line of work would be to package these tools as a Gerrit plugin so that we can study change imapact at review time which will help improve our understanding of the same.

If you have any questions you can contact me by email at this address: owais.khan2@mail.mcgill.ca
