package algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import dataStructure.DGraph;

import dataStructure.edge_data;
import dataStructure.graph;

import dataStructure.node_data;

import utils.Point3D;

/**
 * This class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 *
 * @author
 */
public class Graph_Algo implements graph_algorithms {

    graph g;

    public Graph_Algo(graph g) {
        this.g = g;
    }

    public Graph_Algo() {
        this.g = new DGraph();
    }

    @Override
    public void init(graph g) {
        this.g = g;

    }

    @Override
    public void init(String file_name) {

        try {
            FileInputStream fi = new FileInputStream(new File(file_name));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            graph dGraph = (DGraph) oi.readObject();
            init(dGraph);
            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            e.printStackTrace();
        }
    }

    @Override
    public void save(String file_name) {
        try {
            FileOutputStream f = new FileOutputStream(new File(file_name));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(g);

            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file_name);
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }

    }

    @Override
    public boolean isConnected() {
//		Collection<node_data> v=g.getV();
//		if(v.isEmpty())return true;//connected in the empty sense
//		
//		if(g.edgeSize()<v.size())return false;
//	
//		node_data v1=v.iterator().next();//get starting v
//		tag(g,v1,1);
//		
        boolean flag = true;
//		//check if all vertices are tagged
//		for(node_data v2: v) {
//			if(v2.getTag()==1)continue;
//			flag=false;
//			break;
//		}
//		if(!flag) {
//			v.forEach(m->{m.setTag(0);});//remove tags
//			return false;
//		}
//		DGraph g1=new DGraph();
//		
//		g1.reverseDGraph(g);
//		tag(g1,v1,0);
//		
//		for(node_data v2: v) {
//			if(v2.getTag()==0)continue;
//			flag=false;
//			v2.setTag(0);//remove tag
//			
//		}
        return flag;
    }

    private void tag(graph graph, node_data v, int tag) {
        //tags all vertexes in graph that are reachable from v
        Stack<Iterator<edge_data>> stack = new Stack<Iterator<edge_data>>();
        node_data v1;
        Iterator<edge_data> itr;
        stack.push(graph.getE(v.getKey()).iterator());
        edge_data e;
        do {
            itr = stack.pop();
            while (itr.hasNext()) {
                e = itr.next();
                v1 = graph.getNode(e.getDest());
                if (v1.getTag() == tag) continue;
                v1.setTag(tag);
                stack.push(itr);
                itr = graph.getE(v1.getKey()).iterator();
            }
        } while (!stack.isEmpty());
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        g.getV().forEach(v -> {
            v.setWeight(Double.POSITIVE_INFINITY);
        });
        tagWeight(src, dest);
        g.getNode(dest).setTag(0);
        return g.getNode(dest).getWeight();
    }


    class path implements Comparator<path> {

        node_data node;
        path next;
        double dist;

        public path(double d, node_data node) {
            node.setWeight(d);
            this.node = node;
            dist = d;

        }

        public path addDist(node_data node, double d) {
            path p = new path(node, d);
            p.next = this;
            return p;
        }

        public path(node_data node, double d) {
            node.setWeight(d);
            this.node = node;
            dist = d;

        }

        public path add(node_data node, double d) {
            path p = new path(node, d);
            p.next = this;

            return p;
        }


        public path() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public int compare(path p1, path p2) {
            if (p1.node.getWeight() > p2.node.getWeight()) return 1;
            if (p1.node.getWeight() < p2.node.getWeight()) return -1;
            if (p1.node.getTag() == 2) return -1;

            return 1;
        }

        /**
         * @param size return the shortest path containing #size-1 nodes with tag 2 not including the first node
         */
        public void trim(int size) {
            HashMap<Integer, node_data> treil = new HashMap<Integer, node_data>(size);
            path tail = this;
            treil.put(tail.node.getKey(), tail.node);
            double dist = 0;
            while (treil.size() < size) {

                dist += g.getEdge(tail.next.node.getKey(), tail.node.getKey()).getWeight();
                tail = tail.next;
                if (tail.node.getTag() == 2) treil.put(tail.node.getKey(), tail.node);

            }

            this.dist = dist;
            if (tail.next == null) return;

            tail.next = null;
        }


    }

    private path tagWeight(int src, int dest) {
// tag distance and finds shortest path 
//		System.out.println(g.nodeSize());
        PriorityQueue<path> h = new PriorityQueue<path>(g.nodeSize(), new path());


        node_data vSrc = g.getNode(src);
        path pSrc = new path(vSrc, 0);
        node_data v = g.getNode(dest);
        v.setTag(2);

        double d, ans = v.getWeight();


        h.add(pSrc);

        while (!h.isEmpty() && h.peek().node.getTag() != 2) {

            pSrc = h.remove();


            for (edge_data e : g.getE(pSrc.node.getKey())) {

                d = e.getWeight() + pSrc.node.getWeight();
                v = g.getNode(e.getDest());
                if (v.getWeight() <= d || ans <= d) continue;


                path p = pSrc.add(v, d);
                h.removeIf(m -> (m.node == p.node));

                h.add(p);
                if (v.getKey() == dest) ans = d;


            }
        }

        return h.peek();//shortest path
    }


    @Override
    public List<node_data> shortestPath(int src, int dest) {
        g.getV().forEach(v -> {
            v.setWeight(Double.POSITIVE_INFINITY);
        });


        path p = tagWeight(src, dest);
        g.getNode(dest).setTag(0);


        if (p == null) return null;
        List<node_data> trace = treil(p);

        Collections.reverse(trace);

        return trace;
    }

    private List<node_data> treil(path p) {
        //convert path to List
        ArrayList<node_data> trace = new ArrayList<node_data>();
        while (p != null) {
            trace.add(p.node);
            p = p.next;
        }
        return trace;
    }


    @Override
    public List<node_data> TSP(List<Integer> targets) {
        if (targets == null || targets.size() < 2) return null;
        g.getV().forEach(v -> {
            v.setTag(0);
        });
        Iterator<Integer> itr = targets.iterator();

        node_data vSrc = g.getNode(itr.next());
        int tag = 4;
        int count = tag(vSrc, targets, tag);
        HashMap<Integer, ArrayList<node_data>> connected = new HashMap<Integer, ArrayList<node_data>>();

        if (vSrc.getTag() != tag) {
            count++;
        }
        connected.put(count, new ArrayList<node_data>());
        connected.get(count).add(vSrc);

        while (itr.hasNext()) {//sort by connectivity
            tag++;
            vSrc = g.getNode(itr.next());
            count = tag(vSrc, targets, tag);
            if (vSrc.getTag() != tag) count++;
            vSrc.setWeight(count);
            connected.putIfAbsent(count, new ArrayList<node_data>());
            connected.get(count).add(vSrc);
        }
        g.getV().forEach(v -> {
            v.setTag(0);
            v.setWeight(Double.POSITIVE_INFINITY);
        });

        if (connected.size() == 1 && connected.keySet().contains(targets.size())) {
//			System.out.println("Strongly connected");
            List<node_data> trg = connected.get(targets.size());

            path p = null, ans = null;
            double dist = Double.POSITIVE_INFINITY;

            trg.forEach(v -> {
                v.setTag(2);
            });

            for (node_data start : trg) {

                p = new path(0, start);

                p = tspConnected(p, count - 1);

                trg.forEach(v -> {
                    v.setTag(2);
                });

                p.trim(targets.size());

                if (dist < p.dist) {
                    continue;
                }
                dist = p.dist;
                ans = p;
//			System.out.println(ans.dist+" dist");
            }
            trg = treil(ans);

            Collections.reverse(trg);

            return trg;
        }
        ArrayList<ArrayList<node_data>> deg_order = new ArrayList<ArrayList<node_data>>(connected.size());
        int i = targets.size();
        while (i > 0) {//Verifying connectivity
            if (!connected.containsKey(i)) return null;
            deg_order.add(connected.get(i));
            i -= connected.get(i).size();
        }


        path p = null;
        List<node_data> deg = deg_order.get(0);
        HashMap<Integer, path> paths = new HashMap<Integer, path>();
        deg.forEach(v -> {
            v.setTag(2);
        });

        for (node_data start : deg) {

            p = new path(0, start);
            if (deg.size() == 1) {
                paths.put(p.node.getKey(), p);
                continue;
            }
            p = tspConnected(p, deg.size() - 1);
            deg.forEach(v -> {
                v.setTag(2);
            });

            if (p == null) continue;
            p.trim(deg.size());

            if (paths.containsKey(p.node.getKey()) && paths.get(p.node.getKey()).dist <= p.dist) continue;

            paths.put(p.node.getKey(), p);
        }

        HashMap<Integer, path> paths2;
        for (i = 1; i < deg_order.size(); i++) {
            deg.forEach(v -> {
                v.setTag(0);
            });
            paths2 = new HashMap<Integer, path>(paths.size());
            deg = deg_order.get(i);

            for (path path : paths.values()) {

                deg.forEach(v -> {
                    v.setTag(2);
                });
                p = tspConnected(path, deg.size());
                if (p == null || (paths2.containsKey(p.node.getKey()) && paths2.get(p.node.getKey()).dist <= p.dist))
                    continue;
                paths2.put(p.node.getKey(), p);
            }
            paths = paths2;

        }
        double dist = Double.POSITIVE_INFINITY;
        for (path path : paths.values()) {
            if (dist > path.dist) {
                dist = path.dist;
                p = path;
            }
        }

        deg = treil(p);
        Collections.reverse(deg);
        return deg;


    }

    private path tspConnected(path pSrc, int size) {

        PriorityQueue<path> h = new PriorityQueue<path>(g.nodeSize(), new path());

        HashMap<Integer, node_data> taged = new HashMap<Integer, node_data>();

        node_data v;
        double d, ans;

        while (size > 0) {

            pSrc.node.setTag(0);

            pSrc.node.setWeight(0);
            h.add(pSrc);

            taged.put(pSrc.node.getKey(), pSrc.node);
            ans = Double.POSITIVE_INFINITY;

            while (!h.isEmpty() && h.peek().node.getTag() != 2) {
                pSrc = h.remove();


                for (edge_data e : g.getE(pSrc.node.getKey())) {

                    d = e.getWeight() + pSrc.node.getWeight();
                    v = g.getNode(e.getDest());

                    if (v.getWeight() <= d || ans <= d) continue;
                    taged.put(v.getKey(), v);

                    path p1 = pSrc.addDist(v, d);
//					h.removeIf(m->(m.node==p1.node));
                    if (v.getTag() == 2) {
                        h.removeIf(m -> (m.node.getTag() == 2));

                    }
                    h.add(p1);

                }
            }

            taged.values().forEach(t -> {
                t.setWeight(Double.POSITIVE_INFINITY);
            });

            size--;
            if (h.isEmpty() || size == 0) break;
            taged.clear();
            pSrc = h.peek();

            h.clear();
        }

        return h.peek();
    }


    private int tag(node_data vSrc, List<Integer> targets, int tag) {
        tag(g, vSrc, tag);
        int count = 0;
        for (int i : targets) {
            if (g.getNode(i).getTag() == tag) count++;
        }
        return count;
    }


    /**
     * @param src
     * @param e
     * @param distp
     * @return object{(double) distance, (int) src, (edge_data) e, (int) next key in path }
     */
    public ArrayList<Object> shortestPathDir(int src, edge_data e, double distp) {

        ArrayList<Object> toEdge = new ArrayList<Object>();



        if (src == e.getSrc()) {
            toEdge.add(e.getWeight() - distp-Double.parseDouble(e.getInfo()));
            toEdge.add(src);
            toEdge.add(e);
            toEdge.add(e.getDest());
            return toEdge;
        }
        g.getV().forEach(v -> {
            v.setWeight(Double.POSITIVE_INFINITY);
        });
        path p = tagWeight(src, e.getSrc());
        g.getNode(e.getSrc()).setTag(0);

        toEdge.add(g.getNode(e.getSrc()).getWeight() + e.getWeight() - distp-Double.parseDouble(e.getInfo()));
        toEdge.add(src);
        toEdge.add(e);
        List<node_data> trace = treil(p);
        toEdge.add(trace.get(trace.size() - 2).getKey());
        return toEdge;

    }


    @Override
    public graph copy() {
        DGraph graph = new DGraph();
//		graph.copy(this.g);

        return graph;
    }

}
