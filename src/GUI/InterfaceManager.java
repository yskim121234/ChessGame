package GUI;

import Enum.Color;

public class InterfaceManager {
    public static void show(){
        frame.setVisible(true);
    }

    public static void hide(){
        frame.setVisible(false);
    }

    public static void update(){
        frame.updateFrame();
    }

    public static Frame getFrame(){return frame;}

    public static int promotion(Color color){return frame.promotion(color);}


    private static Frame frame = new Frame();
}
