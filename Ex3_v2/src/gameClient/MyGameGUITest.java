package gameClient;





import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

import static org.junit.jupiter.api.Assertions.*;

class MyGameGUITest
{
//    @org.junit.jupiter.api.Test
    void init() throws JSONException, InterruptedException
    {
        MyGameGUI mgg = new MyGameGUI();
        //mgg.init(0);
        TimeUnit.SECONDS.sleep(60);
    }

//    @org.junit.jupiter.api.Test
    void initiateGame() throws JSONException, FileNotFoundException {
        MyGameGUI mgg = new MyGameGUI();
        mgg.initiateGame4(-31);
    }

//    @org.junit.Test
    void initiatManualeGame() throws JSONException
    {
        MyGameGUI mgg = new MyGameGUI();
        mgg.initiateManualGame(20);
    }

    @org.junit.jupiter.api.Test
    void runGui() throws JSONException, InterruptedException, FileNotFoundException
    {
        MyGameGUI mgg = new MyGameGUI();
        mgg.runGui(true);
        TimeUnit.SECONDS.sleep(60);
    }

//    @org.junit.jupiter.api.Test
    void displayAll() throws JSONException {
        MyGameGUI mgg = new MyGameGUI();
        mgg.scenario = 0;
        //mgg.displayall();
    }
}