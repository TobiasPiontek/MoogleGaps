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

        GridGraph.generate(1000);

        /*
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<Double> latitudes = new ArrayList<>();
        int[] gridCoordinates;
        double[] coordinates;

        for (int i = 0; i < GridGraph.vertexData.length; i++) {
            gridCoordinates = GridGraph.idToGrid(i);
            //if (GridGraph.vertexData[i]) {
                coordinates = GridGraph.gridToCoordinates(gridCoordinates[0], gridCoordinates[1]);
                longitudes.add(coordinates[0]);
                latitudes.add(coordinates[1]);
            //}
        }
        GeoJson.printNodes(longitudes.stream().mapToDouble(Double::doubleValue).toArray(), latitudes.stream().mapToDouble(Double::doubleValue).toArray());
        */

        /*
        int neighborsA[] = GridGraph.getNeighbors(0);
        for (int i = 0; i < neighborsA.length; i++) {
            System.out.println("index = 0, neighbors[" + i + "] = " + neighborsA[i]);
        }
        */

        /*
        int neighborsB[] = GridGraph.getNeighbors(500);
        for (int i = 0; i < neighborsB.length; i++) {
            System.out.println("index = 500, neighbors[" + i + "] = " + neighborsB[i]);
        }
        */

        /*
        for (int i = 0; i < GridGraph.costs.length; i++) {
            System.out.println(GridGraph.costs[i]);
        }
        */

        System.err.println("These variables should generate the startnode");
        System.err.println("First vertex: " + GridGraph.findVertex(-180,-90));
        System.err.println("Center vertex: " + GridGraph.findVertex(0,0));
        System.err.println("Last vertex: " + GridGraph.findVertex(180,90));

        int source = GridGraph.findVertex(-82.96875, -71.41317683396565);
        int target = GridGraph.findVertex(-42.890625, -75.05035357407698);
        Navigation.dijkstra(source, target);
        ArrayList<Integer> way = Navigation.getWay(source, target);

        int[] gridCoordinates;
        double[] coordinates;
        double[] longitudes = new double[way.size()];
        double[] latitudes = new double[way.size()];;
        for (int i = 0; i < way.size(); i++) {
            gridCoordinates = GridGraph.idToGrid(way.get(i));
            coordinates = GridGraph.gridToCoordinates(gridCoordinates[0], gridCoordinates[1]);
            longitudes[i] = coordinates[0];
            latitudes[i] = coordinates[1];
        }

        GeoJson.printPolyline(longitudes, latitudes);


        System.out.println(new Timestamp(System.currentTimeMillis()));
    }
}
