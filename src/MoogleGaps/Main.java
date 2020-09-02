package MoogleGaps;


import java.io.File;

public class Main {

    public static void main(String[] args) {
        if (CLInterface.generateNewGridGraph()) {
            String filepath = CLInterface.getFilename(".pbf", "./OSMMapData");
            int gridGraphSize = CLInterface.enterGridGraphResolution();
            FileReader.readPbfFile(filepath);
            Polygons.createPolygons();
            GridGraph.generate(gridGraphSize);

            File f = new File(filepath);
            GridGraph.serialize(f.getName());
        } else {
            GridGraph.deserialize(CLInterface.getFilename(".ser", "./OSMCacheData"));
        }

        //For testing purposes
        //GeoJson.printGridGraph()
        //CLInterface.generateNavigationRoute();

        //Start of the Webserver
        WebServer.startWebServer();
    }
}
