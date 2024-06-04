import java.util.*;

import java.io.*;

import java.lang.Math;

class expense_8_puzzle  {
	
	// puzzle state and its properties is defined in node class
    class NodeCL{  
    	
    	// Define 3x3 grid of the puzzle
        int [][] griddata = new int[3][3];
        
        // The total estimated cost 
        public int f_estimated_cost = -1;
        
        // The heuristic cost
        public int h_heuristic_cost = -1;
        
        // The actual cost 
        public int g_actual_cost = -1; 
        
        // A hash code that uniquely identifies node
        public int hashCode = -1;
        
       //constructor
        public NodeCL()
        {              
        }
        
        // It creates a new node with the same state
        public NodeCL(NodeCL ol_st)   
        {  
            int [][] nw_st_data = ol_st.getdata();
            this.set(nw_st_data);
            this.hashCode = this.hashCode();
        } 
        
      
        // This constructor creates a new instance of the 'Node' class with a provided puzzle state.
        public NodeCL(int[][] array)
        {
            this.set(array);
            this.hashCode = this.hashCode();
        }
        
               
        // This method updates the puzzle state of the current node with the provided two-dimensional array of integers.
        public void set(int[][] array)
        {
            for(int p=0;p<3;p++)
            {
                for(int q=0;q<3;q++)
                {
                	griddata[p][q] = array[p][q];
                }
            }
        }

       // Return grid data
        public int[][] getdata()
        {
            return this.griddata;
        }

        //Used to swap values
        public void swapValue(int x1, int y1, int x2, int y2)
        {
            int temp_val = this.griddata[x1][y1];
            this.griddata[x1][y1] = this.griddata[x2][y2];
            this.griddata[x2][y2] = temp_val;
        }

        //Get the value of co-ordinatesx,y
        public int getValue(int x, int y)
        {
            return this.griddata[x][y];
        }

        @Override
        //overrides equals() of object class 
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            NodeCL node = (NodeCL) obj;
            return Arrays.deepEquals(this.griddata, node.getdata());
        }        
      
        
        @Override        
        // Override hashcode() method of object class 
        public int hashCode()
        {
            int num = 0;
            for(int p=0;p<3;p++)
            {
                for(int q=0;q<3;q++)
                {
                    num *= 10;
                    num += this.griddata[p][q];
                }
            }

            this.hashCode = Objects.hash(num);
         
            return this.hashCode;
        }

        // Print 3*3 grid in string format       
        public String printPuzzle() {
            StringBuilder str = new StringBuilder();
            
            for (int[] row : this.griddata) {
                for (int item : row) {
                    str.append(item);
                }
                str.append('\n');
            }
            
            return str.toString();
        }


        @Override
        // Returns a string representation of the 3*3 grid    
        public String toString() {
            StringBuilder str = new StringBuilder("{[");
            
            for (int p = 0; p < 3; p++) {
                str.append("[");
                for (int q = 0; q < 3; q++) {
                    str.append(this.griddata[p][q]).append(" ");
                }
                str.append("],");
            }
            
            str.append("], f_estimated_cost = ").append(f_estimated_cost)
               .append(", g_actual_cost = ").append(g_actual_cost)
               .append(", h_heuristic_cost = ").append(h_heuristic_cost)
               .append("}\n");
            
            return str.toString();
        }


        // Find the value in 3*3 grid and returns its position
        public int find(int position)
        {
            for(int p=0;p<3;p++)
            {
                for(int q=0;q<3;q++)
                {
                    if(this.griddata[p][q] == position)
                        return p*3+q;
                }
            }
            return -1;
        }

