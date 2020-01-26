package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gui.Graph_Gui;
import org.json.JSONException;
import org.json.JSONObject;


import utils.Point3D;
import utils.StdDraw;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * This class represents the gui part of the game. it also has functions for running the game.
 */
public class MyGameGUI {

    public int scenario = 0; // The scenario the game is working on.
    private Robot[] robots; // Array of robots in the scenario.
    private Fruit[] fruits; // Array of fruits in the scenario.
    private graph gr; // The graph in the scenario.
    private game_service game; // The game object.
    private KML_Logger kmllogger;
    private Robot choose_robot = null;
	private int time;
	private double dif=0;
	boolean move=true;
	public double difMax=110;
	

    /* Constructors */

    public MyGameGUI() {
        ;
    }

    public MyGameGUI(int scenario, Robot[] robots, Fruit[] fruits) {
        this.scenario = scenario;
        this.robots = robots;
        this.fruits = fruits;
    }


    /**
     * Sets up the start of the game.
     *
     * @param scenario - The scenario to init
     * @throws JSONException
     */
    private void init(int scenario) throws JSONException {
        StdDraw.enableDoubleBuffering();
        this.scenario = scenario;
        this.kmllogger = new KML_Logger(this.scenario);
        Graph_Gui ggui = new Graph_Gui();

        int id = 000000000;
        Game_Server.login(id);
        this.game = Game_Server.getServer(this.scenario);
        String g = this.game.getGraph();
        String info = this.game.toString();
        DGraph gg = new DGraph();
        gg.init(g);
        this.gr = gg;

        ggui.displayGraph(gg);

        JSONObject line = new JSONObject(info);
        JSONObject ttt = line.getJSONObject("GameServer");
        int rs = ttt.getInt("robots");

        int src_node = 0;
        placeRobots(ggui, rs);
        //for (int a = 0; a < rs; a++)
        //  this.game.addRobot(src_node + a);

        /* Draw starting robots */
        List<String> robots = this.game.getRobots();
        this.robots = ggui.drawRobots(robots);


        /* Draw starting fruits */
        List<String> fruits = this.game.getFruits();
//        System.out.println(fruits.get(0));
        this.fruits = ggui.drawFruits(fruits);

        /* Draw the timer */
        ggui.drawTimer(this.game.timeToEnd());

        StdDraw.show();
    }

    /**
     * Runs the automatic game. also, adds placemarks to the kml file accordingly.
     *
     * @param scenario - The scenario of the game to run.
     * @throws JSONException
     */
    public void initiateGame(int scenario) throws JSONException, FileNotFoundException {
        /* Handle starting gui */
        Graph_Gui gg = new Graph_Gui();
      
        this.init(scenario);

        /* Run the game */
        this.game.startGame();
        time=(int) game.timeToEnd();
//        System.out.println(time);
        int counter = 0;
        /* Run the robots automatically */

        while (this.game.isRunning()) {
            //List<String> log = this.game.move();
            nextMoves(gg);
            
//			if(m==0) {m=80;this.game.move();}
//			m--;
            gg.repaint();
            robots = gg.drawRobots(this.game.getRobots());
            fruits = gg.drawFruits(this.game.getFruits());
            StdDraw.show();
            for (int i = 0; i < this.robots.length; i++) {
                if (counter % 23 == 0)
                    this.kmllogger.addPlacemark("robot.png", this.robots[i].pos);

            }
            for (Fruit f1 : this.fruits) {
                if (f1.type == -1)
                    this.kmllogger.addPlacemark("banana.png", f1.pos);
                else
                    this.kmllogger.addPlacemark("apple.png", f1.pos);
            }
            counter++;
        }

        this.kmllogger.endkml();
        String res = game.toString();
		String remark = this.kmllogger.getLogger().toString();
		game.sendKML(remark); // Should be your KML (will not work on case -1).
		System.out.println(res);
    }

    /**
     * Places robots on the
     *
     * @param gg
     * @param rs
     * @throws JSONException
     */
    private void placeRobots(Graph_Gui gg, int rs) throws JSONException {
        List<edge_data> dests = findFruits(gg);

        for (int i = 0; rs > i; i++) {

            if (dests.size() != 0) {
                this.game.addRobot(dests.get(dests.size() - 1).getSrc());
                dests.remove(dests.size() - 1);
            }
        }
        return;
    }

