package MoogleGaps;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public class GridGraph {

    public static boolean[] vertexData;
    private static int southToNorth;
    private static int westToEast;
    public static double[] costs;
    private static double sideLength;


    /**
     * takes n as integer for number of grid points to generate
     * fills boolean array vertexData with True for on land and False for in water
     *
     * @param n
     */
    public static void generate(int n) {
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Generating grid graph...");
        southToNorth = (int) Math.sqrt(n / 2.0);
        westToEast = 2 * southToNorth;
        sideLength = 180.0 / southToNorth;
        vertexData = new boolean[southToNorth * westToEast];
        double longitude;
        double latitude;

        /*String progressBar = "[";
        for (int i = 0; i < 100; i++) {
            progressBar += " ";
        }
        progressBar += "]";*/

        int percentage = 0;
        int fullLength = westToEast * southToNorth;
        float step = (float) (fullLength / 100.0);

        for (int i = 0; i < westToEast; i++) {
            longitude = colToLongitude(i);
            for (int j = 0; j < southToNorth; j++) {
                latitude = rowToLatitude(j);
                vertexData[i * southToNorth + j] = Geometry.pointInPolygonTest(longitude, latitude);

                if (i * j >= percentage * step) {
                    CLInterface.progressPercentage(i * j, fullLength);
                    percentage += 1;
                }
            }
        }
        CLInterface.progressPercentage(1, 1);
        System.out.println("southToNorth = " + southToNorth + ", westToEast = " + westToEast + ", vertexData.length = " + vertexData.length);
        computeCosts();
    }

    public static int findVertex(double longitude, double latitude) {
        int row = (int) ((latitude + 90.0) / sideLength);
        if (row == southToNorth) {
            row--;
        }
        int col = (int) ((longitude + 180) / sideLength);
        if (col == westToEast) {
            col--;
        }
        return gridToId(row, col);
    }

    public static int findVertexInWater(int vertexId) {
        if (!vertexData[vertexId]) {
            return vertexId;
        } else {
            int loopCounter = 1;
            while (true) {
                vertexId = moveNorth(vertexId);
                if (vertexSurroundedByWater(vertexId)) return vertexId;
                for (int i = 0; i < loopCounter; i++) {
                    vertexId = moveEast(vertexId);
                    if (vertexSurroundedByWater(vertexId)) return vertexId;
                }
                for (int i = 0; i < 2 * loopCounter; i++) {
                    vertexId = moveSouth(vertexId);
                    if (vertexSurroundedByWater(vertexId)) return vertexId;
                }
                for (int i = 0; i < 2 * loopCounter; i++) {
                    vertexId = moveWest(vertexId);
                    if (vertexSurroundedByWater(vertexId)) return vertexId;
                }
                for (int i = 0; i < 2 * loopCounter; i++) {
                    vertexId = moveNorth(vertexId);
                    if (vertexSurroundedByWater(vertexId)) return vertexId;
                }
                for (int i = 0; i < loopCounter; i++) {
                    vertexId = moveEast(vertexId);
                }
            }
        }
    }

    private static boolean vertexSurroundedByWater(int vertexId) {
        if (!vertexData[vertexId]) {
            if (Navigation.isSurroundedByWater(vertexId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param vertexId
     * @return ID of point north of vertexId if possible
     */
    public static int moveNorth(int vertexId) {
        int row = idToRow(vertexId);
        if (row >= southToNorth - 1) {
            return vertexId;
        } else {
            vertexId = gridToId(row + 1, idToCol(vertexId));
            return vertexId;
        }
    }

    /**
     * @param vertexId
     * @return ID of point south of vertexId if possible
     */
    public static int moveSouth(int vertexId) {
        int row = idToRow(vertexId);
        if (row <= 0) {
            return vertexId;
        } else {
            vertexId = gridToId(row - 1, idToCol(vertexId));
            return vertexId;
        }
    }

    /**
     * @param vertexId
     * @return ID of point west of vertexId with looping around
     */
    public static int moveWest(int vertexId) {
        int col = idToCol(vertexId);
        vertexId = gridToId(idToRow(vertexId), (((col - 1) % westToEast) + westToEast) % westToEast);
        return vertexId;
    }

    /**
     * @param vertexId
     * @return ID of point east of vertexId with looping around
     */
    public static int moveEast(int vertexId) {
        int col = idToCol(vertexId);
        vertexId = gridToId(idToRow(vertexId), (((col + 1) % westToEast) + westToEast) % westToEast);
        return vertexId;
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
     * takes row and column of vertex ID
     *
     * @param index
     * @return IDs of neighboring vertices as integer array
     */
    public static int[] getNeighbors(int index) {
        int[] neighbors;
        int rowPoint = idToRow(index);
        int colPoint = idToCol(index);
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
        } else if (rowPoint == 0) {
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

    public static double rowToLatitude(int row) {
        return (row + 0.5) * sideLength - 90;
    }

    public static double colToLongitude(int col) {
        return (col + 0.5) * sideLength - 180;
    }

    public static int idToRow(int id) {
        return id % southToNorth;
    }

    public static int idToCol(int id) {
        return id / southToNorth;
    }

    public static double idToLongitude(int nodeId) {
        return colToLongitude(idToCol(nodeId));
    }

    public static double idToLatitude(int nodeId) {
        return rowToLatitude(idToRow(nodeId));
    }

    public static double[] idToLongitude(ArrayList<Integer> nodeIds) {
        double[] longitudeCoordinates = new double[nodeIds.size()];
        for (int i = 0; i < nodeIds.size(); i++) {
            //Block to fix ugly ways at the border section
            longitudeCoordinates[i] = idToLongitude(nodeIds.get(i));
            if (i > 0) {
                if (Math.abs(longitudeCoordinates[i - 1] - idToLongitude(nodeIds.get(i))) > sideLength * 2) {
                    if (idToLongitude(nodeIds.get(i)) < 0) {
                        longitudeCoordinates[i] = idToLongitude(nodeIds.get(i)) + 360;
                    } else {
                        longitudeCoordinates[i] = idToLongitude(nodeIds.get(i)) - 360;
                    }
                }
            }
        }
        return longitudeCoordinates;
    }

    public static double[] idToLatitude(ArrayList<Integer> nodeIds) {
        double[] latitudeCoordinates = new double[nodeIds.size()];
        for (int i = 0; i < nodeIds.size(); i++) {
            latitudeCoordinates[i] = idToLatitude(nodeIds.get(i));
        }
        return latitudeCoordinates;
    }

    /**
     * serializes vertexData into n_vertex_data_hxw.ser
     */
    public static void serialize(String filepath) {
        try {
            FileOutputStream fileOut = new FileOutputStream("./OSMCacheData/" + vertexData.length + "[" + filepath + "]" + southToNorth + "x" + westToEast + ".ser");
            //FileOutputStream fileOut = new FileOutputStream("./OSMCacheData/vertex_data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(vertexData);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * deserializes n_vertex_data_hxw.ser into vertexData
     */
    public static void deserialize(String filepath) {
        try {
            //FileInputStream fileIn = new FileInputStream(name + ".ser");
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            vertexData = (boolean[]) in.readObject();
            in.close();
            fileIn.close();
            String[] numbers = filepath.replaceAll("^\\D+", "").split("\\D+");
            southToNorth = Integer.parseInt(numbers[1]);
            westToEast = Integer.parseInt(numbers[2]);
            sideLength = 180.0 / southToNorth;
            computeCosts();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Error");
            c.printStackTrace();
            return;
        }
    }
}