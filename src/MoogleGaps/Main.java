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

        GeoJson.printWay(Polygons.getPolygonLongitudes(54846), Polygons.getPolygonLatitudes(54846));

        /*
        int size = 0;
        int index = 0;
        for (int i = 0; i < Polygons.wayIds.size(); i++) {
            if (size < Polygons.getPolygonLatitudes(i).length) {
                size = Polygons.getPolygonLatitudes(i).length;
                index = i;
            }
        }
        */

        // 54846
        // System.out.println("largest is: " + index);

        if (Geometry.pointInPolygonTest(42.74353265762329, 13.997037155092595)) {
            System.out.println("Land ahoy!");
        } else {
            System.out.println("Splash!");
        }

        System.out.println(new Timestamp(System.currentTimeMillis()));

        for(int i = 0; i < 100; i++) {
            if (Geometry.pointInPolygonTest(42.64353265762329, 13.997037155092595)) {
                System.out.println("Land ahoy!");
            } else {
                //System.out.println("Splash!");
            }
        }

        System.out.println(new Timestamp(System.currentTimeMillis()));

    }
}
