package MoogleGaps;

import crosby.binary.osmosis.OsmosisReader;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.*;

public class FileReader {

    //Arrays for the Node management
    public static ArrayList<Long> nodeIds = new ArrayList<Long>();
    public static ArrayList<Integer> nodeIdLookUp;

    //Arrays for the ways with their coordinates
    public static List<Integer> wayIds = new ArrayList<Integer>();
    public static double[] longitudes;
    public static double[] latitudes;

    /**
     * Needs to be called to trigger the read in process
     * @param relativeFilePath the Path to the location of a PBF File
     */
    public static void readPbfFile(String relativeFilePath) {
        //Read the PBF file
        InputStream wayInputStream = null;
        InputStream nodeInputStream = null;
        System.out.println("reading file..." + new Timestamp(System.currentTimeMillis()));
        try {
            wayInputStream = new FileInputStream(relativeFilePath);
            nodeInputStream = new FileInputStream(relativeFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

        //Trigger the first stage with the read of the way nodes
        System.out.println("Reading in Ways " + new Timestamp(System.currentTimeMillis()));
        OsmosisReader wayReader = new OsmosisReader(wayInputStream);
        WayReader wayData = new WayReader();
        wayReader.setSink(wayData);
        wayReader.run();

        //Sort The NodeIds Array to speed up the link of nodeid and coordinates information
        System.out.println("Sorting NodeIds... " + new Timestamp(System.currentTimeMillis()));
        nodeIdLookUp = new ArrayList<>(nodeIds.size());
        nodeIdLookUp=Quicksort.quickSort(nodeIds);

        //allocating the arrays with their sizes
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

    /**
     * @param wayId the id of the way in the order where they have been read in
     * @return  a ArrayList with all nodes included
     */
    public static ArrayList<Long> getWayAtId(int wayId) {
        ArrayList<Long> wayAtIndex = new ArrayList<Long>();
        for (int i = wayIds.get(wayId); i < FileReader.wayIds.get(wayId + 1); i++) {
            wayAtIndex.add(nodeIds.get(i));
        }
        return wayAtIndex;
    }

    /**
     * @param wayIndex the id of the way in the order where they have been read in
     * @return the latitute coordinates in order of the respecting way
     */
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

    /**
     * @param wayIndex  the id of the way in the order where they have been read in
     * @return  the longitudes coordinates in order of the respecting way
     */
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
        return nodeIds.get(nodeIdLookUp.get(wayIds.get(id)));
    }

    public static long getLastNodeOfWay(int id) {
        if (id == wayIds.size() - 1) {
            return nodeIds.get(nodeIdLookUp.get(nodeIds.size() - 1));
        } else {
            return nodeIds.get(nodeIdLookUp.get(wayIds.get(id + 1) - 1));
        }
    }

}
