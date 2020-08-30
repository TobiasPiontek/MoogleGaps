package MoogleGaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Navigation {

    private static double[] weights;
    private static PriorityQueue<Integer> queue;
    private static int[] prev;

    private static double dijkstraCalculation(int sourceId, int targetId) {
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
        do {
            currentNode = queue.poll();
            currentWeight = weights[currentNode];
            if (!visited[currentNode]) {
                int[] neighbors = GridGraph.getNeighbors(currentNode);
                for (int neighbor : neighbors) {
                    if (!GridGraph.vertexData[neighbor]) {
                        // check if neighbor is surrounded by water
                        surroundedByWater = isSurroundedByWater(neighbor);
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
        } while (!queue.isEmpty()/* && currentNode != targetId*/);
        return weights[targetId];
    }

    /**
     * @param vertex
     * @return true if all 8 neighbour nodes of the vertex are on water, else false
     */
    public static boolean isSurroundedByWater(int vertex) {
        boolean surroundedByWater;
        int[] neighborsOfNeighbor;
        surroundedByWater = true;
        neighborsOfNeighbor = GridGraph.getNeighbors(vertex);
        for (int neighborsNeighbor : neighborsOfNeighbor) {
            if (GridGraph.vertexData[neighborsNeighbor]) {
                surroundedByWater = false;
            }
        }
        return surroundedByWater;
    }

    private static ArrayList<Integer> getWay(int sourceId, int targetId) {

        int currentId = targetId;
        ArrayList<Integer> path = new ArrayList<>();
        while (currentId != sourceId) {
            path.add(currentId);
            //System.out.println("Get weight of current node: " + weights[currentId]);
            currentId = prev[currentId];
            //preventing loop during way readout
            if (currentId == prev[currentId]) {
                System.err.println("No possible route could be found!");
                return new ArrayList<>();
            }
        }
        path.add(sourceId);
        return path;
    }

    public static ArrayList<Integer> dijkstra(int sourceId, int targetId) {
        dijkstraCalculation(sourceId, targetId);
        return getWay(sourceId, targetId);
    }

    private static double getCosts(int index1, int index2) {
        int[] coordinates1 = GridGraph.idToGrid(index1);
        int[] coordinates2 = GridGraph.idToGrid(index2);
        if (coordinates1[0] - coordinates2[0] == -1) {
            // north
            if (Math.abs(coordinates1[1] - coordinates2[1]) >= 1) {
                // diagonal
                return GridGraph.costs[coordinates1[0] * 2 + 1];
            } else if (coordinates1[1] == coordinates2[1]) {
                // vertical
                return GridGraph.costs[coordinates1[0] * 2];
            }
        } else if (coordinates1[0] - coordinates2[0] == 1) {
            // south
            if (Math.abs(coordinates1[1] - coordinates2[1]) >= 1) {
                // diagonal
                return GridGraph.costs[coordinates1[0] * 2 + 1];
            } else if (coordinates1[1] == coordinates2[1]) {
                // vertical
                return GridGraph.costs[coordinates1[0] * 2];
            }
        } else if (coordinates1[0] == coordinates2[0] && Math.abs(coordinates1[1] - coordinates2[1]) >= 1) {
            // horizontal
            return GridGraph.costs[GridGraph.costs.length - 1];
        }
        System.err.println("[Debug] No reasonable cost could be assigned! (getCost function!)");
        return 0.0;
    }

    private static class PriorityComparator implements Comparator<Integer> {
        public int compare(Integer index1, Integer index2) {
            return Double.compare(weights[index1], weights[index2]);
        }
    }

}
