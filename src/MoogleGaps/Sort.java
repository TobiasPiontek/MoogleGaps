package MoogleGaps;

import java.util.ArrayList;
import java.util.List;

public class Sort {


    //variables for recursive implementation
    private static boolean[] seenWays = new boolean[FileReader.wayIds.size()];  //also used for iterative way sort
    private static List<Long> nodeIds = new ArrayList<Long>();
    private static List<Integer> wayIds = new ArrayList<Integer>();

    //variables for iterative implementation

    public static ArrayList<Long> sortWays(ArrayList<Long> nodeIds, int wayId) {
        seenWays[wayId] = true;
        for (int i = 0; i < FileReader.wayIds.size(); i++) {

            if (!seenWays[i]) {
                if (nodeIds.get(nodeIds.size() - 1) == FileReader.getFirstNodeOfWay(i)) {

                    // append right
                    nodeIds.addAll(FileReader.getWayAtId(i));
                    return sortWays(nodeIds, i);
                } else if (nodeIds.get(0) == FileReader.getLastNodeOfWay(i)) {

                    // append left
                    ArrayList<Long> addLeft = FileReader.getWayAtId(i);
                    addLeft.addAll(0, FileReader.getWayAtId(i));
                    return sortWays(addLeft, i);
                }
            }
        }
        return nodeIds;
    }

    public static void completeWays() {
        for (int i = 0; i < seenWays.length-1; i++) {
            if (!seenWays[i]) {
                ArrayList<Long> way = sortWays(FileReader.getWayAtId(i), i);
                nodeIds.addAll(i, way);
                wayIds.add(way.size());
            }
        }

        for (int i = 0; i < wayIds.size(); i++) {
            System.out.println(wayIds.get(i));
        }
        System.out.println("Sort.wayIds.size() = " + wayIds.size());
     }



    public static void iterativeWaySort(){

        for(int i = 0; i<seenWays.length; i++){
            if(seenWays[i]){

            }

        }


    }



}
