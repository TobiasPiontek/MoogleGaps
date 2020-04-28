package MoogleGaps;

import crosby.binary.osmosis.OsmosisReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


public class FileReader {
    static int binarySearch(List<Long> arr, int l, int r, Long x)
    {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the middle
            // itself
            if (arr.get(mid) == x)
                return mid;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (arr.get(mid) > x)
                return binarySearch(arr, l, mid - 1, x);

            // Else the element can only be present
            // in right subarray
            return binarySearch(arr, mid + 1, r, x);
        }

        // We reach here when element is not
        // present in array
        return -1;
    }

    public static void readPbfFile(String relativeFilePath) {
        InputStream inputStream = null;
        System.out.println("reading file");
        try {
            inputStream = new FileInputStream(relativeFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }

        OsmosisReader reader = new OsmosisReader(inputStream);
        WayReader wayData = new WayReader();
        reader.setSink(wayData);
        reader.run();

        System.out.println(wayData.allNodeIds.size());
        System.out.println(wayData.allNodeLongitudes.size());
        System.out.println(wayData.allNodeLatitudes.size());
        System.out.println(wayData.nodeIds.size());
        System.out.println(wayData.wayIds.size());
        System.out.println(wayData.myNode);

        //System.out.println("Printing way nodes:");
        //for(int i = 0; i < wayData.nodeIds.size(); i++) {
        //   System.out.println(wayData.nodeIds.get(i));
        //}

        for (int i = 0; i < wayData.nodeIds.size(); i++) {
            // System.out.println(wayData.nodeIds.get(i) + ": [" + nodeData.latitudes.get(i) + ", " + nodeData.longitudes.get(i) + "]");
        }

        int i = 0;

        for (Long myId : wayData.nodeIds) {
            int index = binarySearch(wayData.allNodeIds, 0, wayData.allNodeIds.size() - 1, myId);
            if(i % 1000 == 0) {
                //System.out.println(index);
                //System.out.println("nochmal 1000");
                //System.out.println(myId + ": [" + wayData.longitudes.get(index) + ", " + wayData.latitudes.get(index) + "]");
            }
            i++;
        }


    }

}
