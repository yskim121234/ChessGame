package Player;

import ChessSystem.Game;
import ChessSystem.Position;
import Interfaces.IPlayer;
import ChessSystem.InputManager;

public class Player implements IPlayer {
    public Player(){
        score = 0;
    }
    @Override
    public void move() {
        Position from = null, to = null;
        while(true) {
            InputManager im = new InputManager();
            do {
                from = im.inputPosition();
                to = im.inputPosition();
                if (from == null && to == null)
                    System.out.print("입력 오류, 재입력.");
            } while (from == null && to == null);

            if (Game.move(from, to)) break;
            else System.out.println("<<불가능한 이동>>");
        }
    }
    @Override
    public void addScore(int score) {
        this.score += score;
    }

    public int score(){return score;}

    private int score;
}
