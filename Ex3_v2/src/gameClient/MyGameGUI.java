package gameClient;

import Server.game_service;
import dataStructure.*;
import gameClient.Robot;
import gameClient.Fruit;
import Server.Game_Server;
import gui.Graph_Gui;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import org.json.JSONException;
import org.json.JSONObject;
import utils.StdDraw;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Dvir Sadon
 */
public class MyGameGUI
{

    public int scenario = 0; // The scenario the game is working on.
    private Robot[] robots; // Array of robots in the scenario.
    private Fruit[] fruits; // Array of fruits in the scenario.
    private graph gr; // The graph in the scenario.
    private game_service game; // The game object.

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
    public void init(int scenario) throws JSONException
    {
        StdDraw.enableDoubleBuffering();
        this.scenario = scenario;
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
     * Runs the automatic game.
     * @param scenario - The scenario of the game.
     * @throws JSONException
     */
    public void initiateGame(int scenario) throws JSONException
    {
        /* Handle starting gui */
        Graph_Gui gg = new Graph_Gui();

        this.init(scenario);

        /* Run the game */
        this.game.startGame();

        /* Run the robots automatically */
        while(this.game.isRunning())
        {
            moveRobots(this.game, this.gr);
            gg.repaint();
            gg.drawRobots(this.game.getRobots());
            gg.drawFruits(this.game.getFruits());
            StdDraw.show();

            for (int i = 0; i < this.robots.length; i++)
                this.game.chooseNextEdge(i, nextNode(this.gr, this.robots[i].src));
        }
    }

    /**
     *
     * @param g -The graph.
     * @param src - The source of the robot.
     * @return An integer representing the Next node to got to.
     */
    private static int nextNode(graph g, int src)
    {
        int ans = -1;
        Collection<edge_data> ee = g.getE(src);
        Iterator<edge_data> itr = ee.iterator();
        int s = ee.size();
        int r = (int)(Math.random()*s);
        int i=0;

        while(i<r)
        {
            itr.next();
            i++;
        }

        ans = itr.next().getDest();
        return ans;
    }

    /**
     * Moves the robots on the graph to the dest.
     * @param game - The game.
     * @param gg - The graph to move them on.
     */
    private static void moveRobots(game_service game, graph gg)
    {
        List<String> log = game.move();
        if(log!=null)
        {
            long t = game.timeToEnd();

            for(int i=0;i<log.size();i++)
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
                        dest = nextNode(gg, src);
                        game.chooseNextEdge(rid, dest);
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

    public void runGui() throws JSONException
    {
        Graph_Gui gg = new Graph_Gui();
        gg.makeScenerioWindow();
    }
}
