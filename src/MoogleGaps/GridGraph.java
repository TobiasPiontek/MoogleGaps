package MoogleGaps;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class GridGraph {

    private static ArrayList<Boolean> vertexDataEquidistant;
    private static int[] firstIndexOf;
    private static double r = 1;
    private static int nCount = 0;
    private static double mTheta;
    private static double mPhi;
    public static boolean[] vertexData;
    private static int southToNorth;
    private static int westToEast;
    public static double[] costs;
    private static double sideLength;

    public static void generateEquidistant(int n) {
        nCount = 0;
        double phi;
        double theta;
        double longitude;
        double latitude;
        double a = 4 * Math.PI * Math.pow(r, 2) / n;
        double d = Math.sqrt(a);
        mTheta = Math.round(Math.PI / d);
        double dTheta = Math.PI / mTheta;
        double dPhi = a / dTheta;

        // DEBUG
        System.out.println(d);
        System.out.println(mTheta);

        for (int m = 0; m < mTheta; m++) {
            theta = Math.PI * (m + 0.5) / mTheta;
            latitude = theta - (Math.PI / 2);
            mPhi = Math.round(2 * Math.PI * Math.sin(theta / dPhi));
            for (int i = 0; i < mPhi; i++) {
                phi = 2 * Math.PI * i / mPhi;
                longitude = phi - Math.PI;
                vertexDataEquidistant.add(gridToIdEquidistant(m, i), Geometry.pointInPolygonTest(longitude, latitude));
                System.out.println(gridToIdEquidistant(m, i));
                nCount++;
            }
        }
    }

    /**
     * takes n as integer for number of grid points to generate
     * fills boolean array vertexData with True for on land and False for in water
     *
     * @param n
     */
    public static void generate(int n) {
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Generating grid graph...");
        southToNorth = (int) ((Math.sqrt(2 * n + 1) - 1) / 2);
        westToEast = (southToNorth + 1) * 2;
        sideLength = 180.0 / (southToNorth + 1);
        vertexData = new boolean[southToNorth * westToEast];
        System.out.println("southToNorth = " + southToNorth + ", westToEast = " + westToEast + ", vertexData.length = " + vertexData.length);
        for (int i = 0; i < westToEast; i++) {
            double longitude = i * 360.0 / westToEast - 180;
            for (int j = 0; j < southToNorth; j++) {
                double latitude = (j + 1) * 180.0 / (southToNorth + 1) - 90;
                //System.out.println("(" + longitude + ", " + latitude + ")");
                //System.out.println(i * southToNorth + j);
                vertexData[i * southToNorth + j] = Geometry.pointInPolygonTest(longitude, latitude);
            }
        }

        computeCosts();
    }

    public static int findVertex(double longitude, double latitude) {
        //double sideLength = 180.0 / southToNorth;
        double midX = Math.floor(longitude / sideLength) + 0.5;
        double midLongitude = midX * sideLength;
        double midY = Math.floor(latitude / sideLength) + 0.5;
        double midLatitude = midY * sideLength;
        int col;
        int row;
        if (longitude < midLongitude) {
            col = (int) midX;
        } else {
            col = (int) (midX + 0.5);
        }
        if (latitude < midLatitude) {
            row = (int) midY;
        } else {
            row = (int) (midY + 0.5);
        }
        return gridToId(row, col);
    }

    private static double getEuclideanDistance(double xA, double yA, double xB, double yB) {
        return Math.sqrt(Math.pow(xA - xB, 2) + Math.pow(yA - yB, 2));
    }

    private static int gridToId(int row, int col) {
        return col * southToNorth + row;
    }

    public static int[] idToGrid(int id) {
        int col = id / southToNorth;
        int row = id % southToNorth;
        int[] coordinates = new int[2];
        coordinates[0] = row;
        coordinates[1] = col;
        return coordinates;
    }

    public static boolean inWater(int row, int col) {
        return !vertexData[gridToId(row, col)];
    }

    public static double[] gridToCoordinates(int row, int col) {
        //double sideLength = 180 / (double) (southToNorth + 1);
        double longitude = col * sideLength - 180;
        double latitude = (row + 1) * sideLength - 90;
        double[] coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;
        return coordinates;
    }

    /**
     * converts grid coordinates to IDs
     *
     * @param m
     * @param n
     * @return ID of point as integer
     */
    public static int gridToIdEquidistant(int m, int n) {
        return firstIndexOf[m] + n;
    }

    /**
     * converts IDs to grid coordinates
     *
     * @param id
     * @return grid coordinates as integer array
     */
    public static int[] idToGridEquidistant(int id) {
        int m = Arrays.binarySearch(firstIndexOf, id);
        int n = id - m;
        int[] pair = {m, n};
        return pair;
    }

    /**
     * takes m and n of vertex ID
     *
     * @param mn
     * @return coordinates of vertex as double array {longitude, latitude}
     */
    public static double[] getCoordinates(int[] mn) {
        double[] coordinates = new double[2];

        double phi = 2 * Math.PI * mn[1] / mPhi;
        coordinates[0] = phi - Math.PI;

        double theta = Math.PI * (mn[0] + 0.5) / mTheta;
        coordinates[1] = theta - (Math.PI / 2);
        return coordinates;
    }

    /**
     * takes row and column of vertex ID
     *
     * @param index
     * @return IDs of neighboring vertices as integer array
     */
    public static int[] getNeighbors(int index) {
        int[] neighbors;
        int[] coordinates = idToGrid(index);
        int rowPoint = coordinates[0];
        int colPoint = coordinates[1];
        int count = 0;
        int row;
        int col;
        if (rowPoint != 0 && rowPoint != southToNorth - 1) {
            neighbors = new int[8];
            for (int i = 0; i < 3; i++) {
                row = rowPoint - 1 + i;
                for (int j = 0; j < 3; j++) {
                    col = (((colPoint - 1 + j) % westToEast) + westToEast) % westToEast;
                    if (!(i == 1 && j == 1)) {
                        neighbors[count] = gridToId(row, col);
                        count++;
                    }
                }
            }
        } else if (rowPoint == 0){
            neighbors = new int[5];
            for (int i = 0; i < 2; i++) {
                row = rowPoint + i;
                for (int j = 0; j < 3; j++) {
                    col = (((colPoint - 1 + j) % westToEast) + westToEast) % westToEast;
                    if (!(i == 0 && j == 1)) {
                        neighbors[count] = gridToId(row, col);
                        count++;
                    }
                }
            }
        } else {
            neighbors = new int[5];
            for (int i = 0; i < 2; i++) {
                row = rowPoint - 1 + i;
                for (int j = 0; j < 3; j++) {
                    col = (((colPoint - 1 + j) % westToEast) + westToEast) % westToEast;
                    if (!(i == 1 && j == 1)) {
                        neighbors[count] = gridToId(row, col);
                        count++;
                    }
                }
            }
        }

        return neighbors;
    }

    /**
     * takes longitude and latitude of point
     *
     * @param longitude
     * @param latitude
     * @return closest vertex ID as integer array
     */
    public static int[] findVertexEquidistant(double longitude, double latitude) {
        int[] vertex = {0, 0};
        return vertex;
    }

    /**
     * computes the costs of each edge in grid graph
     * the first n - 2 alternate between horizontal and diagonal edge costs
     * the last two are the final horizontal and the constant vertical
     */
    private static void computeCosts() {
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Computing edge costs...");

        double latitude;
        double longitudeA = colToLongitude(0);
        double longitudeB = colToLongitude(1);
        costs = new double[(southToNorth - 1) * 2 + 2];
        double[] nVector;
        for (int row = 0; row < southToNorth - 1; row++) {
            latitude = rowToLatitude(row);

            // create N-Vector of reference point
            nVector = Geometry.getNVector(latitude, longitudeA);

            // horizontal
            costs[row * 2] = Geometry.getDistance(nVector, Geometry.getNVector(latitude, longitudeB));

            // diagonal
            costs[row * 2 + 1] = Geometry.getDistance(nVector, Geometry.getNVector(rowToLatitude(row + 1), longitudeB));
        }

        // last horizontal
        latitude = rowToLatitude(southToNorth - 1);
        nVector = Geometry.getNVector(latitude, longitudeA);
        costs[(southToNorth - 1) * 2] = Geometry.getDistance(nVector, Geometry.getNVector(latitude, longitudeB));

        // constant vertical
        costs[(southToNorth - 1) * 2 + 1] = Geometry.getDistance(nVector, Geometry.getNVector(rowToLatitude(southToNorth - 2), longitudeA));
    }

    private static double rowToLatitude(int row) {
        return (row + 1) * sideLength - 90;
    }

    private static double colToLongitude(int col) {
        return col * sideLength - 180;
    }

    public static int idToRow(int id) {
        return id % southToNorth;
    }
}