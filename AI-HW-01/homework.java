import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.io.IOException;

//Class with the information of the path
class Path{
    int pathCost;
    int cordinateRow;
    int cordinateColumn;
    String currPath;
    double heuristicCost;

    public Path(int pathCost, int cordinateRow, int cordinateColumn, String currPath){
        this.pathCost = pathCost;
        this.cordinateRow = cordinateRow;
        this.cordinateColumn = cordinateColumn;
        this.currPath = currPath;
    }

    public Path(int pathCost, int cordinateRow, int cordinateColumn, String currPath, double heuristicCost){
        this.pathCost = pathCost;
        this.cordinateRow = cordinateRow;
        this.cordinateColumn = cordinateColumn;
        this.currPath = currPath;
        this.heuristicCost = heuristicCost;
    }

    public Path() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Path)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Path p = (Path) o;

        return this.cordinateRow == p.cordinateRow && this.cordinateColumn == p.cordinateColumn;
    }

}


//Comparator class for comparing the path costs in USC algorithm
class PathComparatorUCS implements Comparator<Path>{          
    public int compare(Path p1, Path p2) { 
        if (p1.pathCost > p2.pathCost) 
            return 1; 
        else if (p1.pathCost < p2.pathCost) 
            return -1; 
        
        return 0; 
        } 
}


//Comparator class for comparing the heuristic and path costs in Astar algorithm
class PathComparatorAstar implements Comparator<Path>{       
    public int compare(Path p1, Path p2) { 
        if (p1.heuristicCost > p2.heuristicCost) 
            return 1; 
        else if (p1.heuristicCost < p2.heuristicCost) 
            return -1; 
        
        return 0; 
        } 
}


//Class with all the methods of the algorithm and main method
public class homework {
    static String algorithm;
    static int width;
    static int height;
    static int startRow;
    static int startColumn;
    static int maxRockHeight;
    static int numOfSettlingSites;
    static ArrayList<String> settlingSites = new ArrayList<>();
    static int[][] terrainConfig;


