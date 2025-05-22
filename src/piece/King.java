package piece;

import Board.Board;
import ChessSystem.Game;
import Enum.*;
import ChessSystem.Position;
import java.util.ArrayList;

public class King extends Piece {
    public King(Position pos, Color color) {
        super(pos, color, 0, PieceType.King);
        initial = "K";
        isMoved = false;
        castling = 0;
        if(color == Color.BLACK)
            initial = initial.toLowerCase();
    }

    @Override
    public King clone() {
        King cloned = new King(position(), color());
        cloned.initial = this.initial;
        cloned.isMoved = this.isMoved;
        cloned.castling = this.castling;
        return cloned;
    }

    @Override
    public Position[] getLegalMoves(Board board){
        int[][] directions = {{1,-1}, {1,0}, {1,1},
                              {0,-1},        {0,1},
                              {-1,-1},{-1,0},{-1,1}};

        ArrayList<Position> moves = new ArrayList<>();

        for (int[] d : directions) {
            int newRank = position().getRank() + d[0];
            int newFile = position().getFile() + d[1];

            if(newRank < 0 || newRank > 7) continue;
            if(newFile < 0 || newFile > 7) continue;

            Position newPos = new Position(newRank, newFile);
            Piece piece = board.getPiece(newPos);
            // 킹의 특성: 공격 받는 칸으로 이동할 수 없음
            if(Game.attackingPieces(newPos, Game.reverseColor(color())).length > 0)
                continue;
            
            // 빈 칸인 경우
            if(piece == null){
                // 그 칸을 공격 중인 기물이 없는 경우
                if(Game.attackingPieces(board, newPos, Game.reverseColor(color())).length == 0)
                    moves.add(new Position(newRank, newFile));
            }
            // 적 기물이 있는 경우
            else if (piece.color() != this.color()) {
                // 가상 보드 생성
                Board temp = board.clone();
                // 이동할 위치를 비우고
                temp.setPiece(newPos, null);

                Piece[] attacker = Game.attackingPieces(temp, newPos, Game.reverseColor(color()));
                // 그 위치를 공격하는 기물이 없으면 이동 가능하다 판단
                if(attacker.length == 0) moves.add(newPos);
            }
        }
        
        // 이동한적이 없는 경우 캐슬링 가능 여부 판단
        switch(canCastling(board)){
            case 0: break;
            case 1: moves.add(new Position(position().getRank(), position().getFile() - 2)); break;
            case 2: moves.add(new Position(position().getRank(), position().getFile() + 2)); break;
            case 3: moves.add(new Position(position().getRank(), position().getFile() - 2)); moves.add(new Position(position().getRank(), position().getFile() + 2)); break;
        }


        return moves.toArray(new Position[moves.size()]);
    }
    
    // 0: 캐슬링 불가 1: 퀸사이드, 2: 킹사이드, 3: 모두
    public int canCastling(Board board){
        // 캐슬링 조건
        // 1. 킹과 캐슬링 하려는 룩 사이는 비어있어야 한다.
        // 2. 캐슬링 하려는 킹과 룩은 이동한 적이 없어야 한다.
        // 3. 킹이 위치한 칸과 이동하는 경로 모두 공격 받지 않는 상태여야 한다.
        board.showBoard();
        // 체크 상태라면 캐슬링할 수 없다.
        if(Game.isChecked(board, color())) return 0;
        // 킹이 이동한적이 있다면 캐슬링할 수 없다.
        if(isMoved) return 0;

        boolean queenSide = true, kingSide = true;
        int rank = position().getRank();
        int file = position().getFile();

        // 룩의 시작 위치
        Position queenSidePos, kingSidePos;
        queenSidePos = new Position(rank, 0);
        kingSidePos = new Position(rank, 7);
        
        // 퀸 방향의 룩이 있는지 확인
        Piece piece = board.getPiece(queenSidePos);
        // 룩이 있고 같은 진영인경우
        if(piece instanceof Rook && piece.color() == color()){
            // 룩이 이동한적이 있으면 캐슬링 불가
            if(((Rook) piece).isMoved()) queenSide = false;
            else{
                // 킹 to 룩이 비어있지 않으면 캐슬링할 수 없다.
                for(int f = 1; f <= 3; f++){
                    if(board.getPiece(new Position(rank, file - f)) != null){
                        queenSide = false;
                        break;
                    }
                }
            }
        }

        // 캐슬링 경로에 대한 공격이 있는지 확인
        if(Game.attackingPieces(new Position(rank, file-1), Game.reverseColor(color())).length > 0) queenSide = false;
        if(Game.attackingPieces(new Position(rank, file-2), Game.reverseColor(color())).length > 0) queenSide = false;
        
        // 킹 방향의 룩이 있는지 확인
        piece = board.getPiece(kingSidePos);
        // 룩이 있고 같은 진영이라면
        if(piece instanceof Rook && piece.color() == color()){
            // 룩이 이동한적이 있으면 캐슬링 불가
            if(((Rook) piece).isMoved()) kingSide = false;
            else{
                // 룩과 킹 사이가 비어있는지 확인
                for(int f = 1; f <= 2; f++){
                    if(board.getPiece(new Position(rank, file + f)) != null){
                        kingSide = false;
                        break;
                    }
                }

            }
        }

        // 캐슬링 경로에 대한 공격이 있는지 확인
        if(Game.attackingPieces(new Position(rank, file+1), Game.reverseColor(color())).length > 0) kingSide = false;
        if(Game.attackingPieces(new Position(rank, file+2), Game.reverseColor(color())).length > 0) kingSide = false;

        // 결과 반환
        int result = 0;
        if(queenSide) result += 1;
        if(kingSide) result += 2;
        castling = result;
        return result;
    }

    public int castling(){ return castling;}

    public void moved(){ isMoved = true;}
    private int castling;
    private boolean isMoved;
}
