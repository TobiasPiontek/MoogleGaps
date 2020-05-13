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







        for(int i = 0; i < 1; i++){
            System.out.println("\n\n\n\n\nWay " + i + " is as followed: ");
            GeoJson.printWay(i);
        }








        if (Geometry.inPolygon(Polygons.getLatitudes(), Polygons.getLongitudes(), -69.034, -68.36)) {
            System.out.println("Yup.");
        } else {
            System.out.println("Nope.");
        }
    }
}
