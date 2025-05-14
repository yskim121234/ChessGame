package piece;

import Board.Board;
import ChessSystem.Position;
import Enum.Color;
import Enum.PieceType;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Position pos, Color color) {
        super(pos, color, 3, PieceType.Bishop);
        miner = true;
        initial = "B";
        if(color == Color.BLACK)
            initial = initial.toLowerCase();
    }

    @Override
    public Bishop clone() {
        Bishop cloned = new Bishop(position(), color());
        cloned.initial = this.initial;
        return cloned;
    }

    @Override
    public Position[] getLegalMoves(Board board) {
        int[][] directions = {{1,-1}, {1,1}, {-1,-1}, {-1, 1}};
        ArrayList<Position> moves = new ArrayList<>();

        for(int[] d : directions) {
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
        // 비숍 특성: 같은 랭크 or 같은 파일로 이동할 수 없다.
        if(target.getRank() == position().getRank() || target.getFile() == position().getFile())
            return null;
        
        Position[] moves = getLegalMoves(board);
        for(Position move : moves){
            // 이동 가능하면
            if(move.equals(target)){
                int[] direction = new int[2];
                 direction[0] = move.getRank() > position().getRank() ? 1: -1;
                 direction[1] = move.getFile() > position().getFile() ? 1: -1;

                 ArrayList<Position> path = new ArrayList<>();
                 Position cur = position();
                 while(!cur.equals(target)){
                     cur.setPosition(cur.getRank() + direction[0], cur.getFile() + direction[1]);
                     path.add(cur);
                 }
                 return path.toArray(new Position[path.size()]);
            }
        }

        // 이동이 불가능한 경우
        return null;
    }
}
