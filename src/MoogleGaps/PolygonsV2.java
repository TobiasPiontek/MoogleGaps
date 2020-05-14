package MoogleGaps;

import java.io.File;
import java.util.ArrayList;

public class PolygonsV2 {

    public static void createPolygons(){

        long[] startNodes = new long[FileReader.wayIds.size()];
        long[] endNodes = new long[FileReader.wayIds.size()];

        long[] orderedNodes = new long[FileReader.nodeIds.size()];

        // fill arrays with start and end nodes
        for(int i = 0; i < FileReader.wayIds.size(); i++){
            startNodes[i] = FileReader.getFirstNodeOfWay(i);
            endNodes[i] = FileReader.getLastNodeOfWay(i);
        }

        for(int i = 0; i < FileReader.wayIds.size();i++){
            if(startNodes[i]==endNodes[i]){
                System.out.println("circle detected at Way: " + i);
            }
        }

        }

        //Start and Endnodes are now filled in the list




}