    /**
     * @param gg - The graph to get the fruits on.
     * @return list of edges matching the fruits in game
     * @throws JSONException
     */
    private List<edge_data> findFruits(Graph_Gui gg) throws JSONException {
        ArrayList<edge_data> dests = new ArrayList<edge_data>();
        
        double x0, y0, y1, x1;
        gg.f_E = new ArrayList<ArrayList<Double>>();

        JSONObject line;
        for (node_data v : gr.getV()) {
            for (edge_data e : gr.getE(v.getKey())) {
                e.setTag(0);
                e.setInfo("");
            }
        }
        Point3D pos,epos;
        double dist;
        edge_data e;
        for (String fr : game.getFruits()) {

            line = new JSONObject(fr).getJSONObject("Fruit");
            pos = new Point3D(line.getString("pos"));
            e = getEdge(pos, line.getInt("type"));
            epos=gr.getNode(e.getDest()).getLocation();
            dist=pos.distance3D(epos)/epos.distance3D(gr.getNode(e.getSrc()).getLocation())*e.getWeight();
            if(e.getInfo()==""||dist>Double.parseDouble(e.getInfo()));
            	e.setInfo(""+dist);


            x0 = this.gr.getNode(e.getSrc()).getLocation().x();
            x1 = this.gr.getNode(e.getDest()).getLocation().x();
            y0 = this.gr.getNode(e.getSrc()).getLocation().y();
            y1 = this.gr.getNode(e.getDest()).getLocation().y();
            gg.drowE(x0, y0, x1, y1, (double) line.getInt("type"));

            e.setTag(e.getTag() + line.getInt("value"));
            dests.add(e);
        }
        Collections.sort(dests, new Comparator<edge_data>() {

            @Override
            public int compare(edge_data o1, edge_data o2) {
                return -Double.compare(o1.getWeight() / o1.getTag(), o2.getWeight() / o1.getTag());
            }
        });
        return dests;
    }

    /**
     * @param gg move for all robots.
     * @throws JSONException
     */

    /**
     * @param gg move for all robots
     * @throws JSONException
     */
    private void nextMoves(Graph_Gui gg) throws JSONException {

        JSONObject line;

        ArrayList<ArrayList<Object>> robotToFruit = new ArrayList<ArrayList<Object>>();


        List<edge_data> dests = findFruits(gg);
        Graph_Algo algo = new Graph_Algo();


        algo.init(this.gr);
        int dest = 0;

        List<String> s = this.game.getRobots();
        double diste, distp = 0;
        for (String robot : s) {
            line = new JSONObject(robot).getJSONObject("Robot");
            Point3D p;
            edge_data ed = null;
            int src, Id = line.getInt("id");
            int speed = line.getInt("speed");

            for (edge_data e : gr.getE(line.getInt("src"))) {
                p = new Point3D(line.getString("pos"));
                diste = gr.getNode(e.getSrc()).getLocation().distance2D(gr.getNode(e.getDest()).getLocation());
                distp = (diste - (gr.getNode(e.getSrc()).getLocation().distance2D(p) + p.distance2D(gr.getNode(e.getDest()).getLocation())));
                if (distp < -0.00000001) continue;
                distp = gr.getNode(e.getSrc()).getLocation().distance2D(p);
                distp /= diste;
                distp = e.getWeight() * (distp);
                dest = e.getDest();
                ed = e;
                break;
            }

            ArrayList<Object> dir;

            for (edge_data fr : dests) {

                src = line.getInt("src");

                dir = algo.shortestPathDir(src, fr, distp);
                if((double)dir.get(0)*100>game.timeToEnd())continue;
                dir.add(Id);//4
                


                dir.add(speed);//5
                dir.add(ed);//6
                dir.add(distp);//7
                robotToFruit.add(dir);

            }
            Collections.sort(robotToFruit, new Comparator<ArrayList<Object>>() {

                @Override
                public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {

                    return Double.compare(((Double) o1.get(0)) / (int) o1.get(5), ((Double) o2.get(0)) / ((int) o2.get(5)));

                }
            });

        }
            while (robotToFruit.size() > 0) {
//        
                ArrayList<Object> byrobot = (robotToFruit.get(0));
                robotToFruit.removeIf(a -> (a.get(4) == byrobot.get(4) || a.get(2) == byrobot.get(2) || a.get(2) == byrobot.get(6)));

      
                this.game.chooseNextEdge((int) byrobot.get(4), (int) byrobot.get(3));

                if((byrobot.get(2)==byrobot.get(6)))dif=Math.min(dif,(int)((double)byrobot.get(0)*1000)
                		/((int)byrobot.get(5))
                		);

            }

            
            if(time-game.timeToEnd()>=dif) { 
            	;this.game.move();time=(int)game.timeToEnd();
            
            }
            dif=difMax;
            	

        }


    


