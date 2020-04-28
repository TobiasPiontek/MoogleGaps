package MoogleGaps;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Receives data from the Osmosis pipeline and prints ways which have the
 * 'highway key.
 *
 * @author pa5cal
 */
public class NodeReader implements Sink {

    public List<Double> longitudes = new ArrayList<Double>();
    public List<Double> latitudes = new ArrayList<Double>();

    @Override
    public void initialize(Map<String, Object> arg0) {
    }

    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer) {
            Node myNode = ((NodeContainer) entityContainer).getEntity();
            //if (.contains(myNode.getId())) {
                longitudes.add(myNode.getLongitude());
                latitudes.add(myNode.getLatitude());
            //}
            System.out.println("I'm here!");
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