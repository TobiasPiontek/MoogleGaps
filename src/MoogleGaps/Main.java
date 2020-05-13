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




            System.out.println("\n\n\n\n\nWay is as followed: ");
            GeoJson.printWay(3);


        if (Geometry.inPolygon(Polygons.getLatitudes(), Polygons.getLongitudes(), -69.034, -68.36)) {
            System.out.println("Yup.");
        } else {
            System.out.println("Nope.");
        }
    }
}
