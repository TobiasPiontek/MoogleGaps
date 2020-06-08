package MoogleGaps;


import java.util.ArrayList;
import java.util.Comparator;

public class NodeSortComparator implements Comparator<Integer>
{
    private final ArrayList<Long> array;
    public NodeSortComparator(ArrayList<Long> array)
    {
        this.array = array;
    }

    public Integer[] createIndexArray()
    {
        Integer[] indexes = new Integer[array.size()];
        for (int i = 0; i < array.size(); i++)
        {
            indexes[i] = i;
        }
        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2)
    {
        return array.get(index1).compareTo(array.get(index2));
    }
}