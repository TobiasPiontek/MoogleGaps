package MoogleGaps;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PolygonsV2 {
    //store the coordinates of the polygons
    static double[] latitudes = new double[FileReader.latitudes.length];
    static double[] longitudes = new double[FileReader.longitudes.length];

    static ArrayList<Integer> wayIds = new ArrayList<Integer>();
    private static int coordinatesSize =0;

    public static void createPolygons(){
        System.out.println("Start of simple Polygon detection... " + new Timestamp(System.currentTimeMillis()));

        long[] startNodes = new long[FileReader.wayIds.size()];
        long[] endNodes = new long[FileReader.wayIds.size()];

        //memorize wether a way has already been used or not
        boolean[] waysUsed = new boolean[FileReader.wayIds.size()];

        // fill arrays with start and end nodes
        for(int i = 0; i < FileReader.wayIds.size(); i++){
            startNodes[i] = FileReader.getFirstNodeOfWay(i);
            endNodes[i] = FileReader.getLastNodeOfWay(i);
        }

        for(int i = 0; i < FileReader.wayIds.size();i++){
            if(startNodes[i]==endNodes[i]){

                if(wayIds.isEmpty()){
                    wayIds.add(0);
                }else{
                    wayIds.add(coordinatesSize);
                }
                for(int j = 0; j < FileReader.getLengthOfWay(i);  j++){
                    if(FileReader.getLatitudesOfWay(i)[j] < 0.1 && FileReader.getLatitudesOfWay(i)[j]>-0.1){
                    }
                    latitudes[j + coordinatesSize]= FileReader.getLatitudesOfWay(i)[j];
                    longitudes[j + coordinatesSize] = FileReader.getLongitudesOfWay(i)[j];
                }

                coordinatesSize += FileReader.getLengthOfWay(i);
                waysUsed[i] = true;
            }
        }
        System.out.println("End of Simple Polygon detection Stage..." + new Timestamp(System.currentTimeMillis()));

        }

        //Start and Endnodes are now filled in the list


    /**
     * @param index The index of the Polygon in the List
     * @return an Array of all Longitude coordinates
     */
    public static double[] getPolygonLongitudes(int index){
        double[] polygonLongitudes = new double[getWayLength(index)];

        int k = 0;
        int startIndex= wayIds.get(index);
        for(int j = 0; j < getWayLength(index); j++){
            polygonLongitudes[k]=longitudes[j + startIndex];
            k++;
        }
        return polygonLongitudes;
    }

    /**
     * @param index The index of the Polygon in the List
     * @return an Array of all Latitude coordinates
     */
    public static double[] getPolygonLatitudes(int index){
        double[] polygonLatitudes = new double[getWayLength(index)];

        int k = 0;
        int startIndex= wayIds.get(index);
        for(int j = 0; j < getWayLength(index); j++){
            polygonLatitudes[k]=latitudes[j + startIndex];
            k++;
        }
        return polygonLatitudes;
    }

    //helper Method to get the Length of an
    private static int getWayLength(int i){
        if(i < wayIds.size() -1 ){
            return wayIds.get(i+1)-wayIds.get(i);
        }else{
            return coordinatesSize - wayIds.get(i);
        }

    }

}
