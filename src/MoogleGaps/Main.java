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
        //GeoJson.printWay(FileReader.getLongitudesOfWay(0),FileReader.getLatitudesOfWay(0));

        GeoJson.printWay(PolygonsV2.getPolygonLongitudes(54846), PolygonsV2.getPolygonLatitudes(54846));


        int size = 0;
        int index = 0;
        for (int i = 0; i < PolygonsV2.wayIds.size(); i++) {
            if (size < PolygonsV2.getPolygonLatitudes(i).length) {
                size = PolygonsV2.getPolygonLatitudes(i).length;
                index = i;
            }
        }
        System.out.println("largest is: " + index);

        if (Geometry.coordinateIsInsidePolygon(FileReader.getLongitudesOfWay(0), FileReader.getLatitudesOfWay(0), -69.02924537658691, -68.3538239160708)) {
            if (Geometry.pointInPoly(FileReader.getLongitudesOfWay(0), FileReader.getLatitudesOfWay(0), -69.04151916503906, -68.36142121194034)) {
                System.out.println("Yup.");
            } else {
                System.out.println("Nope.");
            }
        }
    }
}
