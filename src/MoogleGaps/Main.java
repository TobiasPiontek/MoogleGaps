package MoogleGaps;


import java.sql.Timestamp;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));
        Polygons.createPolygons();
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Start of grid graph generation");
        GridGraph.generate(10000);
        //GeoJson.printGridGraph();
        System.out.println(new Timestamp(System.currentTimeMillis()) + " done...");
        CLInterface.generateNavigationRoute();
    }
}
