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

        //System.out.println("\n\n\n\n\nWay is as followed: ");
        //GeoJson.printWay(FileReader.getLongitudesOfWay(0),FileReader.getLatitudesOfWay(0));

        //GeoJson.printWay(Polygons.getPolygonLongitudes(10), Polygons.getPolygonLatitudes(10));


        int size = 0;
        int index = 0;
        for (int i = 0; i < Polygons.wayIds.size(); i++) {
            if (size < Polygons.getPolygonLatitudes(i).length) {
                size = Polygons.getPolygonLatitudes(i).length;
                index = i;
            }
        }


        // 54846
        System.out.println("largest is: " + index);
        System.out.println("with Polygoncount " + Polygons.getPolygonLatitudes(index).length);

        //GeoJson.printWay(Polygons.getPolygonLongitudes(6220),Polygons.getPolygonLatitudes(6220));


        System.out.println(new Timestamp(System.currentTimeMillis()));


        /*
        GridGraph.generate(648);

        ArrayList<Double> longitudes = new ArrayList<>();
        ArrayList<Double> latitudes = new ArrayList<>();
        int[] gridCoordinates = new int[2];
        double[] coordinates = new double[2];

        for (int i = 0; i < GridGraph.vertexData.length; i++) {
            gridCoordinates = GridGraph.idToGrid(i);
            if (GridGraph.vertexData[i]) {
                coordinates = GridGraph.gridToCoordinates(gridCoordinates[0], gridCoordinates[1]);
                longitudes.add(coordinates[0]);
                latitudes.add(coordinates[1]);
            }
        }
        GeoJson.printNodes(longitudes.stream().mapToDouble(Double::doubleValue).toArray(), latitudes.stream().mapToDouble(Double::doubleValue).toArray());
        */

    }
}
