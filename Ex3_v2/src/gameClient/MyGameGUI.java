package gameClient;

import Server.Game_Server;
import Server.game_service;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * This class represents the gui part of the game. it also has functions for running the game.
 */
public class MyGameGUI
{

    public int scenario = 0; // The scenario the game is working on.
    private Robot[] robots; // Array of robots in the scenario.
    private Fruit[] fruits; // Array of fruits in the scenario.
    private graph gr; // The graph in the scenario.
    private game_service game; // The game object.
    private KML_Logger kmllogger;
    private Robot choose_robot=null;

    /* Constructors */

    public MyGameGUI() { ; }

    public MyGameGUI(int scenario, Robot[] robots, Fruit[] fruits)
    {
        this.scenario = scenario;
        this.robots = robots;
        this.fruits = fruits;
    }


    /**
     * Sets up the start of the game.
     * @param scenario - The scenario to init
     * @throws JSONException
     */
    private void init(int scenario) throws JSONException
    {
        StdDraw.enableDoubleBuffering();
        this.scenario = scenario;
        this.kmllogger = new KML_Logger(this.scenario);
        Graph_Gui ggui = new Graph_Gui();

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
        for(int a = 0;a<rs;a++)
            this.game.addRobot(src_node+a);

        /* Draw starting robots */
        List<String> robots = this.game.getRobots();
        this.robots = ggui.drawRobots(robots);


        /* Draw starting fruits */
        List<String> fruits = this.game.getFruits();
        System.out.println(fruits.get(0));
        this.fruits = ggui.drawFruits(fruits);

        /* Draw the timer */
        ggui.drawTimer(this.game.timeToEnd());

        StdDraw.show();
    }

    /**
     * Runs the automatic game. also, adds placemarks to the kml file accordingly.
     * @param scenario - The scenario of the game.
     * @throws JSONException
     */
    public void initiateGame(int scenario) throws JSONException, FileNotFoundException {
        /* Handle starting gui */
        Graph_Gui gg = new Graph_Gui();

        this.init(scenario);

        /* Run the game */
        this.game.startGame();
        int counter = 0;
        /* Run the robots automatically */
        while(this.game.isRunning())
        {
            //List<String> log = this.game.move();
            moveRobots();
            gg.repaint();
            robots=gg.drawRobots(this.game.getRobots());
            fruits=gg.drawFruits(this.game.getFruits());
            StdDraw.show();
            for (int i = 0; i < this.robots.length; i++)
            {
                if(counter%23 == 0)
                    this.kmllogger.addPlacemark("robot.png", this.robots[i].pos);
                this.game.chooseNextEdge(i, nextNode(this.robots[i].src));
            }
            for(Fruit f1:this.fruits)
            {
                if(f1.type == -1)
                    this.kmllogger.addPlacemark("banana.png", f1.pos);
                else
                    this.kmllogger.addPlacemark("apple.png", f1.pos);
            }
            counter++;
        }

        this.kmllogger.endkml();
    }

    /**
     * @param src - The source of the robot.
     * @return An integer representing the Next node to go to.
     */
    private int nextNode(int src)
    {


        int ans = -1;
        Collection<edge_data> ee = this.gr.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;
        while(i<r) {itr.next();i++;}
        ans = itr.next().getDest();
        return ans;
    }



