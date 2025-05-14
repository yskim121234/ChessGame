package Interfaces;

import Board.Board;
import ChessSystem.Position;
import Enum.Color;
import piece.Piece;

public interface IPiece {
    int score = 0;
    String initial = null;
    Color color = null;
    Position position = null;
    Position[] getLegalMoves(Board board);

    int score();
    Color color();
    Position position();

    void position(Position pos);
    Piece clone();
    boolean isMiner();

}
