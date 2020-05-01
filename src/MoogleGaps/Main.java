package MoogleGaps;

import com.sun.nio.sctp.SendFailedNotification;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));

        //GeoJson.printGeoJson();

        //output of end and starting Node

        //testing code for way merging with duplication detection
        ArrayList<Long> startnodes = new ArrayList<>(FileReader.wayIds.size());
        ArrayList<Long> endnodes = new ArrayList<>(FileReader.wayIds.size());
        //fill arrays with start and end nodes
        for(int i = 0; i < FileReader.wayIds.size()-1; i++){
            startnodes.add(FileReader.nodeIds.get(FileReader.wayIds.get(i)));
            endnodes.add(FileReader.nodeIds.get(FileReader.wayIds.get(i+1)-1));
        }
        //adding last Element
        startnodes.add(FileReader.nodeIds.get(FileReader.wayIds.get(FileReader.wayIds.size()-1)));  //get index of last start node
        endnodes.add(FileReader.nodeIds.get(FileReader.nodeIds.size()-1));    //get index of last end node

        System.out.println("Size of WayIds is: " + FileReader.wayIds.size());
        System.out.println("Size of startnodes is: " + startnodes.size());
        System.out.println("Size of endnodes is: " + endnodes.size());

        int merges = 0;
        for(int i = 0; i < FileReader.wayIds.size()-1; i++){
            if(startnodes.contains(endnodes.get(i))){
                System.out.println("way: " + i + " with size: " + FileReader.wayIds.get(i)  + " and: " + startnodes.indexOf(endnodes.get(i)) + " with size: " + FileReader.wayIds.get(startnodes.indexOf(endnodes.get(i)))  + " are mergeable!");
                merges++;
            }
        }
        System.out.println("Merges found: " + merges);

        //detect if ends math with start nodes



        /**
        for(int i = 0; i < FileReader.wayIds.size()-1; i++){
            System.out.println("Way: " + i + " First Element: " + FileReader.nodeIds.get(FileReader.wayIds.get(i)) + " ,End Element: " + FileReader.nodeIds.get(FileReader.wayIds.get(i+1)-1));
        }
        **/

    }
}
