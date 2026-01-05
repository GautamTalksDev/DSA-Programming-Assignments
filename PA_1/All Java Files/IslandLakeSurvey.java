import java.io.BufferedInputStream;
import java.util.*;

public class IslandLakeSurvey {
    //Simple pair for a black (land) cell
    static class Island {
        int a;
        int b;

        Island(int a, int b) {
            this.a = a;
            this.b = b;
        }

    }

    //Simple pair for a white (water) cell
    static class IslandLake {
        int x;
        int y;

        IslandLake(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

        //4-neighbour (up, down, left, right)
        static final int[] sideThree = {-1, 1, 0, 0};
        static final int[] sideFour = {0, 0, -1, 1};

        //8-neighbor (this includes diagonals)
        static final int[] sideFive = {-1, -1, -1, 0, 0, 1, 1, 1};
        static final int[] sideSix = {-1, 0, 1, -1, 1, -1, 0, 1};

    //Survey
    private static void initialSurveyIslandLake(Partition<Island> newPart, Node<Island>[][] newNode, boolean[][] newIs, boolean newSurvey) {
        int S = newIs.length;
        int T = newIs[0].length;

        //This helps to map each island leader node to an id, and base sizes
        HashMap<Node<Island>, Integer> newMap = new HashMap<>();
        LinkedList<Integer> newList = new LinkedList<>();

        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(newIs[i][j]) {
                    Node<Island> landscape = newPart.find(newNode[i][j]);
                    Integer landOut = newMap.get(landscape);
                    if(landOut == null) {
                        landOut = newList.size();
                        newMap.put(landscape, landOut);
                        newList.add(0);
                    }
                    int parts = newList.get(landOut);
                    newList.set(landOut, parts + 1);

                }
            }
        }

        int flood = newList.size();

        //Lake area to be added for each island ID
        int[] floodLandscape;
        if(flood == 0) {
            floodLandscape = new int[1];
        } else {
            floodLandscape = new int[flood];
        }

        int counterOne = 0; //This tracks the number of lakes
        int counterTwo = 0; //This tracks the total lake ares

