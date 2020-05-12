package MoogleGaps;

import java.util.ArrayList;
import java.util.List;

public class Sort {


    //variables for recursive implementation
    private static boolean[] seenWays = new boolean[FileReader.wayIds.size()];  //also used for iterative way sort
    private static List<Long> nodeIds = new ArrayList<Long>();
    private static List<Integer> wayIds = new ArrayList<Integer>();

    //variables for iterative implementation

    public static ArrayList<Long> sortWays(ArrayList<Long> nodeIds, int wayId) {
        seenWays[wayId] = true;
        for (int i = 0; i < FileReader.wayIds.size(); i++) {

            if (!seenWays[i]) {
                if (nodeIds.get(nodeIds.size() - 1) == FileReader.getFirstNodeOfWay(i)) {

                    // append right
                    nodeIds.addAll(FileReader.getWayAtId(i));
                    return sortWays(nodeIds, i);
                } else if (nodeIds.get(0) == FileReader.getLastNodeOfWay(i)) {

                    // append left
                    ArrayList<Long> addLeft = FileReader.getWayAtId(i);
                    addLeft.addAll(0, FileReader.getWayAtId(i));
                    return sortWays(addLeft, i);
                }
            }
        }
        return nodeIds;
    }

    public static void completeWays() {
        for (int i = 0; i < seenWays.length-1; i++) {
            if (!seenWays[i]) {
                ArrayList<Long> way = sortWays(FileReader.getWayAtId(i), i);
                nodeIds.addAll(i, way);
                wayIds.add(way.size());
            }
        }

        for (int i = 0; i < wayIds.size(); i++) {
            System.out.println(wayIds.get(i));
        }
        System.out.println("Sort.wayIds.size() = " + wayIds.size());
     }



    public static void iterativeWaySort(){

        for(int i = 0; i<seenWays.length; i++){
            if(seenWays[i]){

            }

        }


    }

    private ArrayList<Integer> inputArray;

    public ArrayList<Integer> getSortedArray() {
        return inputArray;
    }

    public void MergeSort(ArrayList<Integer> inputArray){
        this.inputArray = inputArray;
    }

    public void sortGivenArray(){
        divide(0, this.inputArray.size()-1);
    }

    public void divide(int startIndex,int endIndex){

        //Divide till you breakdown your list to single element
        if(startIndex<endIndex && (endIndex-startIndex)>=1){
            int mid = (endIndex + startIndex)/2;
            divide(startIndex, mid);
            divide(mid+1, endIndex);

            //merging Sorted array produce above into one sorted array
            merger(startIndex,mid,endIndex);
        }
    }

    public void merger(int startIndex,int midIndex,int endIndex){

        //Below is the mergedarray that will be sorted array Array[i-midIndex] , Array[(midIndex+1)-endIndex]
        ArrayList<Integer> mergedSortedArray = new ArrayList<Integer>();

        int leftIndex = startIndex;
        int rightIndex = midIndex+1;

        while(leftIndex<=midIndex && rightIndex<=endIndex){
            if(inputArray.get(leftIndex)<=inputArray.get(rightIndex)){
                mergedSortedArray.add(inputArray.get(leftIndex));
                leftIndex++;
            }else{
                mergedSortedArray.add(inputArray.get(rightIndex));
                rightIndex++;
            }
        }

        //Either of below while loop will execute
        while(leftIndex<=midIndex){
            mergedSortedArray.add(inputArray.get(leftIndex));
            leftIndex++;
        }

        while(rightIndex<=endIndex){
            mergedSortedArray.add(inputArray.get(rightIndex));
            rightIndex++;
        }

        int i = 0;
        int j = startIndex;
        //Setting sorted array to original one
        while(i<mergedSortedArray.size()){
            inputArray.set(j, mergedSortedArray.get(i++));
            j++;
        }
    }

}
