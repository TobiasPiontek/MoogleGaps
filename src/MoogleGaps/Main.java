package MoogleGaps;


import java.sql.Timestamp;

public class Main {

    public static void main(String[] args) {
        String filepath = CLInterface.getFilename(".pbf", "./OSMMapData");
        int gridGraphSize = CLInterface.enterGridGraphResolution();
        FileReader.readPbfFile(filepath);
        Polygons.createPolygons();
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Generating grid graph...");
        GridGraph.generate(gridGraphSize);
        //GeoJson.printGridGraph()
        CLInterface.generateNavigationRoute();
    }
}
