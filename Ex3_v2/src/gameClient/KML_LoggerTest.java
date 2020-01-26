package gameClient;

import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class KML_LoggerTest {

    @Test
    void addPlacemark()
    {
        KML_Logger kml = new KML_Logger(0);
        kml.addPlacemark("robot.png",new Point3D(0,0));
        String s = kml.getLogger().toString();
    }

    @Test
    void endkml()
    {

    }
}