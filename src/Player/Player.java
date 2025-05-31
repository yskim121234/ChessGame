package Player;

import ChessSystem.Game;
import ChessSystem.Position;
import Interfaces.IPlayer;
import ChessSystem.InputManager;

public class Player implements IPlayer {
    public Player(String name) {
        this.name = name;
        score = 0;
    }

    @Override
    public void addScore(int score) {
        this.score += score;
    }

    public String name() {return name;}
    public int score(){return score;}

    private String name;
    private int score;
}
