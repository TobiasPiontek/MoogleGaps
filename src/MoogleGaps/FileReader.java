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

    }

}
