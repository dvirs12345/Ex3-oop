package gameClient;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;
import dataStructure.edge_data;

import java.util.Collection;

/**
 * This class represents a Fruit.
 * it has a value, type, pos (point3D), edge (edge_data).
 */
public class Fruit
{
    public double value; // The score given when eaten.
    public int type; // Banana or apple.
    public Point3D pos; // Cord of the fruit.
    public edge_data edge; // The edge the fruit is on.
    public int tag; // The id of the robot going to get it.

    public Fruit() { ; }

    /**
     * This is an init function for a fruit. it gets a string representing a fruit and updates the attributes accordingly
     * @param fruitstr - A string representing a fruit.
     * @throws JSONException
     */
    public void init(String fruitstr) throws JSONException
    {
        JSONObject line = new JSONObject(fruitstr);
        JSONObject ttt = line.getJSONObject("Fruit");

        this.value = (double) ttt.get("value");
        this.type = (int) ttt.get("type");
        String[] temp = ((String) ttt.get("pos")).split(",");
        this.pos = new Point3D(Double.parseDouble(temp[0]) , Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
        this.tag = -1;
    }

//    public edge_data getOnEdge(Point3D posf, game_service game, int type)
//    {
//        String g = game.getGraph();
//        DGraph gg = new DGraph();
//        gg.init(g);
//        Collection<node_data> nodes = gg.getV();
//
//        for (node_data n : nodes) // Iterates over all Nodes in the graph
//        {
//            Collection<edge_data> edges = gg.getE(n.getKey());
//
//            for (edge_data e : edges) // Iterates over all edges coming out of the src
//            {
//
//                Point3D source =  gg.getNode(e.getSrc()).getLocation();
//                Point3D dest =  gg.getNode(e.getDest()).getLocation();
//
//                double dist_src_dest = Math.sqrt(Math.pow(source.x()-dest.x(), 2)+Math.pow(source.y()-dest.y(), 2));
//                double dist_src_p = Math.sqrt(Math.pow(source.x()-posf.x(), 2)+Math.pow(source.y()-posf.y(), 2));
//                double dist_dest_p = Math.sqrt(Math.pow(dest.x()-posf.x(), 2)+Math.pow(dest.y()-posf.y(), 2));
//                double total_dist = dist_src_p + dist_dest_p;
//
//                if (Math.abs(total_dist-dist_src_dest) <= 0.0000001)
//                {
//                    if (type==-1) // banana
//                        return new Edge(e.getDest(), e.getSrc(),0);
//                    else // apple
//                        return e;
//                }
//            }
//        }
//        return null;
//    }

}
