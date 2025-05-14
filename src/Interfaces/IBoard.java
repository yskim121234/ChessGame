package Interfaces;

import ChessSystem.Position;
import Enum.Color;
import piece.Piece;

import java.util.ArrayList;


public interface IBoard {
    Piece getPiece(Position pos);
    void setPiece(Position pos, Piece piece);
    void movePiece(Position from, Position to);
    Piece[] getAllPieces(Color color);
    IBoard clone();
}
