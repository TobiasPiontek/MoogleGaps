package MoogleGaps;


import java.sql.Timestamp;
import java.util.ArrayList;

public class Main {

    static ArrayList<Long> startNodes;
    static ArrayList<Long> endNodes;
    static long[] orderedNodes;

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));
        Polygons.createPolygons();

        GridGraph.generate(648);

        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<Double> latitudes = new ArrayList<>();
        int[] gridCoordinates;
        double[] coordinates;

        for (int i = 0; i < GridGraph.vertexData.length; i++) {
            gridCoordinates = GridGraph.idToGrid(i);
            if (GridGraph.vertexData[i]) {
                coordinates = GridGraph.gridToCoordinates(gridCoordinates[0], gridCoordinates[1]);
                longitudes.add(coordinates[0]);
                latitudes.add(coordinates[1]);
            }
        }
        GeoJson.printNodes(longitudes.stream().mapToDouble(Double::doubleValue).toArray(), latitudes.stream().mapToDouble(Double::doubleValue).toArray());

        System.out.println(new Timestamp(System.currentTimeMillis()));
    }
}
