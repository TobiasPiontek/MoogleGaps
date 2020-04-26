package MoogleGaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

/**
 * Receives data from the Osmosis pipeline and prints ways which have the
 * 'highway key.
 *
 * @author pa5cal
 */
public class MyOsmReader implements Sink {

    public List<Long> nodes = new ArrayList<Long>();
    public List<Integer> wayIds = new ArrayList<Integer>();

    @Override
    public void initialize(Map<String, Object> arg0) {
    }

    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer) {
            // Nothing to do here
        } else if (entityContainer instanceof WayContainer) {
            Way myWay = ((WayContainer) entityContainer).getEntity();
            for (Tag myTag : myWay.getTags()) {
                if ("coastline".equalsIgnoreCase(myTag.getValue())) {
                    if (wayIds.isEmpty()) {
                        wayIds.add(0);
                    } else {
                        wayIds.add(myWay.getWayNodes().size() + wayIds.get(wayIds.size() - 1));
                    }
                    for (int i = 0; i < myWay.getWayNodes().size(); i++) {
                        nodes.add(myWay.getWayNodes().get(i).getNodeId());
                    }
                    break;
                }
            }
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
