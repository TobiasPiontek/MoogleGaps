package MoogleGaps;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Navigation {

    static double[] weights;
    static PriorityQueue<Integer> queue;
    static int[] prev;

    public static double dijkstra(int sourceId, int targetId){
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Computing Dijkstra's algorithm...");

        //Initialization
        weights = new double[GridGraph.vertexData.length];
        prev = new int[GridGraph.vertexData.length];

        Comparator<Integer> comp = new PriorityComparator();
        queue = new PriorityQueue<Integer>(GridGraph.vertexData.length, comp);
        boolean[] visited = new boolean[GridGraph.vertexData.length];

        Arrays.fill(weights, Double.POSITIVE_INFINITY);

        queue.add(sourceId);
        weights[sourceId] = 0;

        //start of Algorithm
        int currentNode;
        double currentWeight;
        double cost;
        boolean surroundedByWater;
        int[] neighborsOfNeighbor;
        do{
            currentNode = queue.poll();
            currentWeight = weights[currentNode];
            if(!visited[currentNode]){
                int[] neighbors = GridGraph.getNeighbors(currentNode);
                for (int neighbor : neighbors) {
                    if (!GridGraph.vertexData[neighbor]) {

                        // check if neighbor is surrounded by water
                        surroundedByWater = true;
                        neighborsOfNeighbor = GridGraph.getNeighbors(neighbor);
                        for (int neighborsNeighbor : neighborsOfNeighbor) {
                            if (GridGraph.vertexData[neighborsNeighbor]) {
                                surroundedByWater = false;
                            }
                        }
                        if (surroundedByWater) {

                            // check if neighbor is better
                            cost = getCosts(currentNode, neighbor);
                            if (weights[neighbor] > currentWeight + cost) {
                                weights[neighbor] = currentWeight + cost;
                                prev[neighbor] = currentNode;
                            }
                            queue.add(neighbor);
                        }
                    }
                }
                visited[currentNode] = true;
            }
        } while (!queue.isEmpty() && currentNode != targetId);

        return weights[targetId];

    }

    public static ArrayList<Integer> getWay(int sourceId, int targetId){
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Getting way...");

        int currentId = targetId;
        ArrayList<Integer> path = new ArrayList<>();
        while (currentId != sourceId){
            path.add(currentId);
            currentId = prev[currentId];
        }
        return path;
    }

    public static double getCosts(int index1, int index2) {
        int[] coordinates1 = GridGraph.idToGrid(index1);
        int[] coordinates2 = GridGraph.idToGrid(index2);
        if (coordinates1[0] - coordinates2[0] == -1) {
            // north
            if (Math.abs(coordinates1[1] - coordinates2[1]) == 1) {
                // diagonal
                return GridGraph.costs[coordinates1[0] * 2 + 1];
            } else if (coordinates1[1] == coordinates2[1]) {
                // vertical
                return GridGraph.costs[coordinates1[0] * 2];
            }
        } else if (coordinates1[0] - coordinates2[0] == 1) {
            // south
            if (Math.abs(coordinates1[1] - coordinates2[1]) == 1) {
                // diagonal
                return GridGraph.costs[coordinates1[0] * 2 + 1];
            } else if (coordinates1[1] == coordinates2[1]) {
                // vertical
                return GridGraph.costs[coordinates1[0] * 2];
            }
        } else if (coordinates1[0] == coordinates2[0] && Math.abs(coordinates1[1] - coordinates2[1]) == 1) {
            // horizontal
            return GridGraph.costs[GridGraph.costs.length - 1];
        }
        return 0.0;
    }

    public static class PriorityComparator implements Comparator<Integer> {
        public int compare(Integer index1, Integer index2) {
            return Double.compare(weights[index1], weights[index2]);
        }
    }

}