        // It calculate heuristic value of the current node taking the goal parameter as input.Greedy and a* are informed searches use heuristic
        public int heuristicCalculation(NodeCL goal)
        {
        	
            if(this.h_heuristic_cost>=0) return this.h_heuristic_cost;
            this.h_heuristic_cost = 0;
            for(int p=0;p<9;p++)
            {
                int current_position = this.find(p);
                int current_x = current_position/3, current_y = current_position%3;             
                int goal_position = goal.find(p);
                int goal_x = goal_position/3, goal_y = goal_position%3;

                this.h_heuristic_cost += p*(Math.abs(current_x-goal_x)+Math.abs(current_y-goal_y));
            }
            return this.h_heuristic_cost;
        }
    }

    // newState generates new states by moving the 0 tile in different directions    
    // dir denotes direction of the step, where 0 is up,1 is down, 2 is right, and 3 is left.
    NodeCL newState(NodeCL stnode, int dir) 
    {
        int replace_blank = stnode.find(0);        
        NodeCL new_position = new NodeCL(stnode);
        if(!moveCorrect(stnode,dir)) return new_position;
        int new_x1 = replace_blank/3 , new_y1 = replace_blank%3;
        int new_x2 = new_x1, new_y2 = new_y1;
        if(dir==0) new_x2 = new_x1 + 1;
        else if(dir==1) new_x2 = new_x1 - 1;
        else if(dir==2) new_y2 = new_y1 - 1;
        else if(dir==3) new_y2 = new_y1 + 1;

        new_position.swapValue(new_x1,new_y1,new_x2,new_y2);
        
        return new_position;
    }
     
    // indicates whether the move in a particular direction is valid 
    boolean moveCorrect(NodeCL stnode, int dir)
    {
        int replace_blank = stnode.find(0);
        int new_x1 = replace_blank/3 , new_y1 = replace_blank%3;
        int new_x2 = new_x1, new_y2 = new_y1;
        if(dir==0) new_x2 = new_x1 + 1;
        else if(dir==1) new_x2 = new_x1 - 1;
        else if(dir==2) new_y2 = new_y1 - 1;
        else if(dir==3) new_y2 = new_y1 + 1;

        return new_x2<3 && new_x2>=0 && new_y2>=0 && new_y2<3;
    }

 // This function is used to decide which step to take from current position for next state
 // It find the 0 position in node unvisited and find the position of 0 visited.
 // Now find difference in both position and then decide next move.
    public int selectMove(NodeCL unvisited, NodeCL visited)
    {
        int replace_blank = unvisited.find(0);
        int new_x1 = replace_blank/3 , new_y1 = replace_blank%3;
        int replace_blank2 = visited.find(0);
        int new_x2 = replace_blank2/3 , new_y2 = replace_blank2%3;
       
        int dir = (new_x2 == new_x1 + 1) ? 0 : (new_x2 == new_x1 - 1) ? 1 : (new_y2 == new_y1 - 1) ? 2 : (new_y2 == new_y1 + 1) ? 3 : 4;

     
        int totalcost = unvisited.getValue(new_x2,new_y2);

        return totalcost*10 + dir;
    }
     
    void pathDescription(List<NodeCL> depth) {
        printSearchStatistics();
        if (dump_flag) {
            printSearchStatisticsToFile();
        }

        NodeCL currentNode = this.startGrid;
        if (dump_flag) {
            pw.println(currentNode.printPuzzle());
        }

        String[] moves = {"Up", "Down", "Right", "Left", "Unknow"};
        int totalCost = 0;

        for (NodeCL nextNode : depth) {
            if (!currentNode.equals(nextNode)) {
                int moveValue = selectMove(currentNode, nextNode);
                printMoveInformation(moveValue, moves[moveValue % 10]);
                if (dump_flag) {
                    pw.println("Move " + moveValue / 10 + " to " + moves[moveValue % 10]);
                    pw.println(nextNode.printPuzzle());
                }
                totalCost += moveValue / 10;
            }
            currentNode = nextNode;
        }

        if (depth.size() <= 1) {
            printSolutionNotFound();
        } else {
            printSolutionFoundAtDepth(depth.size() - 1, totalCost);
        }
    }

    void printSearchStatistics() {
        System.out.println("Nodes Popped: " + poppedNode);
        System.out.println("Nodes Expanded: " + expandedNode);
        System.out.println("Nodes Generated: " + generatedNode);
        System.out.println("Maximum Fringe Size: " + fringe_size);
    }

    void printSearchStatisticsToFile() {
        pw.println("Nodes Popped: " + poppedNode);
        pw.println("Nodes Expanded: " + expandedNode);
        pw.println("Nodes Generated: " + generatedNode);
        pw.println("Maximum Fringe Size: " + fringe_size);
    }

    void printMoveInformation(int moveValue, String moveName) {
        System.out.println("Move " + moveValue / 10 +" "+ moveName);
    }

    void printSolutionNotFound() {
        System.out.println("Solution not found");
        if (dump_flag) {
            pw.println("Solution not found");
        }
    }

    void printSolutionFoundAtDepth(int depth, int totalCost) {
        System.out.println("Solution found at depth " + depth + " with cost of " + totalCost);
        if (dump_flag) {
            pw.println("Solution found at depth " + depth + " with cost of " + totalCost);
        }
    }
 
    

    boolean dfs(NodeCL unvisted, HashMap<NodeCL,Boolean> expanded, List<NodeCL> depth, int max_depth)
    {
        if(unvisted.equals(this.goalGrid))
        {
            if(dump_flag)
                pw.println("Solution found");
            return true;
        }
        if(depth.size()> max_depth) return false;
        
        if(depth.size()>= this.fringe_size)
        	fringe_size = depth.size();
        if(dump_flag)
        {
            pw.println("Visiting Node : "+unvisted);
        }

        expanded.put(unvisted,true);
        if(dump_flag)
        {
            pw.println("Current Stack (size = "+ depth.size() +"): ");
            pw.print("   [");
            if(depth.size()>15)
            {
                for(int i=0;i<10;i++)
                    pw.print("\t\t"+depth.get(i));
                for(int i=0;i<3;i++)
                    pw.print("\t\t\t\t\t....\n");
                for(int i=0;i<5;i++)
                    pw.print("\t\t"+depth.get(depth.size()-6+i));
            }
            else{
                for(NodeCL stnode : depth)
                    pw.print("\t\t"+stnode);
            }
            pw.println("   ]");
        }

        boolean flag = false;
        Random random = new Random();
        int random_num = random.nextInt(4);
        for(int i=0;i<4;i++)
        {
            if(moveCorrect(unvisted,(i+random_num)%4))
            {
            	NodeCL visited = newState(unvisted,(i+random_num)%4);
                this.expandedNode++;
                if(! expanded.containsKey(visited) || !expanded.get(visited))
                {
                	visited.g_actual_cost = unvisted.g_actual_cost + 1;
                    depth.add(visited);
                    if(dump_flag)
                        pw.println("\tNode inserted : "+ visited);
                    this.generatedNode++;
                    flag |= dfs(visited,expanded,depth,max_depth);
                    if(flag) return true;
                    else
                    {
                    	depth.remove(visited);
                        expanded.put(visited,false);
                        if(dump_flag)
                            pw.println("\tNode removed : "+unvisted);
                        this.poppedNode++;
                    }
                }
            } 
        }

        return flag;
    }

    List<NodeCL> dfs()
    {
    	//Depth first search is being implemented here as a Stack where elements are added at the last and popped from the front
        HashMap<NodeCL,Boolean> expanded=new HashMap<NodeCL,Boolean>();
        HashMap<NodeCL,NodeCL> mainNode = new HashMap<NodeCL,NodeCL>();

        Stack<NodeCL> stacknode = new Stack<NodeCL>();

        stacknode.push(this.startGrid);
        this.generatedNode ++;
        expanded.put(startGrid,true);
        mainNode.put(startGrid,null);

        while(! stacknode.isEmpty())
        {
            if(dump_flag)
            {
            	//Keeps track of all the nodes that exist
                pw.println("Current Stack (size = "+stacknode.size()+"): ");
                pw.print("   [");
                if(stacknode.size()>15)
                {
                    for(int p=0;p<10;p++)
                        pw.print("\t\t"+stacknode.get(p));
                    for(int p=0;p<3;p++)
                        pw.print("\t\t\t\t\t....\n");
                    for(int p=0;p<5;p++)
                        pw.print("\t\t"+stacknode.get(stacknode.size()-6+p));
                }
                else{
                    for(NodeCL stnode : stacknode)
                        pw.print("\t\t"+stnode);
                }
                pw.println("   ]");
            }
            NodeCL unvisted = stacknode.pop();
            this.poppedNode++;
            if(dump_flag)
            {
            	//Keeps track of all the nodes that are being explored
                pw.println("Visiting Node : "+ unvisted);
            }

            expanded.put(unvisted,true);

            if(unvisted.equals(this.goalGrid))
            {
                if(dump_flag)
                    pw.println("Solution found");
                break;
            }
            
            Random random = new Random();
            int random_num = random.nextInt(4);
            for(int p=0;p<4;p++)
            {
                if(moveCorrect(unvisted,(p+random_num)%4))
                {
                	NodeCL visited = newState(unvisted,(p+random_num)%4);
                    this.expandedNode++;
                    if(!expanded.containsKey(visited) || !expanded.get(visited))
                    {
                        if(dump_flag)
                            pw.println("\tSuccessor node inserted : "+ visited);

                        visited.g_actual_cost = unvisted.g_actual_cost  +1;
                        stacknode.push(visited);
                        if(stacknode.size()>this.fringe_size)
                        	fringe_size = stacknode.size();
                        this.generatedNode++;
                        mainNode.put(visited,unvisted);
                    }
                } 
            }
        }
        
        return generateDepthList(mainNode);
        
    }

    // Method called for  A* BFS Greedy and UCS
    List<NodeCL> aStarBfsGreedyUcs(String method)
    {
    	
        HashMap<NodeCL,Boolean> expanded=new HashMap<NodeCL,Boolean>();
        HashMap<NodeCL,NodeCL> mainNode = new HashMap<NodeCL,NodeCL>();
        PriorityQueue<NodeCL> priorityqueue = new PriorityQueue<>(100000, Comparator.comparingInt(n -> n.f_estimated_cost));

        if (method.equals("a*") || method.equals("greedy")) {
            startGrid.g_actual_cost = 0;
            startGrid.h_heuristic_cost = startGrid.heuristicCalculation(goalGrid);
        } else if (method.equals("ucs") || method.equals("bfs")) {
            startGrid.g_actual_cost = 0;
            startGrid.h_heuristic_cost = 0;
        }

        priorityqueue.add(this.startGrid);
        generatedNode++;
        expanded.put(startGrid,true);
        mainNode.put(startGrid,null);

        while(! priorityqueue.isEmpty())
        {
            if(dump_flag)
            {
                pw.println("Current Queue (size = "+priorityqueue.size()+"): ");
                pw.print("   [");
                int x = 0;
                for(NodeCL nodeState : priorityqueue)
                {
                    pw.print("\t\t"+nodeState);
                    if(x++ > 20) break;
                }
                pw.println("   ]");
            }
            NodeCL nodeUnvisited = priorityqueue.poll();
            poppedNode++;
            if(dump_flag)
            {
                pw.println("Visiting Node : "+nodeUnvisited);
            }

            expanded.put(nodeUnvisited,true);

            if(nodeUnvisited.equals(this.goalGrid))
            {
                if(dump_flag)
                    pw.println("Solution found");
                break;
            }
            
            Random random = new Random();
            int random_num = random.nextInt(4);
            for(int p=0;p<4;p++)
            {
                if(moveCorrect(nodeUnvisited,(p+random_num)%4))
                {
                	NodeCL nodeVisited = newState(nodeUnvisited,(p+random_num)%4);
                    this.expandedNode++;
                    if(!expanded.containsKey(nodeVisited) || !expanded.get(nodeVisited))
                    {
                        if(dump_flag)
                        	// The successors inserted to the queue
                            pw.println("\tSuccessor node inserted to queue : "+nodeVisited);
                        if(method.equals("a*"))
                        {
                        	// We are determining which move to take.
                        	nodeVisited.g_actual_cost = nodeUnvisited.g_actual_cost + selectMove(nodeUnvisited,nodeVisited)/10;
                        	nodeVisited.h_heuristic_cost = nodeVisited.heuristicCalculation(this.goalGrid);
                        	nodeVisited.f_estimated_cost = nodeVisited.h_heuristic_cost + nodeVisited.g_actual_cost;
                        }
                        else if(method.equals("greedy"))
                        {
                        	nodeVisited.g_actual_cost = 0;
                        	nodeVisited.h_heuristic_cost = nodeVisited.heuristicCalculation(this.goalGrid);
                        	nodeVisited.f_estimated_cost = nodeVisited.h_heuristic_cost + nodeVisited.g_actual_cost;
                        }
                        else if(method.equals("ucs"))
                        {
                        	nodeVisited.g_actual_cost = nodeUnvisited.g_actual_cost + selectMove(nodeUnvisited,nodeVisited)/10;
                        	nodeVisited.h_heuristic_cost = 0;
                        	nodeVisited.f_estimated_cost = nodeVisited.h_heuristic_cost + nodeVisited.g_actual_cost;
                        }
                        else if(method.equals("bfs"))
                        {
                        	nodeVisited.g_actual_cost = nodeUnvisited.g_actual_cost + 1;
                        	nodeVisited.h_heuristic_cost = 0;
                        	nodeVisited.f_estimated_cost = nodeVisited.h_heuristic_cost + nodeVisited.g_actual_cost;
                        }
                        
                        priorityqueue.add(nodeVisited);
                        if(priorityqueue.size()>this.fringe_size)
                        	fringe_size = priorityqueue.size();
                        generatedNode++;
                        mainNode.put(nodeVisited,nodeUnvisited);
                    }
                } 
            }
        }       
        
        return generateDepthList(mainNode);
    }
    
    List<NodeCL> generateDepthList(HashMap<NodeCL, NodeCL> mainNode) {
    	NodeCL temp_node = this.goalGrid;
        List<NodeCL> depth = new ArrayList<>();
        while (temp_node != null) {
            depth.add(0, temp_node);
            temp_node = mainNode.get(temp_node);
        }
        return depth;
    }

    public void solvePuzzle(String method, boolean dump_flag, String file_name)
    {
        // Open trace file.
        if(dump_flag)
            try{   
            	this.pw = new PrintWriter(file_name);
           
            }catch(Exception ex)
            {
                ex.printStackTrace();
         
            }
        this.dump_flag = dump_flag;
        if(method.equals("bfs") || method.equals("BFS"))
        {
        	// If method mentioned is BFS 
            System.out.println("Breadth First Search :-");
            List<NodeCL> depth = aStarBfsGreedyUcs("bfs");
            pathDescription(depth);
        }
        else if(method.equals("dfs") || method.equals("DFS"))
        {
        	// If method mentioned is DFS 
            System.out.println("Depth first search :-");
            List<NodeCL> depth = dfs();
            pathDescription(depth);
        }
        else if(method.equals("ids") || method.equals("IDS"))
        {
        	// If method mentioned is Iterative deepening search 
            System.out.println("Iterative deepening search :-");
            for(int p=0;p<100;p++)
            {
                List<NodeCL>depth = new ArrayList<NodeCL>();
                depth.add(startGrid);
                if(dump_flag)
                    pw.println("Iterative deepning with max_depth = "+p);
                boolean found = dfs(startGrid,new HashMap<NodeCL,Boolean>(),depth,p);
                if(found)
                {
                	pathDescription(depth);
                    break;
                }
            }
        }
        else if(method.equals("dls") || method.equals("DLS"))
        {
        	// If method mentioned is Depth limit search 
            System.out.println("Depth limit search :-");
            System.out.print("Enter the maximum depth limit more than 12 for solution : ");
            BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

            int lt = 0;
            try{
                lt = Integer.parseInt(reader.readLine());
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
            List<NodeCL>depth = new ArrayList<NodeCL>();
            depth.add(startGrid);
            dfs(startGrid,new HashMap<NodeCL,Boolean>(),depth,lt);
            pathDescription(depth);
        }
        else if(method.equals("a*") || method.equals("A*"))
        {
        	// If method mentioned is a* 
            System.out.println("A* :-");
            List<NodeCL> depth = aStarBfsGreedyUcs("a*");
            pathDescription(depth);
        }
        else if(method.equals("greedy") || method.equals("GREEDY"))
        {
        	// If method mentioned is greedy 
            System.out.println(" Greedy search :-");
            List<NodeCL> depth = aStarBfsGreedyUcs("greedy");
            pathDescription(depth);
        }
        else if(method.equals("ucs") || method.equals("UCS"))
        {
        	// If method mentioned is UCS 
            System.out.println("Uniform Cost Search :-");
            List<NodeCL> depth = aStarBfsGreedyUcs("ucs");
            pathDescription(depth);
        }
        else
        {
        	//If no method is mentioned then A*  perform search
            System.out.println("A* chosen :-");
            List<NodeCL> depth = aStarBfsGreedyUcs("a*");
            pathDescription(depth);
        }

        if(dump_flag)
            this.pw.close();
    }

    NodeCL readAndCreateNode(String file_name)
    {
        Scanner scanner = null;
        try{
        	scanner = new Scanner(new BufferedReader(new FileReader(file_name)));
        }catch(Exception ex)
        {
            ex.printStackTrace();
        } 
      int rw = 3;
      int col = 3;
      int [][] arr = new int[rw][col];
      while(scanner.hasNextLine()) {
         for (int p=0; p<arr.length; p++) {
            String[] line = scanner.nextLine().trim().split(" ");
            if(line[0].equals("END") && line[1].equals("OF") && line[2].equals("FILE"))
            {
                break;
            }
            for (int q=0; q<line.length; q++) {
               arr[p][q] = Integer.parseInt(line[q]);
            }
         }
      }
      return new NodeCL(arr);
    }

    
    // Read start.txt and goal.txt and store value in start and goal variable
    public void readFiles(String start_file, String goal_file)
    {   
    
        this.startGrid = readAndCreateNode(start_file);       
   
        this.goalGrid = readAndCreateNode(goal_file);
    }
    
    // Create nodes represent the start and goal states
    private NodeCL startGrid, goalGrid;
    
    // Boolean dump_flag denotes trace file should be generated or not
    boolean dump_flag = false;
    
    // Create PrintWriter object to write
    PrintWriter pw; 

    // Created variable to track number of nodes generated
    int generatedNode = 0;
    
    // Created variable to track number of nodes stored
    int fringe_size = 0;
    
    // Created variable to track number of nodes popped
    int poppedNode = 0;
    
    // Created variable to track number of nodes visited
    int expandedNode = 0;

    //The main method is the entry point of the program
    public static void main (String[] args) {
    	
    	//Instance created for expense_8_puzzle class
    	expense_8_puzzle  expense8puzzle = new expense_8_puzzle();
        
        //Read the start and goal states from the files
    	expense8puzzle.readFiles(args[0],args[1]);

        //Set the default to "a*" and update it if a any other algorithm mentioned in cmd
    	String method = (args.length > 2) ? args[2] : "a*";
        
        //Set the dump_flag as false by default and update it if mentioned in cmd.
        boolean dump_flag = (args.length > 3) ? Boolean.parseBoolean(args[3]) : false;

        String file_name ="tracelist.txt";
        
        //Calling method solvePuzzle to get the goal state
        expense8puzzle.solvePuzzle(method,dump_flag,file_name);        
       
    }
}