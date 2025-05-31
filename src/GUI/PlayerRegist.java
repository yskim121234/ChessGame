package GUI;

import javax.swing.*;

import ChessSystem.Game;
import Enum.Color;

import java.awt.*;

public class PlayerRegist extends JFrame {
    public PlayerRegist(){
        setLayout(new GridLayout(2,1));
        p1 = new JPanel();
        p1.setLayout(new FlowLayout());

        p2=  new JPanel();
        p2.setLayout(new FlowLayout());

        t1 =  new JTextField(15);
        p1.add(l1);
        p1.add(t1);

        t2 =  new JTextField(15);
        p1.add(l2);
        p1.add(t2);

        btn = new JButton("Start");
        btn.addActionListener(e->{
            Game.setPlayerName(t1.getText(), t2.getText());
            InterfaceManager.setFrame();
            InterfaceManager.getFrame().startGame();
            dispose();
        });
        btn.setSize(150,30);
        p2.add(btn);
        add(p1);
        add(p2);

        setSize(400,100);
    }

    private JPanel p1, p2;
    private JLabel l1 =  new JLabel("White"), l2 =   new JLabel("Black");
    private JTextField t1,t2;
    private JButton btn;
}
