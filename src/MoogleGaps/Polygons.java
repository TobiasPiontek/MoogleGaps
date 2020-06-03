package MoogleGaps;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Polygons {
    //store the coordinates of the polygons
    static double[] latitudes = new double[FileReader.latitudes.length];
    static double[] longitudes = new double[FileReader.longitudes.length];

    private static boolean[] waysUsed = new boolean[FileReader.wayIds.size()];
    private static long[] startNodes = new long[FileReader.wayIds.size()];
    private static long[] endNodes = new long[FileReader.wayIds.size()];

    static ArrayList<Integer> wayIds = new ArrayList<Integer>();    //saves the start Ids of the Ways
    private static int coordinatesSize =0;      //A Cursor to know where the next Polygon has to be written in the Array

    public static void createPolygons(){
        System.out.println("Start of simple polygon detection..." + new Timestamp(System.currentTimeMillis()));
        createStartAndEndNodeArray();
        simpleCycleDetection();
        System.out.println(wayIds.size() + " Polygons detected with simple Circle detection " + new Timestamp(System.currentTimeMillis()));


        for(int i = 0 ; i < FileReader.wayIds.size(); i++){
            if(!waysUsed[i]){
                long start = startNodes[i];
                long end = endNodes[i];
                boolean foundRight = true;
                //System.out.println("Cycle: " + i);
                int count = 0;
                while(foundRight){
                    foundRight = false;
                    for(int j = 0 ; j < FileReader.wayIds.size(); j++){
                        if(!waysUsed[j] && end == startNodes[j]){
                            count ++;
                            //System.out.println("with " + j + "Glued together!");
                            //System.out.println("Total start was: " + start + "end was: " + end + " start was: " + startNodes[j] + " new End is: " + endNodes[j]);
                            end = endNodes[j];
                            waysUsed[j]= true;
                            foundRight = true;
                            break;
                        }
                    }
                }
                boolean foundLeft = true;
                while(foundLeft){
                    foundLeft = false;
                    for(int j = 0 ; j < FileReader.wayIds.size(); j++){
                        if(!waysUsed[j] && start == endNodes[j]){
                            count ++;
                            start =  startNodes[j];
                            waysUsed[j]= true;
                            foundLeft=true;
                            break;
                        }
                    }
                }

            System.out.println("Length is: " + count);
                if(start == end){
                    System.out.println("2 Edge cycle found: " + i + " length " + count);
                }
            }


        }

        System.out.println("End of MultiwayPolygon detection " + new Timestamp(System.currentTimeMillis()));

    }




    /**
     * Used to detect Polygons simply by choosing Ways where the end and startnode are equal
     */
    private static void simpleCycleDetection() {
        //memorize wether a way has already been used or not
        // fill arrays with start and end nodes
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
    }

    private static void createStartAndEndNodeArray() {
        for(int i = 0; i < FileReader.wayIds.size(); i++){
            startNodes[i] = FileReader.getFirstNodeOfWay(i);
            endNodes[i] = FileReader.getLastNodeOfWay(i);
        }
    }


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
