package piece;

import Board.Board;
import Enum.*;
import ChessSystem.Position;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Position pos, Color color) {
        super(pos, color, 3,  PieceType.Knight);
        miner = true;
        initial = "N";
        if(color == Color.BLACK)
            initial = initial.toLowerCase();
    }

    @Override
    public Knight clone() {
        Knight cloned = new Knight(position(), color());
        cloned.initial = this.initial;
        return cloned;
    }

    @Override
    public Position[] getLegalMoves(Board board) {
        int[][] directions = { {2, -1}, {2, 1}, {-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {-1, -2}, {-1, 2}};
        ArrayList<Position> moves = new ArrayList<>();

        for (int[] d : directions) {
            int newRank = position().getRank() + d[0];
            int newFile = position().getFile() + d[1];

            if(newRank < 0 || newRank > 7) continue;
            if(newFile < 0 || newFile > 7) continue;


            Piece piece = board.getPiece(newRank, newFile);
            if(piece == null) moves.add(new Position(newRank, newFile));
            else if (piece.color() != this.color()) moves.add(new Position(newRank, newFile));
        }

        return moves.toArray(new Position[moves.size()]);
    }
}
