# CommitAnalysis

This repository contains the code that cycles over commits within a given range, rebuilds the software and analyses the build dependencies.

# How To Use:

1. Clone the repository.
2. Place the most recent trace.gdf file created using MAKAO in the build folder.
3. In the config.properties file update the locations as instructed in the file.
4. Using the terminal change directory to the cloned repository folder.
5. Run the command javac *.java
6. Run the command java CommitAnalysis
