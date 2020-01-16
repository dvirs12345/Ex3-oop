package gameClient;

import Server.game_service;
import Server.robot;
import Server.Fruit;
import Server.Game_Server;
import dataStructure.DGraph;
import dataStructure.graph;
import gui.Graph_Gui;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.List;

/**
 * @author Dvir Sadon and Moriya
 */
public class MyGameGUI
{
    public int scenario = 0;
    private Robot[] robots;
    private Fruit[] fruits;
    private graph gr;
    private game_service game;

    public MyGameGUI() { ; }

    public MyGameGUI(int scenario, Robot[] robots, Fruit[] fruits)
    {
        this.scenario = scenario;
        this.robots = robots;
        this.fruits = fruits;
    }

    public void init(int scenario) throws JSONException
    {
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
        ggui.drawRobots(robots);

        /* Draw starting fruits */
        List<String> fruits = this.game.getFruits();
        ggui.drawFruits(fruits);

        /* Draw the timer */
        ggui.drawTimer(this.game.timeToEnd());
    }

    public void initiateGame()
    {
        /* Handle the timer */
        double toend = this.game.timeToEnd();

        /* Handle starting gui */
        Graph_Gui gg = new Graph_Gui();
        gg.makeScenerioWindow();

        /* Run the game */
        this.game.startGame();
        while(this.game.isRunning())
        {
            this.game.move();
        }

    }
}
