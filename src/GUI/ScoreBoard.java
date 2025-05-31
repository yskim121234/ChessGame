package GUI;

import ChessSystem.Game;

import javax.swing.*;
import Enum.Color;
public class ScoreBoard extends JPanel {
    public ScoreBoard(){
        setSize(800, 100);
        setLayout(null);

        scores = new JLabel[2];
        scores[0] = new JLabel(Game.player(Color.WHITE).name() + ": 0");
        scores[1] = new JLabel(Game.player(Color.BLACK).name() + ": 0");

        for(int i = 0; i < 2; i++){
            scores[i].setSize(400, 100);
            scores[i].setFont(new java.awt.Font("Dialog", 1, 48));
            scores[i].setLocation(i*400, 780);
            add(scores[i]);
        }
    }

    //TODO 점수 업데이트가 안됨
    public void updateScore(int white, int black){
        scores[0].setText(Game.player(Color.WHITE).name()+": " + white);
        scores[1].setText(Game.player(Color.BLACK).name()+": " + black);
        for(JLabel label : scores) label.repaint();
    }

    private JLabel[] scores;
}
