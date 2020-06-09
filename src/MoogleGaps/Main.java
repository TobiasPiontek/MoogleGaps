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


        //Code to see Grid graph nodes

        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<Double> latitudes = new ArrayList<>();
        int[] gridCoordinates;
        double[] coordinates;

        for (int i = 0; i < GridGraph.vertexData.length; i++) {
            gridCoordinates = GridGraph.idToGrid(i);
            if (!GridGraph.vertexData[i]) {
                coordinates = GridGraph.gridToCoordinates(gridCoordinates[0], gridCoordinates[1]);
                longitudes.add(coordinates[0]);
                latitudes.add(coordinates[1]);
            }
        }
        GeoJson.printNodes(longitudes.stream().mapToDouble(Double::doubleValue).toArray(), latitudes.stream().mapToDouble(Double::doubleValue).toArray());






        /*
        for (int i = 0; i < GridGraph.costs.length; i++) {
            System.out.println(GridGraph.costs[i]);
        }
        */

        /**
         * upper left corner: lat: 90 ; long = -180
         */

        //Code to Print a route
        /*

        int source = GridGraph.findVertex(-107, -75);
        int target = GridGraph.findVertex(-55, -70);


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

        */

        System.out.println(new Timestamp(System.currentTimeMillis()));
    }
}
