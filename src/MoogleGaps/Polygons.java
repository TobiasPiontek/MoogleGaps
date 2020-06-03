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
                ArrayList<Integer> nodesIndexToAppend = new ArrayList<Integer>();

                //check for right side
                boolean foundRight = true;
                //System.out.println("Cycle: " + i);
                while(foundRight){
                    foundRight = false;
                    for(int j = 0 ; j < FileReader.wayIds.size(); j++){
                        if(!waysUsed[j] && end == startNodes[j]){
                            nodesIndexToAppend.add(j);
                            end = endNodes[j];
                            waysUsed[j]= true;
                            foundRight = true;
                            break;
                        }
                        if(start == end){
                            break;
                        }
                    }
                }

                //Check for the left side
                boolean foundLeft = true;
                while(foundLeft){
                    foundLeft = false;
                    for(int j = 0 ; j < FileReader.wayIds.size(); j++){
                        if(!waysUsed[j] && start == endNodes[j]){
                            start =  startNodes[j];
                            waysUsed[j]= true;
                            foundLeft=true;
                            break;
                        }
                        if(start == end){
                            break;
                        }
                    }
                }
                if(start == end){
                    writeCycleToPolygonList(nodesIndexToAppend);
                }
                else{
                    System.out.println("Way of length " + nodesIndexToAppend.size());
                }

            }
        }

        System.out.println("Polygon count is " + wayIds.size());

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
                    latitudes[j + coordinatesSize]= FileReader.getLatitudesOfWay(i)[j];
                    longitudes[j + coordinatesSize] = FileReader.getLongitudesOfWay(i)[j];
                }

                coordinatesSize += FileReader.getLengthOfWay(i);
                waysUsed[i] = true;
            }
        }
    }

    private static void writeCycleToPolygonList(ArrayList<Integer> idsToAdd){
        int cursor = coordinatesSize;
        if(wayIds.isEmpty()){
            wayIds.add(0);
        }else{
            wayIds.add(coordinatesSize);
        }
        int k = coordinatesSize;
        for(int i = 0; i < idsToAdd.size();i++){
            double[] polygonLatitudes=FileReader.getLatitudesOfWay(idsToAdd.get(i));
            double[] polygonLongitudes=FileReader.getLongitudesOfWay(idsToAdd.get(i));

            //intentionally leaving the last node out to prevent double detections
            for(int j = 0; j < FileReader.getLengthOfWay(idsToAdd.get(i))-1; j++){
                double test = polygonLatitudes[j];
                double testtest = polygonLongitudes[j];
                latitudes[k]=polygonLatitudes[j];
                longitudes[k]=polygonLongitudes[j];
                k++;
            }
        }
        coordinatesSize=k;
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
