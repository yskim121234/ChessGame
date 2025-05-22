package GUI;

import javax.swing.*;
import Enum.Color;

import java.awt.*;

public class PromotionMenu extends JOptionPane {
    public int showPromotionMenu(Component parent, Color color){
        return JOptionPane.showOptionDialog(parent, "기물을 선택하시오.", "Promotion"
                    ,JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, answer, null );

    }

    private String[] answer = {"Queen", "Rook", "Bishop", "Knight"};
}
