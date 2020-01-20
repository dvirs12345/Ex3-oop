package gameClient;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

/**
 * This class represents a Robot.
 * it has an id, value, src, dest, pos (point3D), speed.
 */
public class Robot
{
    public int id; // id
    public double value;
    public int src; // Source
    public int dest; // destination
    private double speed = 1.0; // Speed
    public Point3D pos; // Position

    public Robot(){ ; }

    /**
     * This is an init function for a Robot. it gets a string representing a Robot and updates the attributes accordingly
     * @param robostr - A string representing a robot.
     * @throws JSONException
     */
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
