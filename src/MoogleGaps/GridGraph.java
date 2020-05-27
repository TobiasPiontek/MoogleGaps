package MoogleGaps;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.Arrays;

public class GridGraph {

    static ArrayList<Boolean> vertexData;
    static int[] firstIndexOf;
    static double r = 1;
    static int nCount = 0;
    static double mTheta;
    static double mPhi;

    public static void generate(int n) {
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
                vertexData.add(gridToId(m, i), Geometry.pointInPolygonTest(longitude, latitude));
                System.out.println(gridToId(m, i));
                nCount++;
            }
        }
    }

    /**
     * converts grid coordinates to IDs
     * @param m
     * @param n
     * @return ID of point as integer
     */
    public static int gridToId (int m, int n) {
        return firstIndexOf[m] + n;
    }

    /**
     * converts IDs to grid coordinates
     * @param id
     * @return grid coordinates as integer array
     */
    public static int[] idToGrid(int id) {
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
    public static int[] findVertex (double longitude, double latitude) {
        int[] vertex = {0, 0};
        return vertex;
    }
}