        //This helps to build white partition (8-connected)
        Partition<IslandLake> newPartLake = new Partition<>();
        Node<IslandLake>[][] newNodeLake = new Node[S][T];

        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(!newIs[i][j]) {
                    newNodeLake[i][j] = newPartLake.makeCluster(new IslandLake(i, j));
                }
            }
        }

        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(!newIs[i][j]) {
                    for(int k = 0; k < 8; k++) {
                        int newClusterOne = i + sideFive[k];
                        int newClusterTwo = j + sideSix[k];
                        if(newClusterOne >= 0 && newClusterOne < S && newClusterTwo >= 0 && newClusterTwo < T) {
                            if(!newIs[newClusterOne][newClusterTwo]) {
                                newPartLake.union(newNodeLake[i][j], newNodeLake[newClusterOne][newClusterTwo]);
                            }
                        }
                    }
                }
            }
        }

        //Note the size of each white component, whether it touches the border, and which island IDs it touches
        class SurveyLake {
            int count = 0;
            boolean lake = false;
            HashSet<Integer> newLake = new HashSet<>();
        }

        HashMap<Node<IslandLake>, SurveyLake> newIS = new HashMap<>();

        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(!newIs[i][j]) {
                    Node<IslandLake> newNodeLakeOne = newPartLake.find(newNodeLake[i][j]);
                    SurveyLake newSL = newIS.get(newNodeLakeOne);
                    if(newSL == null) {
                        newSL = new SurveyLake();
                        newIS.put(newNodeLakeOne, newSL);
                    }
                    newSL.count++;
                    if(i == 0 || j == 0 || i == S - 1 || j == T - 1) {
                        newSL.lake = true;
                    }

                    for(int k = 0; k < 4; k++) {
                        int newSurveyLakeOne = i + sideThree[k];
                        int newSurveyLakeTwo = j + sideFour[k];
                        if(newSurveyLakeOne >= 0 && newSurveyLakeOne < S && newSurveyLakeTwo >= 0 && newSurveyLakeTwo < T) {
                            if(newIs[newSurveyLakeOne][newSurveyLakeTwo]) {
                                Node<Island> newNodeIs = newPart.find(newNode[newSurveyLakeOne][newSurveyLakeTwo]);
                                Integer newInt = newMap.get(newNodeIs);
                                if(newInt != null) {
                                    newSL.newLake.add(newInt);
                                }
                            }
                        }
                    }
                }
            }
        }

        //If a white component touches exactly one island and does not touch the border, it is a lake
        Iterator<SurveyLake> newPos = newIS.values().iterator();
        while(newPos.hasNext()) {
            SurveyLake newLake = newPos.next();
            if(!newLake.lake) {
                if(newLake.newLake.size() == 1) {
                    Iterator<Integer> newItInt = newLake.newLake.iterator();
                    int area = newItInt.next();
                    if(area >= 0) {
                        if(area < flood) {
                            int areaFl = newLake.count;
                            counterOne++; //one more lake
                            floodLandscape[area] += areaFl; //add its area to that island
                            counterTwo += areaFl; //total lake area
                        }
                    }
                }
            }
        }

        //Final island sizes = base size + lake additions
        LinkedList<Integer> intLL = new LinkedList<>();
        int counter = 0; //This helps to keep track of total island area (including lakes)
        for(int i = 0; i < flood; i++) {
            int newCounter = newList.get(i) + floodLandscape[i];
            intLL.add(newCounter);
            counter += newCounter;
        }
        Collections.sort(intLL, Collections.reverseOrder());

        //Output
        System.out.println(intLL.size());
        if(intLL.size() == 0) {
            System.out.println("-1");
        } else {
            for(int i = 0; i < intLL.size(); i++) {
                if(i > 0) {
                    System.out.print(" ");
                }
                System.out.print(intLL.get(i));
            }
            System.out.println();
        }
        System.out.println(counter);
        System.out.println(counterOne);
        System.out.println(counterTwo);

        //Blank line
        if(!newSurvey) {
            System.out.println();
        }
    }

    /*
    The main method
     */
    public static void main(String[] args) {
        Scanner newScanner = new Scanner(new BufferedInputStream(System.in));

        int S = newScanner.nextInt();
        int T = newScanner.nextInt();

        //Read grid: '1' = black (land), '0' = white (water)
        boolean[][] newIs = new boolean[S][T];
        for(int i = 0; i < S; i++) {
            String node = newScanner.next();
            for(int j = 0; j < T; j++) {
                if(node.charAt(j) == '1') {
                    newIs[i][j] = true;
                } else {
                    newIs[i][j] = false;
                }
            }
        }

        //Number of phases
        int U = newScanner.nextInt();

        Partition<Island> newPart = new Partition<>();
        Node<Island>[][] newNode = new Node[S][T];

        //This helps to create a singleton node for each black cell
        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(newIs[i][j]) {
                    newNode[i][j] = newPart.makeCluster(new Island(i, j));
                }
            }
        }

        //Union black cells with 4-neighbors
        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(newIs[i][j]) {
                    for(int k = 0; k < 4; k++) {
                        int newNodeOne = i + sideThree[k];
                        int newNodeTwo = j + sideFour[k];
                        if(newNodeOne >= 0 && newNodeOne < S && newNodeTwo >= 0 && newNodeTwo<T) {
                            if(newIs[newNodeOne][newNodeTwo]) {
                                newPart.union(newNode[i][j], newNode[newNodeOne][newNodeTwo]);
                            }
                        }
                    }
                }
            }
        }

        if(U == 0) {
            initialSurveyIslandLake(newPart, newNode, newIs, true);
        } else {
            initialSurveyIslandLake(newPart, newNode, newIs, false);
        }

        for(int i = 0; i < U; i++) {
            int V = newScanner.nextInt();
            int[][] W = new int[V][2];
            for(int x = 0; x < V; x++) {
                W[x][0] = newScanner.nextInt();
                W[x][1] = newScanner.nextInt();
            }

            for(int y = 0; y < V; y++) {
                int newLakeOne = W[y][0];
                int newLakeTwo = W[y][1];
                if(!newIs[newLakeOne][newLakeTwo]) {
                    newIs[newLakeOne][newLakeTwo] = true;
                    newNode[newLakeOne][newLakeTwo] = newPart.makeCluster(new Island(newLakeOne, newLakeTwo));
                }
            }

            for(int z = 0; z < V; z++) {
                int a = W[z][0];
                int b = W[z][1];
                if(newIs[a][b]) {
                    for(int c = 0; c < 4; c++) {
                        int newLakeThree = a + sideThree[c];
                        int newLakeFour = b + sideFour[c];
                        if(newLakeThree >= 0 && newLakeThree < S && newLakeFour >= 0 && newLakeFour < T) {
                            if(newIs[newLakeThree][newLakeFour]) {
                                newPart.union(newNode[a][b], newNode[newLakeThree][newLakeFour]);
                            }
                        }
                    }
                }
            }

            if(i == U - 1) {
                initialSurveyIslandLake(newPart, newNode, newIs, true);
            } else {
                initialSurveyIslandLake(newPart, newNode, newIs, false);
            }
        }

        //Close
        newScanner.close();

    }
}
