package gameClient;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

public class Robot
{
    public int id; // id
    public double value;
    public int src; // Source
    public int dest; // destination
    public double speed = 1.0; // Speed
    public Point3D pos; // Position
    public double timeToDest;
    public ArrayList<Integer> claimedPath;

    public Robot(){; }
    public Robot(String robostr) throws JSONException
    {
    	this.init(robostr);
    }

    public void init(String robostr) throws JSONException
    {
        JSONObject line = new JSONObject(robostr);
        JSONObject ttt = line.getJSONObject("Robot");

        this.id = (int) ttt.get("id");
        this.value = (double) ttt.get("value");
        this.src = (int) ttt.get("src");
        this.dest = (int) ttt.get("dest");
        this.speed = (double) ttt.get("speed");
        String[] temp = ((String) ttt.get("pos")).split(",");
        this.pos = new Point3D(Double.parseDouble(temp[0]) , Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
    }
}
