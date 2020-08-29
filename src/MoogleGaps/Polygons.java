package MoogleGaps;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Polygons {

    //Helper variables to store information during polygon creation
    private static boolean[] waysUsed = new boolean[FileReader.wayIds.size()];
    private static long[] startNodes = new long[FileReader.wayIds.size()];
    private static long[] endNodes = new long[FileReader.wayIds.size()];
    private static int coordinatesSize = 0; //A Cursor to know where the next Polygon has to be written in the Array

    //variables that store polygon Information
    public static double[] latitudes = new double[FileReader.latitudes.length];
    public static double[] longitudes = new double[FileReader.longitudes.length];
    public static ArrayList<Integer> polygonIds = new ArrayList<Integer>();    //saves the start Ids of the Ways

    //bounding box variables
    public static double[] boundingLatMin;
    public static double[] boundingLatMax;
    public static double[] boundingLonMin;
    public static double[] boundingLonMax;

    public static void createPolygons() {
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Detecting simple polygons...");
        createStartAndEndNodeArray();
        simpleCycleDetection();
        System.out.println(polygonIds.size() + " polygons found with simple circle detection");
        int simplePolygonsSize = polygonIds.size();
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Detecting non-simple polygons...");
        multiwayCycleDetection();
        int newPolygons = polygonIds.size() - simplePolygonsSize;
        System.out.println(newPolygons + " non-simple polygons found");
        System.out.println(polygonIds.size() + " Polygons in total");

        System.out.println(new Timestamp(System.currentTimeMillis()) + " Building bounding boxes...");
        createBoundingBoxes();
    }

    /**
     * Is called to fill the bounding boxes list
     * It contains of the upper left and the lower right vertex
     */
    private static void createBoundingBoxes() {
        //initialization of boundingbox variables
        boundingLatMin = new double[polygonIds.size()];
        boundingLatMax = new double[polygonIds.size()];
        boundingLonMin = new double[polygonIds.size()];
        boundingLonMax = new double[polygonIds.size()];

        //bounding Box creation
        for (int i = 0; i < polygonIds.size(); i++) {
            double[] latitudes = getPolygonLatitudes(i);
            double[] longitudes = getPolygonLongitudes(i);

            double latittudeMin = latitudes[0];
            double latitudeMax = latitudes[0];

            double longitudeMin = longitudes[0];
            double longitudeMax = longitudes[0];

            for (int j = 1; j < latitudes.length; j++) {
                if (latitudes[j] < latittudeMin) {
                    latittudeMin = latitudes[j];
                }
                if (latitudes[j] > latitudeMax) {
                    latitudeMax = latitudes[j];
                }
                if (longitudes[j] < longitudeMin) {
                    longitudeMin = longitudes[j];
                }
                if (longitudes[j] > longitudeMax) {
                    longitudeMax = longitudes[j];
                }
            }
            boundingLatMin[i] = latittudeMin;
            boundingLatMax[i] = latitudeMax;

            boundingLonMin[i] = longitudeMin;
            boundingLonMax[i] = longitudeMax;
        }
    }

    /**
     * This stage takes multiple ways glues them together to create larger polygons
     */
    private static void multiwayCycleDetection() {
        for (int i = 0; i < FileReader.wayIds.size(); i++) {
            if (!waysUsed[i]) {
                long start = startNodes[i];
                long end = endNodes[i];
                ArrayList<Integer> nodesIndexToAppend = new ArrayList<Integer>();

                boolean foundRight = true;
                while (foundRight) {
                    foundRight = false;
                    for (int j = 0; j < FileReader.wayIds.size(); j++) {
                        if (!waysUsed[j] && end == startNodes[j]) {
                            nodesIndexToAppend.add(j);
                            end = endNodes[j];
                            waysUsed[j] = true;
                            foundRight = true;
                            break;
                        }
                        if (start == end) {
                            break;
                        }
                    }
                }
                writeCycleToPolygonList(nodesIndexToAppend);
            }
        }
    }

    /**
     * Used to detect Polygons simply by choosing Ways where the end and startnode are equal
     */
    private static void simpleCycleDetection() {
        for (int i = 0; i < FileReader.wayIds.size(); i++) {
            if (startNodes[i] == endNodes[i]) {
                if (polygonIds.isEmpty()) {
                    polygonIds.add(0);
                } else {
                    polygonIds.add(coordinatesSize);
                }
                for (int j = 0; j < FileReader.getLengthOfWay(i); j++) {
                    latitudes[j + coordinatesSize] = FileReader.getLatitudesOfWay(i)[j];
                    longitudes[j + coordinatesSize] = FileReader.getLongitudesOfWay(i)[j];
                }
                coordinatesSize += FileReader.getLengthOfWay(i);
                waysUsed[i] = true;
            }
        }
    }

    /**
     * A helper method to write the multiwaypolygon to the polygonlist
     *
     * @param idsToAdd
     */
    private static void writeCycleToPolygonList(ArrayList<Integer> idsToAdd) {
        int cursor = coordinatesSize;
        if (polygonIds.isEmpty()) {
            polygonIds.add(0);
        } else if (polygonIds.get(polygonIds.size() - 1) < coordinatesSize) {
            polygonIds.add(coordinatesSize);
        }
        for (int i = 0; i < idsToAdd.size(); i++) {
            double[] polygonLatitudes = FileReader.getLatitudesOfWay(idsToAdd.get(i));
            double[] polygonLongitudes = FileReader.getLongitudesOfWay(idsToAdd.get(i));
            //intentionally leaving the last node out to prevent double detections
            for (int j = 0; j < polygonLatitudes.length - 1; j++) {
                latitudes[cursor] = polygonLatitudes[j];
                longitudes[cursor] = polygonLongitudes[j];
                cursor++;
            }
        }
        coordinatesSize = cursor;
    }

    /**
     * A helper Method to create an Array with the start and endnodes of the ways to speed up the polygon creation
     */
    private static void createStartAndEndNodeArray() {
        for (int i = 0; i < FileReader.wayIds.size(); i++) {
            startNodes[i] = FileReader.getFirstNodeOfWay(i);
            endNodes[i] = FileReader.getLastNodeOfWay(i);
        }
    }

    /**
     * @param index The index of the Polygon in the List
     * @return an Array of all Longitude coordinates
     */
    private static double[] getPolygonLongitudes(int index) {
        double[] polygonLongitudes = new double[getWayLength(index)];
        int k = 0;
        int startIndex = polygonIds.get(index);
        for (int j = 0; j < getWayLength(index); j++) {
            polygonLongitudes[k] = longitudes[j + startIndex];
            k++;
        }
        return polygonLongitudes;
    }

    /**
     * @param index The index of the Polygon in the List
     * @return an Array of all Latitude coordinates
     */
    private static double[] getPolygonLatitudes(int index) {
        double[] polygonLatitudes = new double[getWayLength(index)];

        int k = 0;
        int startIndex = polygonIds.get(index);
        for (int j = 0; j < getWayLength(index); j++) {
            polygonLatitudes[k] = latitudes[j + startIndex];
            k++;
        }
        return polygonLatitudes;
    }

    /**
     * helper method to get the length of a Polygon
     *
     * @param iD The ID of the polygon
     * @return the length of the given polygon id
     */
    public static int getWayLength(int iD) {
        if (iD < polygonIds.size() - 1) {
            return polygonIds.get(iD + 1) - polygonIds.get(iD);
        } else {
            return coordinatesSize - polygonIds.get(iD);
        }
    }
}
