package MoogleGaps;

import java.util.ArrayList;

public class PolygonsV2 {
    //store the coordinates of the polygons
    static double[] latitudes = new double[FileReader.latitudes.length];
    static double[] longitudes = new double[FileReader.longitudes.length];

    static ArrayList<Integer> wayIds = new ArrayList<Integer>();
    private static int coordinatesSize =0;

    public static void createPolygons(){

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
                    //System.out.println("WayIndex:" + i +" Startindex:" + coordinatesSize + " length of this way " + FileReader.getLengthOfWay(i));
                }

                for(int j = 0; j < FileReader.getLengthOfWay(i);  j++){
                    //System.out.println("Filled coordinate: " + (j + coordinatesSize));
                    latitudes[j + coordinatesSize]= FileReader.getLatitudesOfWay(i)[j];
                    longitudes[j + coordinatesSize] = FileReader.getLongitudesOfWay(i)[j];
                }

                coordinatesSize += FileReader.getLengthOfWay(i);
                waysUsed[i] = true;
            }
        }

        }

        //Start and Endnodes are now filled in the list


    /**
     * @param i The index of the Polygon in the List
     * @return an Array of all Longitude coordinates
     */
    public static double[] getPolygonLongitudes(int i){
        double[] polygonLongitudes = new double[getWayLength(i)];

        int k = 0;
        for(int j = wayIds.get(i); j < getWayLength(i); j++){
            polygonLongitudes[k++]=longitudes[j];
        }
        return polygonLongitudes;
    }

    /**
     * @param i The index of the Polygon in the List
     * @return an Array of all Latitude coordinates
     */
    public static double[] getPolygonLatitudes(int i){
        double[] polygonLatitudes = new double[getWayLength(i)];

        int k = 0;
        for(int j = wayIds.get(i); j < getWayLength(i); j++){
            polygonLatitudes[k++]=latitudes[j];
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
