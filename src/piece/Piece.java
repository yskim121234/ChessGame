package piece;

import Enum.*;
import ChessSystem.Position;
import Interfaces.IPiece;
import Board.Board;

public class Piece implements IPiece {

    public Piece(int rank, int file, Color color, int score, PieceType pieceType) {
        position = new Position(rank, file);
        this.color = color;
        this.score = score;
        this.miner = false;
        this.type = pieceType;
    }
    public Piece(Position pos, Color color, int score, PieceType pieceType) {
        position = pos;
        this.color = color;
        this.score = score;
        this.type = pieceType;
    }

    @Override
    public Position[] getLegalMoves(Board board) {
        return null;
    }

    public Position[] pathOfAttack(Board board, Position target){return null;}

    @Override
    public String toString() {
        return "["+initial+"]";
    }

    @Override
    public int score() {return score;}
    @Override
    public Color color() {
        return color;
    }
    @Override
    public Position position() {
        return position;
    }
    @Override
    public void position(Position pos) {
        position = pos;
    }
    public void position(Board board, Position pos) {
        position = pos;
    }
    @Override
    public Piece clone(){ return new Piece(position, color, score, type);}
    @Override
    public boolean isMiner() {return miner;}
    public PieceType pieceType() {return type;}

    protected int score;
    protected String initial;
    protected boolean miner;
    private Position position;
    private Color color;
    private PieceType type;

}
