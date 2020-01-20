package gameClient;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gui.Graph_Gui;
import oop_dataStructure.OOP_DGraph;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MyGameGUITest
{
//    @org.junit.jupiter.api.Test
    void init() throws JSONException, InterruptedException
    {
        MyGameGUI mgg = new MyGameGUI();
        mgg.init(0);
        TimeUnit.SECONDS.sleep(60);
    }

//    @org.junit.jupiter.api.Test
    void initiateGame() throws JSONException
    {
        MyGameGUI mgg = new MyGameGUI();
        mgg.initiateGame(20);
    }
//  @Test
  void initiatManualeGame() throws JSONException
  {
      MyGameGUI mgg = new MyGameGUI();
      mgg.initiateManualGame(20);
  }

    @org.junit.jupiter.api.Test
    void runGui() throws JSONException, InterruptedException, FileNotFoundException
    {
        MyGameGUI mgg = new MyGameGUI();
        mgg.runGui();
        TimeUnit.SECONDS.sleep(60);
    }
}