import ChessSystem.Game;

public class Main {
    public static void main(String[] args) {
        Game.getBoard().showBoard();
        while(true) {
            Game.player(Game.whoTurn()).move();
            Game.getBoard().showBoard();
        }
    }
}
