package piece;

import Board.Board;
import ChessSystem.Game;
import Enum.*;
import ChessSystem.Position;
import java.util.ArrayList;
import java.util.Scanner;

public class Pawn extends Piece {
    public Pawn(Position pos, Color color) {
        super(pos, color, 1,   PieceType.Pawn);
        initial = "P";
        this.score = 1;
        if(color == Color.BLACK)
            initial = initial.toLowerCase();
    }

    @Override
    public Pawn clone() {
        Pawn cloned = new Pawn(position(), color());
        cloned.initial = this.initial;
        return cloned;
    }

    @Override
    public Position[] getLegalMoves(Board board) {
        // 기본 이동
        int[] d = {this.color() == Color.WHITE ? -1 : 1, 0};
        // 공격 규칙
        int[][] attack = {{this.color() == Color.WHITE ? -1 : 1, -1}, {this.color() == Color.WHITE ? -1 : 1, 1}};
        ArrayList<Position> moves = new ArrayList<>();

        int newRank = position().getRank() + d[0];
        int newFile = position().getFile();

        // 한 칸 앞이 비어있는지
        if (board.getPiece(newRank, newFile) == null) {
            moves.add(new Position(newRank, newFile));

            // 처음 위치인 경우 두 칸 앞이 비어있는지 확인
            if (position().getRank() == (this.color() == Color.WHITE ? 6 : 1)) {
                newRank += d[0];
                if (board.getPiece(newRank, newFile) == null)
                    moves.add(new Position(newRank, newFile));
            }// if first location
        }
        
        for(int[] a : attack){
            newRank = position().getRank() + a[0];
            newFile = position().getFile() + a[1];
            // 공격 범위가 보드를 벗어나는 경우 처리
            if(newFile < 0 || newFile > 7) continue;
            
            // 공격 범위에 적이 없는 경우
            if(board.getPiece(newRank, newFile) == null) continue;

            // 공격 범위에 적이 있는 경우
            if (board.getPiece(newRank, newFile).color() != this.color())
                moves.add(new Position(newRank, newFile));
        }
        
        // 앙파상이 가능한 경우
        switch (canEnpassant(board)){
            case 1: // 우측으로 앙파상하는 경우
                moves.add(new Position(position().getRank() + d[0], position().getFile() + 1));
                break;
            case -1: // 좌측으로 앙파상하는 경우
                moves.add(new Position(position().getRank() + d[0], position().getFile() - 1));
                break;
            default:
        }

        return moves.toArray(new Position[moves.size()]);
    }

    @Override
    public void position(Board board, Position pos) {
        super.position(pos);
        if(pos.getRank() == (this.color() == Color.WHITE ? 0 : 7))
            promotion(board);
    }

    public int canEnpassant(Board board){
        // 마지막으로 움직인 기물이, 상대 진영 폰이며, 2칸 전진을 한 경우
        if(Game.lastMovedPiece() instanceof Pawn pawn && pawn.color() != this.color() && pawn.isMovedTwoSteps()){
            // 그 기물이 앙파상 범위에 들었다면 앙파상 방향을 반환
            int rank = pawn.position().getRank();
            int file = pawn.position().getFile();
            if(rank == position().getRank()){
                if(file == position().getFile() + 1) return 1;
                if(file == position().getFile() - 1) return -1;
            }
        }

        return 0;
    }

    public boolean isMovedTwoSteps() { return movedTwoSteps;}
    public void movedTwoSteps(boolean b){ movedTwoSteps = b;}

    private void promotion(Board board) {
        System.out.print("Choose Promotion: Rook, Knight, Bishop, Queen\n=>");
        Scanner sc = new Scanner(System.in);

        Piece promotedPiece = null;
        do {
            String piece = sc.nextLine();
            switch (piece) {
                case "Rook":
                    promotedPiece = new Rook(position(), color());
                    break;
                case "Knight":
                    promotedPiece = new Knight(position(), color());
                    break;
                case "Bishop":
                    promotedPiece = new Bishop(position(), color());
                    break;
                case "Queen":
                    promotedPiece = new Queen(position(), color());
                    break;
                default:
                    break;
            }
        }while(promotedPiece == null);
    }


    private boolean movedTwoSteps = false;
}


