package MoogleGaps;

import java.util.ArrayList;
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
public class WayReader implements Sink {

    public List<Long> nodeIds = new ArrayList<Long>();
    public List<Integer> wayIds = new ArrayList<Integer>();
    public List<Double> allNodeLongitudes = new ArrayList<Double>();
    public List<Double> allNodeLatitudes = new ArrayList<Double>();
    public List<Long> allNodeIds = new ArrayList<Long>();
    Way myWay;
    Node myNode;

    @Override
    public void initialize(Map<String, Object> arg0) {
    }

    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer) {
            myNode = ((NodeContainer) entityContainer).getEntity();
            /*
            allNodeIds.add(myNode.getId());
            allNodeLongitudes.add(myNode.getLongitude());
            allNodeLatitudes.add(myNode.getLatitude());
            */

        } else if (entityContainer instanceof WayContainer) {
            myWay = ((WayContainer) entityContainer).getEntity();


            for (Tag myTag : myWay.getTags()) {
                if ("coastline".equalsIgnoreCase(myTag.getValue())) {
                    if (wayIds.isEmpty()) {
                        wayIds.add(0);
                    } else {
                        wayIds.add(myWay.getWayNodes().size() + wayIds.get(wayIds.size() - 1));
                    }
                    for (int i = 0; i < myWay.getWayNodes().size(); i++) {
                        nodeIds.add(myWay.getWayNodes().get(i).getNodeId());
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