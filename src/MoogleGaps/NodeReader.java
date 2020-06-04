package MoogleGaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

/**
 * Receives data from the Osmosis pipeline and prints ways which have the
 * 'highway key.
 *
 * @author pa5cal
 */
public class NodeReader implements Sink {

    Node myNode;

    @Override
    public void initialize(Map<String, Object> arg0) {
    }

    // gets coordinates of WayNodes
    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer) {
            myNode = ((NodeContainer) entityContainer).getEntity();

            // add long- and latitudes to respective Lists
            int index = Collections.binarySearch(FileReader.nodeIds, myNode.getId());
            if (index > -1) {
                FileReader.longitudes[index] = myNode.getLongitude();
                FileReader.latitudes[index] = myNode.getLatitude();

                //search on right side of sorted list

                int i = 1;
                while (index + i < FileReader.nodeIds.size() && myNode.getId() == FileReader.nodeIds.get(index + i)) {
                    FileReader.longitudes[index + i] = myNode.getLongitude();
                    FileReader.latitudes[index + i] = myNode.getLatitude();
                    i++;
                }

                //search on the left side of sorted list
                int j = 1;
                while (index - j >= 0 && myNode.getId() == FileReader.nodeIds.get(index - j)) {
                    FileReader.longitudes[index - j] = myNode.getLongitude();
                    FileReader.latitudes[index - j] = myNode.getLatitude();
                    j++;
                }


            }
        } else if (entityContainer instanceof WayContainer) {
            // Nothing to do here
        } else if (entityContainer instanceof RelationContainer) {
            // Nothing to do here
        } else {
            // Nothing to do here
        }
    }

    @Override
    public void complete() {
    }

    @Override
    public void close() {
    }

}