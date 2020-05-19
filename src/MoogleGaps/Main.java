package MoogleGaps;

import com.sun.nio.sctp.SendFailedNotification;

import java.io.File;
import java.util.ArrayList;

public class Main {

    static ArrayList<Long> startNodes;
    static ArrayList<Long> endNodes;
    static long[] orderedNodes;

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        FileReader.readPbfFile(CLInterface.getFilename(".pbf", "./OSMMapData"));
        PolygonsV2.createPolygons();

        //System.out.println("\n\n\n\n\nWay is as followed: ");
        GeoJson.printWay(FileReader.getLongitudesOfWay(4),FileReader.getLatitudesOfWay(4));

        //GeoJson.printWay(PolygonsV2.getPolygonLongitudes(3),PolygonsV2.getPolygonLatitudes(3) );



        if (Geometry.coordinateIsInsidePolygon(FileReader.getLongitudesOfWay(0), FileReader.getLatitudesOfWay(0),  -69.02924537658691, -68.3538239160708)) {
            System.out.println("Yup.");
        } else {
            System.out.println("Nope.");
        }
    }
}
