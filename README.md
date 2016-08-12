# CommitAnalysis

This repository contains the code that cycles over commits within a given range, rebuilds the software and analyses the build dependencies.

# How To Use:

1. Downlaod and install MAKAO and the guess graph exploration software by following the instructions on http://mcis.polymtl.ca/makao.html.
2. Clone this repository.
3. Place the most recent trace.gdf file created using MAKAO in the build folder.
4. In the config.properties file update the locations as instructed in the file.
5. Using the terminal change directory to the cloned repository folder.
6. To properly run MAKAO you need a working version of java 6. For instructions on how to install java 6 use Google.
7. Then open a terminal session and export the following variables:
    i) JAVA_HOME=/location_of_java6_folder/
    ii) MAKAO=/location_of_makao_folder/
    iii) GUESS_HOME=/location_of_guess_folder/

8. Run the command javac *.java
9. Run the command java CommitAnalysis
