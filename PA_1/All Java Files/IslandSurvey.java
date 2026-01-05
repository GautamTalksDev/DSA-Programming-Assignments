import java.io.BufferedInputStream;
import java.util.Scanner;

public class IslandSurvey {
    //This is the simple pair to store a black cell's coordinates
    static class Island {
        int a;
        int b;

        Island(int a, int b) {
            this.a = a;
            this.b = b;
        }

    }

    //Print one survey
    //If list == false, print a blank line after.
    private static void initialSurvey(Partition<Island> newPart, boolean list) {
        java.util.List<Integer> newList = newPart.clusterSizes();
        int newInt = newList.size();
        int counter = 0;
        for(int i = 0; i < newList.size(); i++) {
            counter += newList.get(i);
        }

        System.out.println(newInt);

        if(newInt == 0) {
            System.out.println("-1");
        } else {
            for(int i = 0; i < newList.size(); i++) {
                if(i > 0) {
                    System.out.print(" ");
                }
                System.out.print(newList.get(i));
            }
            System.out.println();
        }

        System.out.println(counter);
        if(!list) {
            System.out.println();
        }
    }
        // 4-neighborhood directions (row/col deltas)
        static final int[] sideOne = {-1, 1, 0, 0};
        static final int[] sideTwo = {0, 0, -1, 1};

    /*
    The main method
     */
    public static void main(String[] args) {
        Scanner newScanner = new Scanner(new BufferedInputStream(System.in));

        int S = newScanner.nextInt();
        int T = newScanner.nextInt();

        //Grid: '1' = black land, '0' = white water
        boolean[][] land = new boolean[S][T];
        for(int i = 0; i < S; i++) {
            String rows = newScanner.next();
            for(int j = 0; j < T; j++) {
                if(rows.charAt(j) == '1') {
                    land[i][j] = true;
                } else {
                    land[i][j] = false;
                }
            }
        }

        //number of phases
        int newSide = newScanner.nextInt();

        Partition<Island> newPart = new Partition<>();
        Node<Island>[][] newNode = new Node[S][T];

        //This loop helps to make a singleton for each black cell
        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(land[i][j]) {
                    newNode[i][j] = newPart.makeCluster(new Island(i, j));
                }
            }
        }

        //This loop helps to union 4-neighbor black cells
        for(int i = 0; i < S; i++) {
            for(int j = 0; j < T; j++) {
                if(land[i][j]) {
                    for(int k = 0; k < 4; k++) {
                        int kSide = i + sideOne[k];
                        int sSide = j + sideTwo[k];
                        if(kSide >= 0 && kSide < S && sSide >= 0 && sSide < T) {
                            if(land[kSide][sSide]) {
                                newPart.union(newNode[i][j], newNode[kSide][sSide]);
                            }
                        }
                    }
                }
            }
        }

        if(newSide == 0) {
            initialSurvey(newPart, true);
        } else {
            initialSurvey(newPart, false);
        }

        for(int l = 0; l < newSide; l++) {
            int newEntry = newScanner.nextInt();
            int[][] newArr = new int[newEntry][2];
            for(int m = 0; m < newEntry; m++) {
                newArr[m][0] = newScanner.nextInt();
                newArr[m][1] = newScanner.nextInt();
            }

            for(int n = 0; n < newEntry; n++) {
                int newSideOne = newArr[n][0];
                int newSideTwo = newArr[n][1];
                if(!land[newSideOne][newSideTwo]) {
                    land[newSideOne][newSideTwo] = true;
                    newNode[newSideOne][newSideTwo] = newPart.makeCluster(new Island(newSideOne, newSideTwo));
                }
            }

            for(int o = 0; o < newEntry; o++) {
                int aSide = newArr[o][0];
                int bSide = newArr[o][1];
                if(land[aSide][bSide]) {
                    for(int p = 0; p < 4; p++) {
                        int newSideOne = aSide + sideOne[p];
                        int newSideTwo = bSide + sideTwo[p];
                        if(newSideOne >= 0 && newSideOne < S && newSideTwo >= 0 && newSideTwo < T) {
                            if(land[newSideOne][newSideTwo]) {
                                newPart.union(newNode[aSide][bSide], newNode[newSideOne][newSideTwo]);
                            }
                        }
                    }
                }
            }

            if (l == newSide - 1) {
                initialSurvey(newPart, true);
            } else {
                initialSurvey(newPart, false);
            }
        }

        //Close
        newScanner.close();
    }
}
