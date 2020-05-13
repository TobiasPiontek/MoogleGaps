package MoogleGaps;

import java.util.ArrayList;

public class Polygons {

    //public ArrayList<Double> latitudes;
    //public ArrayList<Double> longitudes;

    public static double[] getLatitudes() {
        double[] latitudes = FileReader.getLatitudesOfWay(FileReader.wayIds.get(0));
        double[] realLatitudes = new double[latitudes.length - 1];
        for (int i = 1; i < latitudes.length; i++) {
            realLatitudes[i - 1] = latitudes[i];
        }
        System.out.println(realLatitudes[0]);
        return realLatitudes;
    }

    public static double[] getLongitudes() {
        double[] longitudes = FileReader.getLongitudesOfWay(FileReader.wayIds.get(0));
        double[] realLongitudes = new double[longitudes.length - 1];
        for (int i = 1; i < longitudes.length; i++) {
            realLongitudes[i - 1] = longitudes[i];
        }
        System.out.println(realLongitudes[0]);
        return realLongitudes;
    }
}
