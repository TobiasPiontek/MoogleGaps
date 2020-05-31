package MoogleGaps;

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
     * @param n
     */
    public static void generate(int n) {
        southToNorth = (int) Math.sqrt(n / 2);
        westToEast = southToNorth * 2;
        vertexData = new boolean[southToNorth * westToEast];
        System.out.println("southToNorth = " + southToNorth);
        System.out.println("westToEast = " + westToEast);
        System.out.println("vertexData.length = " + vertexData.length);
        for (int i = 0; i < westToEast; i++) {
            double longitude = i * 360 / westToEast - 180;
            for (int j = 0; j < southToNorth; j++) {
                double latitude = j * 180 / southToNorth - 90;
                //System.out.println("(" + longitude + ", " + latitude + ")");
                //System.out.println(i * southToNorth + j);
                vertexData[i * southToNorth + j] = Geometry.pointInPolygonTest(longitude, latitude);
            }
        }
    }

    public static int findVertex(double longitude, double latitude) {
        double sideLength = 180 / southToNorth;
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
        double sideLength = 180 / southToNorth;
        double longitude = col * sideLength - 180;
        double latitude = row * sideLength - 90;
        double[] coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;
        return coordinates;
    }

    /**
     * converts grid coordinates to IDs
     * @param m
     * @param n
     * @return ID of point as integer
     */
    public static int gridToIdEquidistant(int m, int n) {
        return firstIndexOf[m] + n;
    }

    /**
     * converts IDs to grid coordinates
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
     * @param mn
     * @return coordinates of vertex as double array {longitude, latitude}
     */
    public static double[] getCoordinates (int[] mn) {
        double[] coordinates = new double[2];

        double phi = 2 * Math.PI * mn[1] / mPhi;
        coordinates[0] = phi - Math.PI;

        double theta = Math.PI * (mn[0] + 0.5) / mTheta;
        coordinates[1] = theta - (Math.PI / 2);
        return coordinates;
    }

    /**
     * takes row and column of vertex ID
     * @param row
     * @param col
     * @return IDs of neighboring vertices as 2D integer array
     */
    public static int[][] getNeighbors (int row, int col) {
        int[][] neighbors = {{0, 0}, {1, 1}};
        return neighbors;
    }

    /**
     * takes longitude and latitude of point
     * @param longitude
     * @param latitude
     * @return closest vertex ID as integer array
     */
    public static int[] findVertexEquidistant(double longitude, double latitude) {
        int[] vertex = {0, 0};
        return vertex;
    }
}
