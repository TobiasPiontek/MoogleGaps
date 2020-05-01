package MoogleGaps;

import crosby.binary.osmosis.OsmosisReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileReader {

    public static List<Long> nodeIds = new ArrayList<Long>();
    public static List<Integer> wayIds = new ArrayList<Integer>();
    public static double[] longitudes;
    public static double[] latitudes;

    public static void readPbfFile(String relativeFilePath) {

        // read files
        InputStream wayInputStream = null;
        InputStream nodeInputStream = null;
        System.out.println("reading file");
        try {
            wayInputStream = new FileInputStream(relativeFilePath);
            nodeInputStream = new FileInputStream(relativeFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

        // extract WayNodes
        OsmosisReader wayReader = new OsmosisReader(wayInputStream);
        WayReader wayData = new WayReader();
        wayReader.setSink(wayData);
        wayReader.run();

        System.out.println(nodeIds.size());
        // System.out.println(wayIds.size());

        // wayIds.add(nodeIds.size());

        Collections.sort(nodeIds);

        System.out.println(nodeIds.size());

        // check that nodeIds is sorted correctly
        for (int i = 0; i < nodeIds.size() - 1; i++) {
            if (nodeIds.get(i) > nodeIds.get(i + 1)) {
                System.out.println("F");
            }
        }

        longitudes = new double[nodeIds.size()];
        latitudes = new double[nodeIds.size()];

        // extract longitudes and latitudes
        OsmosisReader nodeReader = new OsmosisReader(nodeInputStream);
        NodeReader nodeData = new NodeReader();
        nodeReader.setSink(nodeData);
        nodeReader.run();

        for (int i = 0; i < nodeIds.size(); i++) {
            // System.out.println(nodeIds.get(i) + ": [" + longitudes[i] + ", " + latitudes[i] + "]");
        }

        for (int i = 0; i < 100; i++) {
            // System.out.println(wayIds.get(i));
        }

    }

    public static ArrayList<Long> getWayAtId(int wayId) {
        ArrayList<Long> wayAtIndex = new ArrayList<Long>();
        for (int i = wayIds.get(wayId); i < FileReader.wayIds.get(wayId + 1); i++) {
            wayAtIndex.add(nodeIds.get(i));
        }
        return wayAtIndex;
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
