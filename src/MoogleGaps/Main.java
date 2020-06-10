package MoogleGaps;


import java.sql.Timestamp;

public class Main {

    public static void main(String[] args) {
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));
        Polygons.createPolygons();
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Generating grid graph...");
        GridGraph.generate(10000);
        // GeoJson.printGridGraph();
        // System.out.println(new Timestamp(System.currentTimeMillis()) + " Done");
        CLInterface.generateNavigationRoute();
    }
}
