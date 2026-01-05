
// Expensive Subway problem (P2-part A)
// Startup code given in the Fall 2025 for csi2110/csi2510
// This file only contains basic commands to read the data from the input files.
// Use and modify it freely
// 
// Do not forget to add your name and student number on each program file you submit
//

import java.util.*;


class Main {

    // Edge class
    static class Edge implements Comparable<Edge> {
        int start;
        int middle;
        int end;

        Edge(int start, int middle, int end) {
            this.start = start;
            this.middle = middle;
            this.end = end;
        }

        // This method helps to sort edges
        public int compareTo(Edge newE) {
            return this.end - newE.end;
        }
    }

    // Disjoint-set class using Partition
    static class Partition {
        int[] mainNode;
        Partition(int part) {
            mainNode = new int[part];
            for(int i = 0; i < part; i++) {
                mainNode[i] = i;
            }
        }

        // This method utilizes Recursion
        int location(int newLoc) {
            if(mainNode[newLoc] == newLoc) {
                return newLoc;
            }
            return location(mainNode[newLoc]);
        }

        // This method handles the union of two sets
        void lockPart(int lock1, int lock2) {
            int node1 = location(lock1);
            int node2 = location(lock2);

            if(node1 != node2) {
                mainNode[node2] = node1;
            }
        }
    }

    // Using Kruskal's Algorithm to calculate the MST cost.
    static long costCalculation(int total, ArrayList<Edge> num) {
        Collections.sort(num);

        Partition calc = new Partition(total);

        long compute = 0;
        int newSub = 0;

        // For loop to go through edges
        for (int i = 0; i < num.size(); i++) {
            Edge newNetwork = num.get(i);

            int newCom = calc.location(newNetwork.start);
            int newWay = calc.location(newNetwork.middle);

            if (newCom != newWay) {
                calc.lockPart(newCom, newWay);
                compute += newNetwork.end;
                newSub++;

                if (newSub == total - 1) {
                    break;
                }
            }
        }

        if (newSub != total - 1) {
            return -1;
        } else {
            return compute;
        }
    }

    /*

    This is the main method

     */
    public static void main (String[] args) {

      Scanner scanner = new Scanner(System.in); // Note that for Online Judge the input must be given in the standard I/O
            // to read from input files use the command 'java Main < test1.txt', test1.txt should be in the same folder
                                

      int nv, ne; 


      while (true) { // This loop repeats for each problem that is included in the same input file (until 0 0 is read)

      	nv=scanner.nextInt(); // read number of vertices
      	ne=scanner.nextInt(); // read number of edges
        // System.out.println(nv +" " + ne);
        if ((nv==0) & (ne==0)) break; // if both are zero it is indication to stop 
        

        ArrayList<String> vertexNames=new ArrayList<String>(); // reading vertex names into an Array List
      	for (int v=0; v<nv; v++) {
          String vName = scanner.next(); // here a vertex name is read
          vertexNames.add(vName);  
          // System.out.println(vName);
      	}

        // you should do something here to add the vertices to the graph
          Map<String, Integer> newStat = new HashMap<>();

          for (int i = 0; i < nv; i++) {
              String newTic = vertexNames.get(i);
              newStat.put(newTic, i);
          }

          ArrayList<Edge> newVert = new ArrayList<Edge>();

      	for (int e=0; e<ne; e++) {
          String v1Name = scanner.next(); // a vertex name that is one end of the edge
          String v2Name = scanner.next(); // a vertex name that is the other end of the edge
          int weight=scanner.nextInt(); // edge weight is given
          // System.out.println(v1Name+" "+v2Name+" "+weight);
          // here you have a new edge information: {v1Name,v2Name} with weight 'weight'
            int newInt1 = newStat.get(v1Name);
            int newInt2 = newStat.get(v2Name);
          // you should do something here in order to add this edge to the graph
            newVert.add(new Edge(newInt1, newInt2, weight));
      	} 

        String home=scanner.next(); // here is the name of the vertex of Peter's home
        // System.out.println(home);
        long newFare = costCalculation(nv, newVert);
        // Below is a silly printout that no matter the input give the answer "Impossible"
        // You should substitute this to call your algorithms/methods that will produce the correct answer         
        if (newFare == -1) {
            System.out.println("Impossible");
        } else {
            System.out.println(newFare);
        }
      }
      scanner.close();

   }
}



