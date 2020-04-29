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
            }
        } else if (entityContainer instanceof WayContainer) {
            // Nothing to do here
        } else if (entityContainer instanceof RelationContainer) {
            // Nothing to do here
        } else {
            System.out.println("Unknown Entity!");
        }
    }

    @Override
    public void complete() {
    }

    @Override
    public void close() {
    }

}