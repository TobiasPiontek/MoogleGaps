package MoogleGaps;

import com.sun.nio.sctp.SendFailedNotification;

import java.io.File;
import java.util.ArrayList;

public class Main {

    static ArrayList<Long> startNodes;
    static ArrayList<Long> endNodes;
    static long[] orderedNodes;

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));

        //GeoJson.printGeoJson();

        // output of end and starting node

        // testing code for way merging with duplication detection
        startNodes = new ArrayList<>(FileReader.wayIds.size());
        endNodes = new ArrayList<>(FileReader.wayIds.size());
        orderedNodes = new long[FileReader.nodeIds.size()];

        // fill arrays with start and end nodes
        for(int i = 0; i < FileReader.wayIds.size() - 1; i++){
            startNodes.add(FileReader.nodeIds.get(FileReader.wayIds.get(i)));
            endNodes.add(FileReader.nodeIds.get(FileReader.wayIds.get(i + 1) - 1));
        }
        // adding last element
        startNodes.add(FileReader.nodeIds.get(FileReader.wayIds.get(FileReader.wayIds.size() - 1)));  //get index of last start node
        endNodes.add(FileReader.nodeIds.get(FileReader.nodeIds.size() - 1));    //get index of last end node

        System.out.println("Size of WayIds is: " + FileReader.wayIds.size());
        System.out.println("Size of startNodes is: " + startNodes.size());
        System.out.println("Size of endNodes is: " + endNodes.size());

        //detect if ends math with start nodes

        Sort.completeWays();

        /**
        for(int i = 0; i < FileReader.wayIds.size()-1; i++){
            System.out.println("Way: " + i + " First Element: " + FileReader.nodeIds.get(FileReader.wayIds.get(i)) + " ,End Element: " + FileReader.nodeIds.get(FileReader.wayIds.get(i+1)-1));
        }
        **/
    }
}
