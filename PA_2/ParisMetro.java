// Paris (P2- Part B)
// Startup code given in the Fall 2025 for csi2110/csi2510
// This file only contains basic commands to read the data from the input file 'metro.txt'
// Use and modify it freely
// 
// Do not forget to add your name and student number on each program file you submit
//

import java.io.*;
import java.util.*;
import java.util.Scanner;


public class ParisMetro {

    static class NewVert {
        int start;
        int end;

        // Constructor
        NewVert(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    // newStation class
    static class NewStation implements Comparable<NewStation> {
        int station1;
        int station2;
        int station3;

        // Constructor
        NewStation(int station1, int station2, int station3) {
            this.station1 = station1;
            this.station2 = station2;
            this.station3 = station3;
        }
        public int newInterlink(NewStation station) {
            return this.station3 - station.station3;
        }

        @Override
        public int compareTo(NewStation side) {
            return this.station3 - side.station3;
        }
    }

    static class Node implements Comparable<Node> {
        int left_side;
        int right_side;
        Node(int left_side, int right_side) {
            this.left_side = left_side;
            this.right_side = right_side;
        }
        public int Terminal(Node newNode) {
            return this.left_side - this.right_side;
        }

        @Override
        public int compareTo(Node parent) {
            return this.right_side - parent.right_side;
        }
    }

    // Disjoint-set structure
    // Kruskal's MST
    static class Partition {
        int[] mainNode;

        Partition(int part) {
            mainNode = new int[part];
            for(int i = 0; i < part; i++) {
                mainNode[i] = i;
            }
        }

        int location(int newLoc) {
            if(mainNode[newLoc] == newLoc) {
                return newLoc;
            }
            return location(mainNode[newLoc]);
        }

        void lockPart(int lock1, int lock2) {
            int node1 = location(lock1);
            int node2 = location(lock2);

            if(node1 != node2) {
                mainNode[node2] = node1;
            }
        }
    }

    // Helper method
    static ArrayList<Node> ComPoint(int pair, int edge, ArrayList<Integer> algo, int[] line, ArrayList<Node> newNode) {
        return new ArrayList<Node>();
    }

    /*

    This is the main method

     */
    public static void main (String[] args) {
        Scanner newScanner = new Scanner(System.in);

        int left_node = newScanner.nextInt();
        int right_node = newScanner.nextInt();

        System.out.println("Total vertices: " + left_node + ", " + "Total Edges: " + right_node);

        // Creating a new array
        int[] platform = new int[left_node];
        String[] platformNumber = new String[left_node];
        Map<Integer, Integer> trainOnPlat = new HashMap<>();

        for(int i = 0; i < left_node; i++) {
            int tickets = newScanner.nextInt();
            String trainName = newScanner.nextLine();
            trainName = trainName.trim();
            platform[i] = tickets;
            platformNumber[i] = trainName;
            trainOnPlat.put(tickets, i);
        }

        String trainConnectivity = newScanner.next().trim();

        // Creating an Adjacency list for original graph
        ArrayList<NewVert>[] trainInfo = (ArrayList<NewVert>[]) new ArrayList[left_node];
        for(int i = 0; i < left_node; i++) {
            trainInfo[i] = new ArrayList<>();
        }

        // Creating a 2D array to store all edges
        int[][] trainTiming = new int[right_node][3];

        for(int i = 0; i < right_node; i++) {
            int departure = newScanner.nextInt();
            int arrival = newScanner.nextInt();
            int destination = newScanner.nextInt();
            int timing = trainOnPlat.get(departure);
            int date = trainOnPlat.get(arrival);

            trainTiming[i][0] = timing;
            trainTiming[i][1] = date;
            trainTiming[i][2] = destination;

            if(destination > 0) {
                trainInfo[timing].add(new NewVert(date, destination));
            }
        }

        newScanner.close();

        // Partition instance
        Partition newStat = new Partition(left_node);

        for(int i = 0; i < right_node; i++) {
            int location = trainTiming[i][0];
            int date = trainTiming[i][1];
            int destination = trainTiming[i][2];
            if (destination == -1) {
                newStat.lockPart(location, date);
            }
        }

        int[] main = new int[left_node];
        for(int i = 0; i < left_node; i++) {
            main[i] = newStat.location(i);
        }

        int[] trainBox = new int[left_node];
        for(int i = 0; i < left_node; i++) {
            trainBox[main[i]]++;
        }

        int[] transmittion = new int[left_node];
        Arrays.fill(transmittion, -1);
        int sub = 0;
        for(int i = 0; i < left_node; i++) {
            if(trainBox[i] >= 2) {
                transmittion[i] = sub;
                sub++;
            }
        }
        int newInt = sub;

        int[] connect = new int[left_node];
        Arrays.fill(connect, -1);
        int counter = 0;
        for(int i = 0; i < left_node; i++) {
            int j = main[i];
            if(transmittion[j] != -1) {
                connect[i] = transmittion[j];
                counter++;
            }
        }

        // Creating a new ArrayList
        ArrayList<Integer>[] fairSys = (ArrayList<Integer>[]) new ArrayList[newInt];
        for(int i = 0; i < newInt; i++) {
            fairSys[i] = new ArrayList<>();
        }

        for(int i = 0; i < left_node; i++) {
            if(connect[i] != -1) {
                fairSys[connect[i]].add(i);
            }
        }

        // Creating a new String array
        String[] expense = new String[newInt];
        for(int i = 0; i < newInt; i++) {
            int expensePrice = fairSys[i].get(0);
            expense[i] = platformNumber[expensePrice];
        }

        // Print statement
        System.out.println("Hub Stations = [ ");
        for(int i = 0; i < newInt; i++) {
            if(i > 0) {
                System.out.println(", ");
            }
            System.out.println(expense[i]);
        }
        System.out.println(" ]");
        System.out.println("Number of Hub Stations = " + newInt + " (total Hub Vertices = " + counter + ")");

        int newPart = 1000000000;

        int[][] choose = new int[newInt][newInt];

        for(int i = 0; i < newInt; i++) {
            Arrays.fill(choose[i], newPart);
        }

        for(int i = 0; i < newInt; i++) {
            int[] out = new int[left_node];
            boolean[] min = new boolean[left_node];

            for(int d = 0; d < left_node; d++) {
                out[d] = newPart;
                min[d] = false;
            }

            // Creating a new Priority Queue
            PriorityQueue<Node> newPriorityQueue = new PriorityQueue<>();

            for(int j = 0; j < fairSys[i].size(); j++) {
                int t = fairSys[i].get(j);
                out[t] = 0;
                newPriorityQueue.add(new Node(t, 0));
            }

            // A while loop for the Dijkstra algorithm
            while(!newPriorityQueue.isEmpty()) {
                Node newNode = newPriorityQueue.poll();
                int newIntOne = newNode.left_side;
                int newIntTwo = newNode.right_side;

                if (!min[newIntOne]) {
                    min[newIntOne] = true;

                    ArrayList<NewVert> newAL = trainInfo[newIntOne];
                    for (int o = 0; o < newAL.size(); o++) {
                        NewVert way = newAL.get(o);
                        int para = way.start;
                        int clear = way.end;

                        if (connect[para] == -1 || connect[para] == i) {
                            int newCase = newIntTwo + clear;
                            if (newCase < out[para]) {
                                out[para] = newCase;
                                newPriorityQueue.add(new Node(para, newCase));
                            }
                        } else {
                            int path = connect[para];
                            if (path != i) {
                                int pass = newIntTwo + clear;
                                if (pass < choose[i][path]) {
                                    choose[i][path] = pass;
                                    choose[path][i] = pass;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Creating a new ArrayList
        ArrayList<NewStation> lines = new ArrayList<NewStation>();
        for(int i = 0; i < newInt; i++) {
            for(int j = i + 1; j < newInt; j++) {
                if(choose[i][j] < newPart) {
                    lines.add(new NewStation(i, j, choose[i][j]));
                }
            }
        }

        // Print Statement
        System.out.println("Number of Possible Segments = " + lines.size());

        // Using Kruskal's algorithm to sort all the segments by their cost
        Collections.sort(lines);

        Partition process = new Partition(newInt);

        ArrayList<NewStation> operate = new ArrayList<NewStation>();

        int network = 0;
        int implement = 0;

        for(int i = 0; i < lines.size(); i++) {
            NewStation newLines = lines.get(i);
            int newInt1 = process.location(newLines.station1);
            int newInt2 = process.location(newLines.station2);

            if(newInt1 != newInt2) {
                process.lockPart(newInt1, newInt2);
                operate.add(newLines);
                network += newLines.station3;
                implement++;

                if(implement == newInt - 1) {
                    break;
                }
            }
        }

        if(implement != newInt - 1) {
            System.out.println("Impossible");
        } else {
            System.out.println("Total Cost = $" + network);
            System.out.println("Segments to Buy:");
            for(int i = 0; i < operate.size(); i++) {
                NewStation newEdge = operate.get(i);
                String directPath1 = expense[newEdge.station1];
                String directPath2 = expense[newEdge.station2];
                System.out.println((i + 1) + "( " + directPath1 + " - " + directPath2 + " ) - $" + newEdge.station3);
            }
        }
    }

    /*

    This is the readMetro method

     */
    public static void readMetro()
    {

        System.out.println("\nStart Reading Metro");
        Scanner scan = new Scanner(System.in); 

        int n = scan.nextInt(); // number of vertices
        int m = scan.nextInt(); // number of edges
        System.out.println("Paris Metro Graph has "+n+" vertices and "+m+" edges.");
        

        for (int i=0; i <n; i++)
        {   int vertexNumber=Integer.valueOf(scan.next()); // vertex number (unique, example: 0016)
            String stationName=scan.nextLine(); 	    // vertex name (not unique, example: Bastille)
            //System.out.println(vertexNumber+" "+stationName);
        }

        scan.nextLine();//read the $ sign 

        for (int i=0; i <m; i++)
        {
            int v1=scan.nextInt(); int v2=scan.nextInt(); int weight=scan.nextInt(); // edge information
            //System.out.println("v1="+v1+" v2="+v2+" weight="+weight);
  
        }

       System.out.println("End Reading Metro\n");
    }


}
