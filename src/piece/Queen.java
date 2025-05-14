package piece;

import Board.Board;
import Enum.*;
import ChessSystem.Position;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(Position pos, Color color) {
        super(pos, color, 9, PieceType.Queen);
        initial = "Q";
        if(color == Color.BLACK)
            initial = initial.toLowerCase();
    }

    @Override
    public Queen clone() {
        Queen cloned = new Queen(position(), color());
        cloned.initial = this.initial;
        return cloned;
    }

    @Override
    public Position[] getLegalMoves(Board board) {
        int[][] direcitons = {{1,-1}, {1,1}, {-1,-1}, {-1, 1}, {1, 0}, {-1,0}, {0,1}, {0, -1}};
        ArrayList<Position> moves = new ArrayList<>();

        for(int[] d : direcitons) {
            int newRank = position().getRank();
            int newFile = position().getFile();
            do{
                newRank += d[0];
                newFile += d[1];
                // 배열에서 벗어나는 경우 종료
                if(newRank < 0 || 7 < newRank || newFile < 0 || 7 < newFile) break;

                Piece piece = board.getPiece(newRank, newFile);
                // 빈칸인 경우 수 후보에 추가
                if (piece == null) { moves.add(new Position(newRank, newFile));}
                // 아군인 경우 해당 방향 탐색 중지
                else if (piece.color() == this.color()) { break;}
                // 적인 경우 해당 위치를 수 후보에 추가 후 탐색 중지
                else if (piece.color() != this.color()) { moves.add(new Position(newRank, newFile)); break;}
            }while(true);
        }
        return moves.toArray(new Position[moves.size()]);
    }

    @Override
    public Position[] pathOfAttack(Board board, Position target){
        //퀸이 타겟으로 이동하는 방향
        int rankDiff = target.getRank() - position().getRank();
        int fileDiff = target.getFile() - position().getFile();

        int[] d = new int[]{Integer.compare(rankDiff, 0), Integer.compare(fileDiff, 0)};
        
        // 이동을 아예 안하는 경우는 제외
        if(d[0] == 0 && d[1] == 0) return null;

        // 대각선 이동, rank 직선 이동, file 직선 이동 모두 아닌 경우
        if(Math.abs(rankDiff) != Math.abs(fileDiff) && rankDiff !=0 && fileDiff != 0) return null;
        
        ArrayList<Position> path = new ArrayList<>();
        int rank = position().getRank() + d[0];
        int file = position().getFile() + d[1];

        while (rank != target.getRank() || file != target.getFile()){
            path.add(new Position(rank, file));
            rank += d[0];
            file += d[1];
        }

        return path.toArray(new Position[path.size()]);
    }
}
