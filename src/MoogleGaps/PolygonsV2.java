package MoogleGaps;

import java.io.File;
import java.util.ArrayList;

public class PolygonsV2 {

    public static void createPolygons(){

        long[] startNodes = new long[FileReader.wayIds.size()];
        long[] endNodes = new long[FileReader.wayIds.size()];

        long[] orderedNodes = new long[FileReader.nodeIds.size()];

        // fill arrays with start and end nodes
        for(int i = 0; i < FileReader.wayIds.size() - 1; i++){
            startNodes[i] = FileReader.nodeIds.get(FileReader.nodeIdLookUp.get(FileReader.wayIds.get(i)));
            endNodes[i] = FileReader.nodeIds.get(FileReader.nodeIdLookUp.get(FileReader.wayIds.get(i+1)-1));
        }

        startNodes[startNodes.length-1] = FileReader.nodeIds.get(FileReader.nodeIdLookUp.get(FileReader.wayIds.get(FileReader.wayIds.size()-1)));
        endNodes[endNodes.length-1] = FileReader.nodeIds.get(FileReader.nodeIdLookUp.get(FileReader.nodeIds.size()-1));


        for(int i = 0; i < FileReader.wayIds.size();i++){
            if(startNodes[i]==endNodes[i]){
                System.out.println("circle detected at Way: " + i);
            }
        }

        }

        //Start and Endnodes are now filled in the list




}