    /**
     * Moves the robots on the graph to the dest.
     */
    private void moveRobots()
    {
        List<String> log = this.game.move();
        if(log!=null)
        {
            long t = this.game.timeToEnd();

            for(int i=0;i < log.size();i++)
            {
                String robot_json = log.get(i);
                try
                {
                    JSONObject line = new JSONObject(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    int rid = ttt.getInt("id");
                    int src = ttt.getInt("src");
                    int dest = ttt.getInt("dest");

                    if(dest==-1)
                    {
                        dest = this.nextNode(src);
                        System.out.println("robot: "+rid+" dest "+dest);
                        this.game.chooseNextEdge(rid, dest);
                        this.robots[rid].dest = -1;
                        this.robots[rid].src = dest;
                        System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
                        System.out.println(ttt);
                    }
                }
                catch (JSONException e) {e.printStackTrace();}
            }
        }
    }

    private int[] getStartnodes()
    {
        return new int[0];
    }

    /**
     * Runs the entire game.
     * @throws JSONException
     * @throws FileNotFoundException 
     */
    public void runGui() throws JSONException, FileNotFoundException
    {

    	Integer[] scenarios= new Integer[24];
    	for(int i=0;i<24;i++) {
    		scenarios[i]=i;
    	}

    	JOptionPane pane= new JOptionPane("Choose senario",JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                scenarios,
                scenarios[0]);
    	JDialog dialog= pane.createDialog(null);
    	dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	dialog.setVisible(true);
    	int scn=(int)pane.getValue();
    	

    	dialog.dispose();

    	System.out.println(scn);
    	String[] options= {"Auto","Manual"};
    	
    	pane= new JOptionPane(
    		    "How to run?",
    		   
    		    JOptionPane.PLAIN_MESSAGE,
    		    JOptionPane.YES_NO_OPTION,
    		    null,     //do not use a custom Icon
    		    options,  //the titles of buttons
    		    options[0]);
    	dialog=pane.createDialog(null);
    	dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	dialog.setVisible(true);
    	boolean manual=("Manual".equals(pane.getValue()));
	
//    	System.out.println(manual);
    	if(manual)initiateManualGame(scn);
    	else{initiateGame(scn);}
    	int score=calcScore();
    	

    	
    	pane= new JOptionPane(
    		    "Your score is: "+score,
    		    JOptionPane.INFORMATION_MESSAGE);
    	
    	dialog=pane.createDialog(null);
//    	dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	dialog.setVisible(true);
    	System.exit(0);
//    	boolean manual=("Manual".equals(pane.getValue()));
    	
//    	
    	
    	
    }
    private int calcScore() throws JSONException {
		int score=0;
    	for(String robot:game.getRobots()) {
    		System.out.println(robot);
			score+=new JSONObject(robot).getJSONObject("Robot").getInt("value");
		}
    	return score;
	}

	node_data getClosestNode(double x, double y) {

        int minDistanceToMatch = 5;

        node_data closestNode = null;

        double closestDistance = Double.POSITIVE_INFINITY;

        Point3D point = new Point3D(x, y, 0);

        for(node_data node : gr.getV()) {

                double nodeDistance = node.getLocation().distance2D(point);

                if((nodeDistance < minDistanceToMatch) &&

                                (nodeDistance < closestDistance)) {

                        closestNode = node;

                        closestDistance = nodeDistance;

                }

        }

        return closestNode;

    }

  
    public void initiateManualGame(int scenario) throws JSONException {
        /* Handle starting gui */
        Graph_Gui gg = new Graph_Gui();

        this.init(scenario);

        /* Run the game */
        this.game.startGame();

        /* Run the robots automatically */
        while(this.game.isRunning()){
        	game.move();
			  gg.repaint();
            this.robots=gg.drawRobots(this.game.getRobots());
            this.fruits=gg.drawFruits(this.game.getFruits());
            StdDraw.show();
        	if(StdDraw.isKeyPressed(127)&&choose_robot!=null) {
        		choose_robot=null;
        	}
        	
        	if(StdDraw.isMousePressed()) {
        		
        		node_data closest_node =getClosestNode(StdDraw.mouseX(),StdDraw.mouseY());
        		if(closest_node==null)continue;
        		if(choose_robot!=null) {
        			if(gr.getEdge(choose_robot.src, closest_node.getKey())!=null) {
        				choose_robot.dest=closest_node.getKey();
        				this.game.chooseNextEdge(choose_robot.id, choose_robot.dest);
        				choose_robot=null;
        				
        			}
        			continue;
        		}
        		
        		for(Robot r:robots) {
        			if(closest_node.getLocation().distance2D(r.pos)==0) {
        				choose_robot=r;
        				break;
        			}
        		}
        		 
        	}
        }
        
	}
}
