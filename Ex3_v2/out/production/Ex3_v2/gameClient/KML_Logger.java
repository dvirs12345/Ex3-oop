package gameClient;

import utils.Point3D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class KML_Logger
{
    public int scenario; // The scenario the logger is working on.
    private StringBuffer logger; // String to hold the kml file.

    public KML_Logger(int scenario)
    {
        this.scenario = scenario;
        this.logger = new StringBuffer();
        makeheader();
    }

    /**
     * Adds the head text of the kml file to logger.
     */
    private void makeheader()
    {
        this.logger.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                "<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" +
                "  <Document>\r\n" +
                "    <name>" + "Game level :"+scenario + "</name>" +"\r\n");

        makeStyles();
    }

    /**
     * Adds the style for apples, bananas and robots.
     */
    private void makeStyles()
    {
        // Apple style.
        this.logger.append("<Style id=\"Apple\">\r\n" +
                "     <IconStyle>\r\n" +
                "    <scale>1.0</scale>\r\n" +
                "       <Icon>\r\n" +
                "         <href>apple.png</href>\r\n" +
                "       </Icon>\r\n" +
                "     </IconStyle>\r\n" +
                "   </Style>\n");

        // Banana style.
        this.logger.append("<Style id=\"Banana\">\r\n" +
                "  <IconStyle>\r\n" +
                "    <scale>1.0</scale>\r\n" +
                "    <Icon>\r\n" +
                "      <href>banana.png</href>\r\n" +
                "    </Icon>\r\n" +
                "  </IconStyle>\r\n" +
                "</Style>\n");

        // Robot style.
        this.logger.append("<Style id=\"Robot\">\r\n" +
                "  <IconStyle>\r\n" +
                "    <scale>1.0</scale>\r\n" +
                "    <Icon>\r\n" +
                "      <href>robot.png</href>\r\n" +
                "    </Icon>\r\n" +
                "  </IconStyle>\r\n" +
                "</Style>\n");
    }

    /**
     * Adds a placemark to logger.
     * @param id - The id (is it a banana, apple or robot).
     * @param location - point3D Location of where the object is located.
     */
    public void addPlacemark(String id, Point3D location)
    {
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        LocalDateTime timeStamp = LocalDateTime.now();
        String loca = location.toString();

        this.logger.append("    <Placemark>\r\n" +
                "      <TimeStamp>\r\n" +
                "        <when>" + timeStamp+ "</when>\r\n" +
                "      </TimeStamp>\r\n" +
                "      <styleUrl>#" + id + "</styleUrl>\r\n" +
                "      <Point>\r\n" +
                "        <coordinates>" + loca + "</coordinates>\r\n" +
                "      </Point>\r\n" +
                "    </Placemark>\r\n");
    }

    /**
     * Adds the ending of the kml file and calls a method to save it.
     * @throws FileNotFoundException
     */
    public void endkml() throws FileNotFoundException
    {
        this.logger.append("</Document>\r\n" +
                "</kml>");
        writeAndSave();
    }

    /**
     * Actually writes to and saves the kml file.
     * @throws FileNotFoundException
     */
    private void writeAndSave() throws FileNotFoundException
    {
        File myfile = new File("data/" + this.scenario + ".kml");
        PrintWriter print = new PrintWriter(myfile);
        print.write(this.logger.toString());
        print.close();
    }
}
