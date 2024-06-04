Programming language: JAVA (java version "21")
                                          
How to run the code:
1. Install jdk  21 version and Java env eclipse or Intellij 
2. Open the folder expense_8_puzzle_Assignment1 project                      
3. Open command prompt within the folder (where the expense_8_puzzle.java file is saved)
4. Write "javac expense_8_puzzle.java" in the command prompt to compile the project
5. To Run write "java expense_8_puzzle start.txt goal.txt a* True"
   Note: You can use any method from bfs,ucs, ids, greedy, dls, dfs or a* (default is a*)
          Set true or false for dump file (default is false)


Dump file is created with name tracelist.txt

How the code is structured:
expense_8_puzzle.java file contains source code of the project. main()is the entry point of the project. It will read the file name, method name and dump_flag from command line  and pass the value to its method where we decide which search function is called according to method passed by user.
   

Note: Class file, Project file and classpath file generated after compilation of the program.
 
