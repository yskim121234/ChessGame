package piece;

import Board.Board;
import Enum.*;
import ChessSystem.Position;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(Position pos, Color color) {
        super(pos, color, 5, PieceType.Rook);
        initial = "R";
        isMoved = false;
        if(color == Color.BLACK)
            initial = initial.toLowerCase();
    }

    @Override
    public Rook clone() {
        Rook cloned = new Rook(position(), color());
        cloned.initial = this.initial;
        cloned.isMoved = this.isMoved;
        return cloned;
    }

    @Override
    public Position[] getLegalMoves(Board board) {
        int[][] direcitons = {{1, 0}, {-1,0}, {0,1}, {0, -1}};
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
        // 룩의 특성: 랭크와 파일이 모두 다른 칸으로는 이동할 수 없다.
        if(target.getRank() != position().getRank() && target.getFile() != position().getFile())
            return null;

        Position[] moves = getLegalMoves(board);
        for(Position move : moves){
            // 이동 가능하면
            if(move.equals(target)){
                int[] direction = new int[2];
                if(target.getRank() == position().getRank()){
                    direction[1] = target.getFile() > position().getFile() ? 1: -1;
                }
                else{
                    direction[0] = target.getRank() > position().getRank() ? 1: -1;
                }


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

    public void moved(){ isMoved = true;}
    public boolean isMoved(){ return isMoved;}
    private boolean isMoved;
}
