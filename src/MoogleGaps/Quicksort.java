package MoogleGaps;

import java.util.ArrayList;
import java.util.Collections;

public class Quicksort {


    private static int partition(ArrayList<Long> array, ArrayList<Integer> index, int begin, int end) {
        int pivot = end;

        int counter = begin;
        for (int i = begin; i < end; i++) {
            if (array.get(i) < array.get(pivot)) {
                long temp = array.get(counter);
                int tempindex = index.get(counter);
                array.set(counter,array.get(i));
                index.set(counter,index.get(i));
                array.set(i,temp);
                index.set(i, tempindex);
                counter++;
            }
        }
        long temp = array.get(pivot);
        int tempindex = index.get(pivot);
        array.set(pivot,array.get(counter));
        index.set(pivot, index.get(counter));
        array.set(counter,temp);
        index.set(counter,tempindex);

        return counter;
    }

    /**
     * helper Method to invert the index to complete the lookuptable
     * @param index
     */
    private static ArrayList<Integer> invertOrder(ArrayList<Integer> index){
        ArrayList<Integer> inverted = new ArrayList<Integer>(Collections.nCopies(index.size(), 0));
        for(int i = 0; i < index.size(); i++){
            inverted.set(index.get(i),i);
        }
        return inverted;
    }

    private static void doQuickSort(ArrayList<Long> array, ArrayList<Integer> index, int begin, int end) {
        if (end <= begin) {
            return;
        }
        int pivot = partition(array, index, begin, end);
        doQuickSort(array, index, begin, pivot-1);
        doQuickSort(array, index,pivot+1, end);
    }

    public static ArrayList<Integer> quickSort(ArrayList<Long> array){
        ArrayList<Integer> indexOf;
        indexOf = new ArrayList<Integer>(array.size());
        for(int i = 0; i<array.size();i++){ //create index array
            indexOf.add(i);
        }
        doQuickSort(array,indexOf,0,array.size()-1);

        return invertOrder(indexOf);
    }
}
