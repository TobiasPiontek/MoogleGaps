package MoogleGaps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Navigation {

    static double[] weights;
    static PriorityQueue<Integer> queue;
    static int[] prev;

    public static double dijkstra(int sourceId, int targetId){
        //Initialization
        weights = new double[GridGraph.vertexData.length];
        prev = new int[GridGraph.vertexData.length];

        Comparator<Integer> comp = new PriorityComparator();
        queue = new PriorityQueue<Integer>(GridGraph.vertexData.length, comp);
        boolean[] visited = new boolean[GridGraph.vertexData.length];

        for(int i = 0; i < weights.length; i++){
            weights[i] =  Double.POSITIVE_INFINITY;
        }
        queue.add(sourceId);
        weights[sourceId]=0;

        //start of Algorithm
        int currentNode;
        double currentWeight;
        do{
            currentNode=queue.poll();
            currentWeight=weights[currentNode];
            if(!visited[currentNode]){
                int[] neighbors = GridGraph.getNeighbors(currentNode);
                for(int i = 0; i < neighbors.length; i++){
                    //Todo implement cost variable in Grid graph
                    if(weights[neighbors[i]] > currentWeight + cost[i]){
                        weights[neighbors[i]] = currentWeight + cost[i];
                        prev[neighbors[i]] = currentNode;
                    }
                    queue.add(neighbors[i]);
                }
                visited[currentNode] = true;
            }
        }while(!queue.isEmpty() && currentNode != targetId);

        return weights[targetId];

    }

    public static ArrayList<Integer> getWay(int sourceId, int targetId){
        int currentId = targetId;
        ArrayList<Integer> path = new ArrayList<>();
        while(currentId != sourceId){
            path.add(currentId);
            currentId = prev[currentId];
        }
        return path;
    }


    public static class PriorityComparator implements Comparator<Integer> {
        public int compare(Integer index1, Integer index2) {
            if (weights[index1] < weights[index2]) return -1;
            if (weights[index1] > weights[index2]) return 1;
            return 0;
        }
    }



}
