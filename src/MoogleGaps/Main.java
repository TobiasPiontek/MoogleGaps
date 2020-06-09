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
        /*
        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<Double> latitudes = new ArrayList<>();
        int row;
        int col;
        double latitude;
        double longitude;

        for (int i = 0; i < GridGraph.vertexData.length; i++) {
            row = GridGraph.idToRow(i);
            col = GridGraph.idToCol(i);
            if (!GridGraph.vertexData[i]) {
                longitude = GridGraph.colToLongitude(col);
                longitudes.add(longitude);
                latitude = GridGraph.rowToLatitude(row);
                latitudes.add(latitude);
            }
        }
        GeoJson.printNodes(longitudes.stream().mapToDouble(Double::doubleValue).toArray(), latitudes.stream().mapToDouble(Double::doubleValue).toArray());
        */

        // Test point in Antarctica
        /*
        if(Geometry.pointInPolygonTest(-167.7273, -85.9091))
            System.out.println("True");
        else{
            System.out.println("False");
        }
         */

        /*
        for (int i = 0; i < GridGraph.costs.length; i++) {
            System.out.println(GridGraph.costs[i]);
        }
        */

        /**
         * upper left corner: lat: 90 ; long = -180
         */

        //Code to Print a route


        int source = GridGraph.findVertex(-107, -75);
        int target = GridGraph.findVertex(-55, -70);

        Navigation.dijkstra(source, target);
        ArrayList<Integer> way = Navigation.getWay(source, target);

        int row;
        int col;
        double longitude;
        double latitude;
        double[] longitudes = new double[way.size()];
        double[] latitudes = new double[way.size()];;
        for (int i = 0; i < way.size(); i++) {
            row = GridGraph.idToRow(way.get(i));
            col = GridGraph.idToCol(way.get(i));
            longitude = GridGraph.colToLongitude(col);
            latitude = GridGraph.rowToLatitude(row);
            longitudes[i] = longitude;
            latitudes[i] = latitude;
        }

        GeoJson.printPolyline(longitudes, latitudes);



        //CLInterface.generateNavigationRoute();


    }
}
