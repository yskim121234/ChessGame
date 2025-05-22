package GUI;

import javax.swing.*;
import java.awt.*;

public class GameEnd extends JOptionPane {
    public int showGameEndMessage(Component parent, String message){
        return JOptionPane.showOptionDialog(parent, message, "Promotion"
                ,JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null );

    }

    private String[] answer = {"예"};
}
