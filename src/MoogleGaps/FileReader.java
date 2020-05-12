package MoogleGaps;

import crosby.binary.osmosis.OsmosisReader;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.*;

public class FileReader {

    public static ArrayList<Long> nodeIds = new ArrayList<Long>();
    public static ArrayList<Integer> nodeIdLookUp;


    public static List<Integer> wayIds = new ArrayList<Integer>();
    public static double[] longitudes;
    public static double[] latitudes;

    public static void readPbfFile(String relativeFilePath) {

        // read files
        InputStream wayInputStream = null;
        InputStream nodeInputStream = null;
        System.out.println("reading file..." + new Timestamp(System.currentTimeMillis()));
        try {
            wayInputStream = new FileInputStream(relativeFilePath);
            nodeInputStream = new FileInputStream(relativeFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

        System.out.println("Reading in Ways " + new Timestamp(System.currentTimeMillis()));
        OsmosisReader wayReader = new OsmosisReader(wayInputStream);
        WayReader wayData = new WayReader();
        wayReader.setSink(wayData);
        wayReader.run();

        System.out.println("Sorting NodeIds... " + new Timestamp(System.currentTimeMillis()));
        nodeIdLookUp = new ArrayList<>(nodeIds.size());
        nodeIdLookUp=Quicksort.quickSort(nodeIds);


        System.out.println("Reading in node coordinates... " + new Timestamp(System.currentTimeMillis()));
        longitudes = new double[nodeIds.size()];
        latitudes = new double[nodeIds.size()];

        // extract longitudes and latitudes
        OsmosisReader nodeReader = new OsmosisReader(nodeInputStream);
        NodeReader nodeData = new NodeReader();
        nodeReader.setSink(nodeData);
        nodeReader.run();
        System.out.println("Done with reading Nodes... " + new Timestamp(System.currentTimeMillis()));
    }

    public static ArrayList<Long> getWayAtId(int wayId) {
        ArrayList<Long> wayAtIndex = new ArrayList<Long>();
        for (int i = wayIds.get(wayId); i < FileReader.wayIds.get(wayId + 1); i++) {
            wayAtIndex.add(nodeIds.get(i));
        }
        return wayAtIndex;
    }


    public static double[] getLatitudesOfWay(int wayIndex){
        int startpoint;
        int endpoint;
        startpoint = wayIds.get(wayIndex);
        if(wayIndex < wayIds.size()-1){ //if its not the last element
            endpoint = wayIds.get(wayIndex+1)-1;
        }else{
            endpoint=nodeIds.size()-1;
        }
        int size = endpoint-startpoint;
        double[] wayAtId = new double[size];

        for(int i = 0; i < size; i++){
            wayAtId[i] = latitudes[FileReader.nodeIdLookUp.get(i + startpoint)];
        }

        return wayAtId;
    }


    public static double[] getLongitudesOfWay(int wayIndex){
        int startpoint;
        int endpoint;
        startpoint = wayIds.get(wayIndex);
        if(wayIndex < wayIds.size()-1){ //if its not the last element
            endpoint = wayIds.get(wayIndex+1)-1;
        }else{
            endpoint=nodeIds.size()-1;
        }
        int size = endpoint-startpoint;
        double[] wayAtId = new double[size];

        for(int i = 0; i < size; i++){
            wayAtId[i] = longitudes[FileReader.nodeIdLookUp.get(i + startpoint)];
        }
        return wayAtId;
    }


    public static long getFirstNodeOfWay(int id) {
        return nodeIds.get(wayIds.get(id));
    }

    public static long getLastNodeOfWay(int id) {
        if (id == wayIds.size() - 1) {
            return nodeIds.get(nodeIds.size() - 1);
        } else {
            return nodeIds.get(wayIds.get(id + 1) - 1);
        }
    }
}
