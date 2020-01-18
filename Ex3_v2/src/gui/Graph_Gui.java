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
 	public void makeScenerioWindow()
	{
		JFrame frame= new JFrame();
		frame.setTitle("Enter wanted scenario");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel headingPanel = new JPanel();

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constr = new GridBagConstraints();
		constr.insets = new Insets(5, 5, 5, 5);
		constr.anchor = GridBagConstraints.WEST;

		constr.gridx=0;
		constr.gridy=0;

		// Declare the required Labels
		JLabel userNameLabel = new JLabel("Enter scenario number (0-23) :");

		// Declare Text fields
		JTextField userNameTxt = new JTextField(20);

		panel.add(userNameLabel, constr);
		constr.gridx=1;
		panel.add(userNameTxt, constr);
		constr.gridx=0; constr.gridy = 1;

		constr.gridwidth = 2;
		constr.anchor = GridBagConstraints.CENTER;

		JButton button = new JButton("Enter");
		// add a listener to button
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MyGameGUI mgg = new MyGameGUI();
				try
				{
					int scenario = Integer.parseInt(userNameTxt.getText());
					frame.setVisible(false);
					mgg.initiateGame(scenario);
				}
				catch (JSONException ex)
				{
					ex.printStackTrace();
				}
			}
		});

		// Add label and button to panel
		panel.add(button, constr);

		mainPanel.add(headingPanel);
		mainPanel.add(panel);

		// Add panel to frame
		frame.add(mainPanel);
		frame.pack();
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

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
	 * Displays the shortest path on the graph.
	 */
	public static void display_shortestPath() 
	{
		JFrame frame= new JFrame(); 
        frame.setTitle("Enter wanted variables");
         
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
 
        JPanel headingPanel = new JPanel();
         
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 5, 5, 5);     
        constr.anchor = GridBagConstraints.WEST;
 
        constr.gridx=0;
        constr.gridy=0;
  
        // Declare the required Labels
        JLabel userNameLabel = new JLabel("Source :");
        JLabel pwdLabel = new JLabel("Destenation :");
         
        // Declare Text fields
        JTextField userNameTxt = new JTextField(20);
        JTextField pwdTxt = new JTextField(20);
         
        panel.add(userNameLabel, constr);
        constr.gridx=1;
        panel.add(userNameTxt, constr);
        constr.gridx=0; constr.gridy = 1;
         
        panel.add(pwdLabel, constr);
        constr.gridx=1;
        panel.add(pwdTxt, constr);
        constr.gridx=0; constr.gridy = 2;
         
        constr.gridwidth = 2;
        constr.anchor = GridBagConstraints.CENTER;
  
        JButton button = new JButton("Enter");
        // add a listener to button
        button.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		change_graph1(Integer.parseInt(userNameTxt.getText()), Integer.parseInt(pwdTxt.getText()));
        	}
        });
  
       // Add label and button to panel
       panel.add(button, constr);
  
       mainPanel.add(headingPanel);
       mainPanel.add(panel);
 
        // Add panel to frame
       frame.add(mainPanel);
       frame.pack();
       frame.setSize(400, 400);
       frame.setLocationRelativeTo(null);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setVisible(true);
	}
	
	/**
	 * Displays the TSP result on the graph
	 */
	public static void display_TSP() 
	{
		JFrame frame= new JFrame(); 
        frame.setTitle("Enter wanted variables");
         
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
 
        JPanel headingPanel = new JPanel();
         
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 5, 5, 5);     
        constr.anchor = GridBagConstraints.WEST;
 
        constr.gridx=0;
        constr.gridy=0;
  
        // Declare the required Labels
        JLabel userNameLabel = new JLabel("Enter integers seperate by , :");
         
        // Declare Text fields
        JTextField userNameTxt = new JTextField(20);
         
        panel.add(userNameLabel, constr);
        constr.gridx=1;
        panel.add(userNameTxt, constr);
        constr.gridx=0; constr.gridy = 1;
         
        constr.gridwidth = 2;
        constr.anchor = GridBagConstraints.CENTER;
  
        JButton button = new JButton("Enter");
        // add a listener to button
        button.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		String[] arr1 = userNameTxt.getText().split(",");
        		List<Integer> temp = new LinkedList<Integer>();
        		
        		for (int i = 0; i < arr1.length; i++)
					temp.add(Integer.parseInt(arr1[i]));
        		
        		change_graph2(temp);
        	}
        });
  
       // Add label and button to panel
       panel.add(button, constr);
  
       mainPanel.add(headingPanel);
       mainPanel.add(panel);
 
        // Add panel to frame
       frame.add(mainPanel);
       frame.pack();
       frame.setSize(400, 400);
       frame.setLocationRelativeTo(null);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setVisible(true);
	}
	
	/**
	 * This function draws the path from the given src to the dest given by shortest_path on the graph.
	 * @param src - The source node id
	 * @param dest - The destination node id
	 */
	public static void change_graph1(int src, int dest) 
	{
		Graph_Algo ga = new Graph_Algo();
		ga.init(g1);
		List<node_data> path = ga.shortestPath(src, dest);
		
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.YELLOW);
		
		double srcX,srcY,destX,destY;
		
		for (int i = 0; i < path.size()-1;i++) // Iterates over the path. 
		{
			node_data iterable_element = path.get(i);
			srcX = iterable_element.getLocation().x();
			srcY = iterable_element.getLocation().y();		
			destX = path.get(i+1).getLocation().x();
			destY = path.get(i+1).getLocation().y();
			
			StdDraw.line(srcX, srcY, destX, destY);
		}
		
		StdDraw.setPenRadius(0.025);
		StdDraw.setPenColor(StdDraw.BLUE);
		double x, y;
		
		for (node_data iterable_element : path) // Iterates over Nodes and makes their points on the graph.
		{
			if(iterable_element.getLocation() == null) 
				throw new RuntimeException("The Location of this node is null: "+iterable_element.getKey()); 
			
			x = iterable_element.getLocation().x();
			y = iterable_element.getLocation().y();
			
			StdDraw.text(x, y+0.05, Integer.toString(iterable_element.getKey()));
			StdDraw.point(x, y);
		}
	} 
	
	/**
	 * This function draws the path given by TSP on the graph when given the List.
	 * @param l1 - The list needed for TSP.
	 */
	public static void change_graph2(List<Integer> l1) 
	{
		Graph_Algo ga = new Graph_Algo();
		ga.init(g1);
		List<node_data> path = ga.TSP(l1);
		
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.YELLOW);
		
		double srcX,srcY,destX,destY;
		
		for (int i = 0; i < path.size()-1;i++) // Iterates over the path. 
		{
			node_data iterable_element = path.get(i);
			srcX = iterable_element.getLocation().x();
			srcY = iterable_element.getLocation().y();		
			destX = path.get(i+1).getLocation().x();
			destY = path.get(i+1).getLocation().y();
			
			StdDraw.line(srcX, srcY, destX, destY);
		}
		
		StdDraw.setPenRadius(0.025);
		StdDraw.setPenColor(StdDraw.BLUE);
		double x, y;
		
		for (node_data iterable_element : path) // Iterates over Nodes and makes their points on the graph.
		{
			if(iterable_element.getLocation() == null) 
				throw new RuntimeException("The Location of this node is null: "+iterable_element.getKey()); 
			
			x = iterable_element.getLocation().x();
			y = iterable_element.getLocation().y();
			
			StdDraw.text(x, y+0.05, Integer.toString(iterable_element.getKey()));
			StdDraw.point(x, y);
		}
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
}