    //Method to find out BFS from Source to Destination
    public String bfs(int[][] terrainConfig2, String settlingSite) {
        int flag = 0; int h1; int h2; int m1; int m2;

        int destColumn = Integer.parseInt(settlingSite.split(" ")[0]);
        int destRow = Integer.parseInt(settlingSite.split(" ")[1]);

        boolean[][] visited = new boolean[height][width];

        Queue<String> sitesKnownQueue = new LinkedList<>();

        sitesKnownQueue.add(startRow + ":" + startColumn + ":" + startColumn + "," + startRow);

        while (sitesKnownQueue.isEmpty() == false) {
            String x = sitesKnownQueue.remove();
            int currRow = Integer.parseInt(x.split(":")[0]);
            int currColumn = Integer.parseInt(x.split(":")[1]);
            String pathTillNow = x.split(":")[2];

            if(currRow == destRow && currColumn == destColumn){
                System.out.println(pathTillNow);
                flag = 1;
                return (pathTillNow);
            }

            if (currColumn >= width || currRow >= height || currColumn < 0 || currRow < 0 || visited[currRow][currColumn] == true)
                continue;

            visited[currRow][currColumn] = true;  
            
            if(terrainConfig2[currRow][currColumn] >= 0){
                h1 = 0;
                m1 = terrainConfig2[currRow][currColumn];
            }
            else{
                h1 = Math.abs(terrainConfig2[currRow][currColumn]);
                m2 = 0;
            }

            //go north-west
            if((currRow - 1 >= 0) && (currColumn - 1 >= 0) && visited[currRow - 1][currColumn - 1] == false){
                if(terrainConfig2[currRow - 1][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn - 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add((currRow - 1) + ":" + (currColumn - 1) + ":" + pathTillNow + " " + (currColumn - 1) + "," + (currRow - 1));
                }
            }

            //go west
            if((currColumn - 1 >=0) && visited[currRow][currColumn - 1] == false){
                if(terrainConfig2[currRow][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow][currColumn - 1]);
                    m2 = 0;
                }
                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add(currRow + ":" + (currColumn - 1) + ":" + pathTillNow + " " + (currColumn - 1) + "," + currRow); 
                }
            }

            //go south-west
            if((currRow + 1 < height) && (currColumn - 1 >= 0) && visited[(currRow + 1)][currColumn - 1] == false){
                if(terrainConfig2[currRow + 1][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn - 1]);
                    m2 = 0;
                }
                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add((currRow + 1) + ":" + (currColumn - 1) + ":" + pathTillNow + " " + (currColumn - 1) + "," + (currRow + 1));
                }
            }

            //go north 
            if((currRow - 1 >=0) && visited[(currRow - 1)][currColumn] == false){

                if(terrainConfig2[currRow - 1][currColumn] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add((currRow - 1) + ":" + currColumn + ":" + pathTillNow + " " + currColumn + "," + (currRow - 1));
                }
            }

            //go south
            if((currRow + 1 < height) && visited[(currRow + 1)][currColumn] == false){
                if(terrainConfig2[currRow + 1][currColumn] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add((currRow + 1) + ":" + currColumn + ":" + pathTillNow + " " + currColumn + "," + (currRow + 1));
                }  
            }

            //go north-east
            if((currRow - 1 >= 0) && (currColumn + 1 < width) && visited[currRow - 1][currColumn + 1] == false){
                if(terrainConfig2[currRow - 1][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add((currRow - 1) + ":" + (currColumn + 1) + ":" + pathTillNow + " " + (currColumn + 1) + "," + (currRow - 1));
                }
            }

            //go east
            if((currColumn + 1 < width) && visited[currRow][currColumn + 1] == false){
                if(terrainConfig2[currRow][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add(currRow + ":" + (currColumn + 1) + ":" + pathTillNow + " " + (currColumn + 1) + "," + currRow);
                }
            }

            //go south-east
            if((currRow + 1 < height) && (currColumn + 1 < width) && visited[(currRow + 1)][currColumn + 1] == false){
                if(terrainConfig2[currRow + 1][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    sitesKnownQueue.add((currRow + 1) + ":" + (currColumn + 1) + ":" + pathTillNow + " " + (currColumn + 1) + "," + (currRow + 1));
                }
            }

        }

        // System.out.println("FAIL");
        return "FAIL";
    }



    //Method to find out UCS from Source to Destination
    public String ucs(int[][] terrainConfig2, String settlingSite) {
        int flag = 0;  int qFlag = 0; int h1; int h2; int m1; int m2;

        int destColumn = Integer.parseInt(settlingSite.split(" ")[0]);
        int destRow = Integer.parseInt(settlingSite.split(" ")[1]);

        Queue<Path> sitesOpenQueue = new PriorityQueue<>(new PathComparatorUCS());
        Queue<Path> sitesClosedQueue = new PriorityQueue<>(new PathComparatorUCS());

        sitesOpenQueue.add(new Path(0, startRow, startColumn, startColumn + "," + startRow));

        while (sitesOpenQueue.isEmpty() == false){
            Path x = sitesOpenQueue.poll();
            int currRow = x.cordinateRow;  
            int currColumn = x.cordinateColumn;
            int costTillNow = x.pathCost;
            String pathTillNow = x.currPath;

            if(currRow == destRow && currColumn == destColumn){
                // System.out.println(pathTillNow);
                System.out.println(costTillNow);
                flag = 1;
                return (pathTillNow);
            }

            if (currColumn >= width || currRow >= height || currColumn < 0 || currRow < 0)
                continue;

            if(terrainConfig2[currRow][currColumn] >= 0){
                h1 = 0;
                m1 = terrainConfig2[currRow][currColumn];
            }
            else{
                h1 = Math.abs(terrainConfig2[currRow][currColumn]);
                m2 = 0;
            }

            //go north-west
            if((currRow - 1 >= 0) && (currColumn - 1 >= 0)){
                if(terrainConfig2[currRow - 1][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn - 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 14, currRow-1, currColumn-1, pathTillNow + " " + (currColumn-1) + "," + (currRow-1));
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }

                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }


            //go west
            if((currColumn - 1 >=0)){
                if(terrainConfig2[currRow][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow][currColumn - 1]);
                    m2 = 0;
                }
                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 10, currRow, currColumn - 1, pathTillNow + " " + (currColumn - 1) + "," + currRow);

                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 10 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 10 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }


            //go south-west
            if((currRow + 1 < height) && (currColumn - 1 >= 0)){
                if(terrainConfig2[currRow + 1][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn - 1]);
                    m2 = 0;
                }
                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 14, currRow+1, currColumn-1, pathTillNow + " " + (currColumn-1) + "," + (currRow+1));
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }


            //go north 
            if((currRow - 1 >=0)){

                if(terrainConfig2[currRow - 1][currColumn] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 10, currRow-1, currColumn, pathTillNow + " " + currColumn + "," + (currRow-1));
                    if(sitesOpenQueue.contains(y)){
                    for(Path p: sitesOpenQueue){
                        if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn){
                                if(costTillNow + 10 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                        }
                    }
                    if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn){
                                if(costTillNow + 10 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }


            //go south
            if((currRow + 1 < height)){
                if(terrainConfig2[currRow + 1][currColumn] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 10, currRow+1, currColumn, pathTillNow + " " + currColumn + "," + (currRow+1));
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn){
                                    if(costTillNow + 10 < p.pathCost){
                                        sitesOpenQueue.remove(p);
                                        qFlag = 1;
                                        break;
                                    }
                            }
                        }

                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn){
                                if(costTillNow + 10 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }  
            }


            //go north-east
            if((currRow - 1 >= 0) && (currColumn + 1 < width)){
                if(terrainConfig2[currRow - 1][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 14, currRow-1, currColumn+1, pathTillNow + " " + (currColumn+1) + "," + (currRow-1));
                    if(sitesOpenQueue.contains(y)){
                    for(Path p: sitesOpenQueue){
                        if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 14 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }    

                    if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 14 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }

                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }


            //go east
            if((currColumn + 1 < width)){
                if(terrainConfig2[currRow][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 10, currRow, currColumn + 1, pathTillNow + " " + (currColumn + 1) + "," + currRow);
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 10 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 10 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }


            //go south-east
            if((currRow + 1 < height) && (currColumn + 1 < width)){
                if(terrainConfig2[currRow + 1][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    Path y = new Path(costTillNow + 14, currRow+1, currColumn+1, pathTillNow + " " + (currColumn+1) + "," + (currRow+1));
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn+1){
                                    if(costTillNow + 14 < p.pathCost){
                                        sitesOpenQueue.remove(p);
                                        qFlag = 1;
                                        break;
                                    }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 14 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }

            sitesClosedQueue.add(x);
        }

        // System.out.println("FAIL");
        return "FAIL";
    }



    //Method to find out Astar from Source to Destination
    public String astar(int[][] terrainConfig2, String settlingSite) {
        int flag = 0;  int qFlag = 0; int h1; int h2; int m1; int m2;

        int destColumn = Integer.parseInt(settlingSite.split(" ")[0]);
        int destRow = Integer.parseInt(settlingSite.split(" ")[1]);
        double intialHeuristicCost = Math.sqrt(Math.pow((destRow - startRow), 2) + Math.pow((destColumn - startColumn), 2)) * 10;

        Queue<Path> sitesOpenQueue = new PriorityQueue<>(new PathComparatorAstar());
        Queue<Path> sitesClosedQueue = new PriorityQueue<>(new PathComparatorAstar());

        sitesOpenQueue.add(new Path(0, startRow, startColumn, startColumn + "," + startRow, intialHeuristicCost));

        while (sitesOpenQueue.isEmpty() == false){
            Path x = sitesOpenQueue.poll();
            int currRow = x.cordinateRow;  
            int currColumn = x.cordinateColumn;
            int costTillNow = x.pathCost;
            double currHeuristicCost = 0.0;
            String pathTillNow = x.currPath;

            if(currRow == destRow && currColumn == destColumn){
                // System.out.println(pathTillNow);
                System.out.println(costTillNow);
                flag = 1;
                return (pathTillNow);
            }

            if (currColumn >= width || currRow >= height || currColumn < 0 || currRow < 0)
                continue;

            if(terrainConfig2[currRow][currColumn] >= 0){
                h1 = 0;
                m1 = terrainConfig2[currRow][currColumn];
            }
            else{
                h1 = Math.abs(terrainConfig2[currRow][currColumn]);
                m2 = 0;
            }


            //go north-west
            if((currRow - 1 >= 0) && (currColumn - 1 >= 0)){
                if(terrainConfig2[currRow - 1][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn - 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - (currRow-1)), 2) + Math.pow((destColumn - (currColumn-1)), 2)) * 10;
                    Path y = new Path(costTillNow + 14 + Math.abs(h1 - h2) + m2, currRow - 1, currColumn - 1, pathTillNow + " " + (currColumn - 1) + "," + (currRow-1), currHeuristicCost + costTillNow + 14 + Math.abs(h1 - h2) + m2);
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }

                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }

            //go west
            if((currColumn - 1 >=0)){
                if(terrainConfig2[currRow][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow][currColumn - 1]);
                    m2 = 0;
                }
                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - currRow), 2) + Math.pow((destColumn - (currColumn-1)), 2)) * 10;
                    Path y = new Path(costTillNow + 10 + Math.abs(h1 - h2) + m2, currRow, currColumn - 1, pathTillNow + " " + (currColumn - 1) + "," + currRow, currHeuristicCost + costTillNow + 10 + Math.abs(h1 - h2) + m2);

                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }

            //go south-west
            if((currRow + 1 < height) && (currColumn - 1 >= 0)){
                if(terrainConfig2[currRow + 1][currColumn - 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn - 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn - 1]);
                    m2 = 0;
                }
                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - (currRow+1)), 2) + Math.pow((destColumn - (currColumn-1)), 2)) * 10;
                    Path y = new Path(costTillNow + 14 + Math.abs(h1 - h2) + m2, currRow + 1, currColumn - 1, pathTillNow + " " + (currColumn - 1) + "," + (currRow+1), currHeuristicCost + costTillNow + 14 + Math.abs(h1 - h2) + m2);
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn-1){
                                if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }

            //go north 
            if((currRow - 1 >=0)){

                if(terrainConfig2[currRow - 1][currColumn] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - (currRow-1)), 2) + Math.pow((destColumn - currColumn), 2)) * 10;
                    Path y = new Path(costTillNow + 10 + Math.abs(h1 - h2) + m2, currRow - 1, currColumn, pathTillNow + " " + (currColumn) + "," + (currRow-1), currHeuristicCost + costTillNow + 10 + Math.abs(h1 - h2) + m2);
                    if(sitesOpenQueue.contains(y)){
                    for(Path p: sitesOpenQueue){
                        if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn){
                                if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                        }
                    }
                    if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn){
                                if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }

            //go south
            if((currRow + 1 < height)){
                if(terrainConfig2[currRow + 1][currColumn] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - (currRow+1)), 2) + Math.pow((destColumn - currColumn), 2)) * 10;
                    Path y = new Path(costTillNow + 10 + Math.abs(h1 - h2) + m2, currRow + 1, currColumn, pathTillNow + " " + (currColumn) + "," + (currRow+1), currHeuristicCost + costTillNow + 10 + Math.abs(h1 - h2) + m2);
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn){
                                    if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                        sitesOpenQueue.remove(p);
                                        qFlag = 1;
                                        break;
                                    }
                            }
                        }

                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn){
                                if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }  
            }

            //go north-east
            if((currRow - 1 >= 0) && (currColumn + 1 < width)){
                if(terrainConfig2[currRow - 1][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow - 1][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow - 1][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - (currRow-1)), 2) + Math.pow((destColumn - (currColumn+1)), 2)) * 10;
                    Path y = new Path(costTillNow + 14 + Math.abs(h1 - h2) + m2, currRow - 1, currColumn + 1, pathTillNow + " " + (currColumn + 1) + "," + (currRow-1), currHeuristicCost + costTillNow + 14 + Math.abs(h1 - h2) + m2);
                    if(sitesOpenQueue.contains(y)){
                    for(Path p: sitesOpenQueue){
                        if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }    

                    if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow-1 && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }

                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }

            //go east
            if((currColumn + 1 < width)){
                if(terrainConfig2[currRow][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - currRow), 2) + Math.pow((destColumn - (currColumn+1)), 2)) * 10;
                    Path y = new Path(costTillNow + 10 + Math.abs(h1 - h2) + m2, currRow, currColumn + 1, pathTillNow + " " + (currColumn + 1) + "," + currRow, currHeuristicCost + costTillNow + 10 + Math.abs(h1 - h2) + m2);
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesOpenQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 10 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }
            
            //go south-east
            if((currRow + 1 < height) && (currColumn + 1 < width)){
                if(terrainConfig2[currRow + 1][currColumn + 1] >= 0){
                    h2 = 0;
                    m2 = terrainConfig2[currRow + 1][currColumn + 1];
                }
                else{
                    h2 = Math.abs(terrainConfig2[currRow + 1][currColumn + 1]);
                    m2 = 0;
                }

                if(Math.abs(h1 - h2) <= maxRockHeight){
                    currHeuristicCost = Math.sqrt(Math.pow((destRow - (currRow+1)), 2) + Math.pow((destColumn - (currColumn+1)), 2)) * 10;
                    Path y = new Path(costTillNow + 14 + Math.abs(h1 - h2) + m2, currRow + 1, currColumn + 1, pathTillNow + " " + (currColumn + 1) + "," + (currRow+1), currHeuristicCost + costTillNow + 14 + Math.abs(h1 - h2) + m2);
                    if(sitesOpenQueue.contains(y)){
                        for(Path p: sitesOpenQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn+1){
                                    if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                        sitesOpenQueue.remove(p);
                                        qFlag = 1;
                                        break;
                                    }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else if(sitesClosedQueue.contains(y)){
                        for(Path p: sitesClosedQueue){
                            if(p.cordinateRow == currRow+1 && p.cordinateColumn == currColumn+1){
                                if(costTillNow + 14 + Math.abs(h1 - h2) + m2 < p.pathCost){
                                    sitesClosedQueue.remove(p);
                                    qFlag = 1;
                                    break;
                                }
                            }
                        }
                        if(qFlag == 1){
                            sitesOpenQueue.add(y);
                            qFlag = 0;
                        }
                    }
                    else{
                        sitesOpenQueue.add(y);
                    }
                }
            }

            sitesClosedQueue.add(x);
        }

        // System.out.println("FAIL");
        return "FAIL";
    }


    //Main Method
    public static void main(String[] args) throws IOException {

        File inputFile = new File("input47.txt");
        FileWriter fileWriter = new FileWriter("output47.txt");

        Scanner fileScanner = new Scanner(inputFile);

        int count = 1; int rowCount = 0;

        while (fileScanner.hasNextLine()) {
            String data = fileScanner.nextLine();

            if (count == 1) {
                algorithm = data; 
            } 
            else if (count == 2) {
                width = Integer.parseInt(data.split(" ")[0]);
                height = Integer.parseInt(data.split(" ")[1]);
                terrainConfig = new int[height][width];
            } 
            else if (count == 3) {
                startColumn = Integer.parseInt(data.split(" ")[0]);
                startRow = Integer.parseInt(data.split(" ")[1]);
            } 
            else if (count == 4) {
                maxRockHeight = Integer.parseInt(data);
            } 
            else if (count == 5) {
                numOfSettlingSites = Integer.parseInt(data);
            } 
            else if (count >= 6 && count < 6 + numOfSettlingSites) {
                settlingSites.add(data);
            } 
            else {
                String[] rowConfig = data.trim().split(" ");
                for(int j=0; j<rowConfig.length; j++){
                    terrainConfig[rowCount][j] = Integer.parseInt(rowConfig[j]);
                }
                rowCount++;
            }
            count++;
        }

        homework hw = new homework();

        switch(algorithm){
            case "BFS": {
                for(int i=0; i<numOfSettlingSites; i++){
                    String pathReceived = hw.bfs(terrainConfig, settlingSites.get(i));
                    fileWriter.write(pathReceived);
                    if(i != numOfSettlingSites - 1){
                        fileWriter.write("\n");
                    }
                    // System.out.println();
                }
                break;
            }
            case "UCS":{
                for(int i=0; i<numOfSettlingSites; i++){
                    String pathReceived = hw.ucs(terrainConfig, settlingSites.get(i));
                    fileWriter.write(pathReceived);
                    if(i != numOfSettlingSites - 1){
                        fileWriter.write("\n");
                    }
                    // System.out.println();
                }
                break;
            }
            case "A*":{
                for(int i=0; i<numOfSettlingSites; i++){
                    String pathReceived = hw.astar(terrainConfig, settlingSites.get(i));
                    fileWriter.write(pathReceived);
                    if(i != numOfSettlingSites - 1){
                        fileWriter.write("\n");
                    }
                    // System.out.println();
                }
                break;
            }
        }

        fileScanner.close();
        fileWriter.close();
        
    }

}