package gameClient;

import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

public class Fruit
{
    public double value;
    public int type;
    public Point3D pos;

    public Fruit() { ; }

    public void init(String fruitstr) throws JSONException
    {
        JSONObject line = new JSONObject(fruitstr);
        JSONObject ttt = line.getJSONObject("Fruit");

        this.value = (double) ttt.get("value");
        this.type = (int) ttt.get("type");
        String[] temp = ((String) ttt.get("pos")).split(",");
        this.pos = new Point3D(Double.parseDouble(temp[0]) , Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
    }

}
