package GUI;

import ChessSystem.Game;
import Enum.Color;
import javax.swing.*;

public class Frame extends JFrame {
    public Frame(){
        setVisible(true);
        setSize(800, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Chess");

        panel = new BoardPanel();
        add(panel);

        score = new ScoreBoard();
        score.setLocation(0, 800);
        add(score);

    }

    public int promotion(Color color){
        PromotionMenu menu = new PromotionMenu();
        return menu.showPromotionMenu(this, color);
    }

    public void updateFrame(){
        panel.updateBoard();
        score.updateScore(Game.player(Color.WHITE).score(), Game.player(Color.BLACK).score());
        repaint();
    }

    private ScoreBoard score;
    private BoardPanel panel;
}
