package MoogleGaps;

import java.io.File;
import java.util.ArrayList;

public class PolygonsV2 {
    //store the coordinates of the polygons
    static double[] latitudes = new double[FileReader.latitudes.length];
    static double[] longitudes = new double[FileReader.longitudes.length];

    static ArrayList<Integer> wayIds = new ArrayList<Integer>();

    public static void createPolygons(){

        System.out.println("Starting the create Polygons Method!");

        long[] startNodes = new long[FileReader.wayIds.size()];
        long[] endNodes = new long[FileReader.wayIds.size()];

        //memorize wether a way has already been used or not
        boolean[] waysUsed = new boolean[FileReader.wayIds.size()];

        // fill arrays with start and end nodes
        for(int i = 0; i < FileReader.wayIds.size(); i++){
            startNodes[i] = FileReader.getFirstNodeOfWay(i);
            endNodes[i] = FileReader.getLastNodeOfWay(i);
        }

        int checkSize=0;
        for(int i = 0; i < FileReader.wayIds.size();i++){
            if(startNodes[i]==endNodes[i]){

                if(wayIds.isEmpty()){
                    wayIds.add(0);
                }else{
                    wayIds.add(checkSize);
                }

                for(int j = 0; j < FileReader.getLongitudesOfWay(i).length;  j++){
                    latitudes[j+checkSize]= FileReader.getLatitudesOfWay(i)[j];
                    longitudes[j+checkSize] = FileReader.getLongitudesOfWay(i)[j];
                }

                checkSize += FileReader.getLengthOfWay(i);
                waysUsed[i] = true;
            }
        }

        System.out.println("wayids at 0= " + wayIds.get(0) + "Wayids at 1= " + wayIds.get(1));

        for(int i = 0; i < 1; i++){
            for(int j = wayIds.get(i); j < wayIds.get(i+1); j++){
                System.out.println("Latitude: " + latitudes[j] + " longitudes: " + longitudes[j]);
            }

        }


        }

        //Start and Endnodes are now filled in the list

    public static void getPolygonLongitudes(int i){
        
    }
    public static void getPolygonLatitudes(int i){

    }


}
