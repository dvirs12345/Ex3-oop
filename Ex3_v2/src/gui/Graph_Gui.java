package gui;

import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.Fruit;
import gameClient.MyGameGUI;
import gameClient.Robot;
import org.json.JSONException;
import utils.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class contains functions for drawing graphs.  
 * @author Dvir Sadon.
 */
public class Graph_Gui
{
	static graph g1; // The graph.
	private double maxX , maxY, minX, minY; // Size of the graph.
	public ArrayList<ArrayList<Double>> f_E=new ArrayList<ArrayList<Double>>();
	
	public Graph_Gui() { ; }

	public void drawTimer(double timer)
	{
		StdDraw.text(this.minX+this.minX/100000, this.maxY+this.maxY/100000, Double.toString(timer));
	}

	/**
	 *
	 * @param fruits - List of Strings representing the fruits.
	 * @return Array of fruits.
	 * @throws JSONException
	 */
	public Fruit[] drawFruits(List<String> fruits) throws JSONException
	{
		Fruit[] fruities = new Fruit[fruits.size()];
		int counter = 0;

		for(String fru : fruits)
		{
			Fruit reut = new Fruit();
			reut.init(fru);
			fruities[counter] = reut;
			counter++;
		}

		drawFruits(fruities);
		return fruities;
	}

	/**
	 * Actually draws the fruits of the graph.
	 * @param fruities - Array of the fruits to draw.
	 */
	public void drawFruits(Fruit[] fruities)
	{
		String[] filename = {"apple.gif","","banana.gif"};
		for (Fruit rob : fruities)
			StdDraw.picture(rob.pos.x(),rob.pos.y(), filename[(rob.type-1)*-1]);
	}

	/**
	 * Clears the screen and draws the graph.
	 */
	public void repaint()
	{
		StdDraw.clear();
		displayGraph(g1);
	}

	/**
	 * Draws the robots.
	 * @param robots - List of String representing the robots.
	 * @return Array of the robots.
	 * @throws JSONException
	 */
	public Robot[] drawRobots(List<String> robots) throws JSONException
	{
		Robot[] robos = new Robot[robots.size()];
		int counter = 0;

		for(String robo : robots)
		{
			Robot reut = new Robot();
			reut.init(robo);
			robos[counter] = reut;
			counter++;
		}

		drawRobots(robos);

		return robos;
	}

	/**
	 * Actually draws the robots on the graph.
	 * @param robots - Array of robots to draw.
	 */
	public void drawRobots(Robot[] robots)
	{
		String filename = "robot1.gif";
		for (Robot rob : robots)
			StdDraw.picture(rob.pos.x(),rob.pos.y(), filename);
	}

	/**
	 * Makes the screen for choosing the scenario.
	 */
 	

	/**
	 * @param c - Collection of nodes.
	 * @return An array of doubles representing the max and min for x,y of all the points in the collection
	 * array values:
	 * (maxX, maxY, minX, minY)
	 */
	private double[] sizes(Collection<node_data> c)
	{
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		for(node_data nd : c)
		{
			if(nd.getLocation().y() > maxY)
				maxY = nd.getLocation().y();
			if(nd.getLocation().x() > maxX)
				maxX = nd.getLocation().x();
			if(nd.getLocation().y() < minY)
				minY = nd.getLocation().y();
			if(nd.getLocation().x() < minX)
				minX = nd.getLocation().x();
		}
		double[] arr = {maxX, maxY, minX, minY};
		return arr;
	}

	/**
	 * This function displays the given graph using std.
	 * @param g1 - The given graph
	 */
	public void displayGraph(graph g1) // Assuming that x and y are between (0,1).
	{
		double[] arr = sizes(g1.getV());

		this.maxX  = arr[0]+arr[0]/100000;
		this.minX = arr[2]-arr[2]/100000;
		this.maxY = arr[1]+arr[1]/100000;
		this.minY = arr[3]-arr[3]/100000;
		StdDraw.setYscale(this.minY, this.maxY);
		StdDraw.setXscale(this.minX, this.maxX);

		this.g1 = g1;
		Collection<node_data> vertecies = g1.getV();
		
		// The edges drawing part. 
		
		StdDraw.setPenRadius(0.003);
		StdDraw.setPenColor(StdDraw.BLACK);
		double srcX,srcY,destX,destY;
		
		for (node_data iterable_element : vertecies) // Iterate over all Nodes.
		{
			srcX = iterable_element.getLocation().x();
			srcY = iterable_element.getLocation().y();
			
			Collection<edge_data> edges = g1.getE(iterable_element.getKey());
			if(edges != null) 
			{
				for (edge_data iterable_element2 : edges) // Iterate over the hashMap of edges coming from this Node.
				{
					destX = g1.getNode(iterable_element2.getDest()).getLocation().x();
					destY = g1.getNode(iterable_element2.getDest()).getLocation().y();

					//StdDraw.text((srcX+destX)/2, (srcY+destY)/2, Double.toString(round(iterable_element2.getWeight(),2)));
					StdDraw.line(srcX, srcY, destX, destY);
				}
			}	
		}
		double size=StdDraw.getPenRadius();
		StdDraw.setPenRadius(size*1.5);
		for(ArrayList<Double> e:f_E ) {
			
			
			if(e.get(4)==1)StdDraw.setPenColor(Color.RED);
			else {StdDraw.setPenColor(Color.YELLOW);}

			StdDraw.line(e.get(0), e.get(1), e.get(2), e.get(3));
		}

		// The Nodes drawing part.
		
		StdDraw.setPenRadius(0.025);
		StdDraw.setPenColor(StdDraw.BLUE);
		double x, y;
		
		for (node_data iterable_element : vertecies) // Iterates over Nodes and makes their points on the graph.
		{
			if(iterable_element.getLocation() == null) 
				throw new RuntimeException("The Location of this node is null: "+iterable_element.getKey()); 
			
			x = iterable_element.getLocation().x();
			y = iterable_element.getLocation().y();
			
			StdDraw.text(x, y+0.05, Integer.toString(iterable_element.getKey()));
			StdDraw.point(x, y);
		}
	}


	private static double round(double value, int places)
	{
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}


	
	/**
	 * This function saves the graph as filename.
	 * @param filename - The name of the file.
	 */
	public static void save(String filename) 
	{
		try 
		{
			StdDraw.save(filename);
		}
		catch (Exception e) 
		{
			throw new RuntimeException("filename shouldn't be null! ");
		}
	}

	public void drowE(double x0, double y0, double x1, double y1, Double type) {
		ArrayList<Double> e=new ArrayList<Double>();
		e.add(x0);
		e.add(y0);
		e.add(x1);
		e.add(y1);
		e.add(type);
		f_E.add(e);
		
	}
}
