package MoogleGaps;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String filepath = CLInterface.getFilename(".pbf", "./OSMMapData");
        int gridGraphSize = CLInterface.enterGridGraphResolution();
        FileReader.readPbfFile(filepath);
        Polygons.createPolygons();
        GridGraph.generate(gridGraphSize);

        //For testing purposes
        //GeoJson.printGridGraph()
        //CLInterface.generateNavigationRoute();

        //Start of the Webserver
        WebServer.startWebServer();
    }
}
