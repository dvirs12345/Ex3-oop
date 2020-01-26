package gameClient;

import java.io.FileNotFoundException;


import org.json.JSONException;

public class GameguiMain {
	 public static void main(String[] args) throws InterruptedException, FileNotFoundException, JSONException {
	        MyGameGUI mgg = new MyGameGUI();
	        mgg.runGui();
	     
	    }
}