    /**
     * @param p    - The point to find the edge for.
     * @param type - The type of the fruit.
     * @return
     */
    private edge_data getEdge(Point3D p, int type) {
        double dist;
        edge_data ans = null;
        for (node_data v : gr.getV()) {
            for (edge_data e : gr.getE(v.getKey())) {
                dist = (gr.getNode(e.getSrc()).getLocation().distance2D(gr.getNode(e.getDest()).getLocation())) - (gr.getNode(e.getSrc()).getLocation().distance2D(p) + p.distance2D(gr.getNode(e.getDest()).getLocation()));

                if ((type == 1 && e.getDest() < e.getSrc()) || type != 1 && e.getDest() > e.getSrc()) continue;

                if (dist >= -0.00000001) return e;
            }
        }
        System.out.println("found no edge!!!!");

        return ans;

    }

    /**
     * Runs the entire game.
     * @throws JSONException
     * @throws FileNotFoundException
     */
    public void runGui() throws JSONException, FileNotFoundException {

        Integer[] scenarios = new Integer[24];
        for (int i = 0; i < 24; i++) {
            scenarios[i] = i;
        }

        JOptionPane pane = new JOptionPane("Choose senario", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                scenarios,
                scenarios[0]);
        JDialog dialog = pane.createDialog(null);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        int scn = (int) pane.getValue();


        dialog.dispose();

//        System.out.println(scn);
        String[] options = {"Auto", "Manual"};

        pane = new JOptionPane(
                "How to run?",

                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]);
        dialog = pane.createDialog(null);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        boolean manual = ("Manual".equals(pane.getValue()));

//    	System.out.println(manual);
        if (manual) initiateManualGame(scn);
        else {
            initiateGame(scn);
        }
        int score = calcScore();


        String[] options2 = {"play again"};
        pane = new JOptionPane(
                "Your score is: " + score,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.YES_OPTION,
                null,     //do not use a custom Icon
                options2  //the titles of buttons
        );

        dialog = pane.createDialog(null);
//    	dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
//        System.out.println(pane.getValue());
        System.out.println(game.toString());
        if (pane.getValue() == null) System.exit(0);
        runGui();
//    	boolean manual=("Manual".equals(pane.getValue()));

//


    }

    private int calcScore() throws JSONException {
        int score = 0;
        for (String robot : game.getRobots()) {
            System.out.println(robot);
            score += new JSONObject(robot).getJSONObject("Robot").getInt("value");
        }
        return score;
    }

    node_data getClosestNode(double x, double y) {

        int minDistanceToMatch = 5;

        node_data closestNode = null;

        double closestDistance = Double.POSITIVE_INFINITY;

        Point3D point = new Point3D(x, y, 0);

        for (node_data node : gr.getV()) {

            double nodeDistance = node.getLocation().distance2D(point);

            if ((nodeDistance < minDistanceToMatch) &&

                    (nodeDistance < closestDistance)) {

                closestNode = node;

                closestDistance = nodeDistance;

            }

        }

        return closestNode;

    }


    /**
     * Initiates the manual game.
     *
     * @param scenario - The scenario chosen.
     * @throws JSONException
     */
    public void initiateManualGame(int scenario) throws JSONException {
        /* Handle starting gui */
        Graph_Gui gg = new Graph_Gui();

        this.init(scenario);

        /* Run the game */
        this.game.startGame();

        /* Run the robots automatically */
        while (this.game.isRunning()) {
            game.move();
            gg.repaint();
            this.robots = gg.drawRobots(this.game.getRobots());
            this.fruits = gg.drawFruits(this.game.getFruits());
            StdDraw.show();
            if (StdDraw.isKeyPressed(127) && choose_robot != null) {
                choose_robot = null;
            }

            if (StdDraw.isMousePressed()) {

                node_data closest_node = getClosestNode(StdDraw.mouseX(), StdDraw.mouseY());
                if (closest_node == null) continue;
                if (choose_robot != null) {
                    if (gr.getEdge(choose_robot.src, closest_node.getKey()) != null) {
                        choose_robot.dest = closest_node.getKey();
                        this.game.chooseNextEdge(choose_robot.id, choose_robot.dest);
                        choose_robot = null;

                    }
                    continue;
                }

                for (Robot r : robots) {
                    if (closest_node.getLocation().distance2D(r.pos) == 0) {
                        choose_robot = r;
                        break;
                    }
                }
            }
        }
    }

   
}
