package Board;

import ChessSystem.*;
import Interfaces.IBoard;
import piece.*;
import Enum.*;
import java.util.ArrayList;

public class Board implements IBoard {

    public Board() {
        // 체크판은 8x8 사이즈이며, 행을 rank, 열을 file 이라고 한다.
        // 각 칸은 Square 라고 부른다.
        squares = new Piece[8][8];
        isClone = false;
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++) {
                // Create Pawn
                // 폰은 위, 아래에서 각각 두번째 행 모든 열에 배치된다.
                if(rank == 1 || rank == 6){
                    Color color;
                    if(rank == 6) color = Color.WHITE;
                    else color = Color.BLACK;

                    squares[rank][file] = new Pawn(new Position(rank, file), color);
                    continue;
                }
                // Creat else
                else if(rank == 0 || rank == 7){
                    // 나머지 기물들은 최상단행, 최하단행에서 룩-나이트-비숍-퀸-킹-비숍-나이트-룩 순으로 배치된다.
                    Color color;
                    if(rank == 7) color = Color.WHITE;
                    else color = Color.BLACK;

                    Position pos = new Position(rank, file);

                    switch (file){
                        case 0: case 7: //Rook
                            squares[rank][file] = new Rook(pos, color);
                            break;
                        case 1: case 6: //Knight
                            squares[rank][file] = new Knight(pos, color);
                            break;
                        case 2: case 5: //Bishop
                            squares[rank][file] = new Bishop(pos, color);
                            break;
                        case 3:// Queen
                            squares[rank][file] = new Queen(pos, color);
                            break;
                        case 4:// King
                            squares[rank][file] = new King(pos, color);
                            break;
                    }//switch(file) end
                    continue;
                }// if rank 0 or 7 end
                // 나머지 칸은 null 로 초기화해둔다.
                squares[rank][file] = null;
            }// for file
        }//for rank
    }// Board

    @Override// 해당 위치의 기물을 가져온다.
    public Piece getPiece(Position pos) {
        return squares[pos.getRank()][pos.getFile()];
    }
    public Piece getPiece(int rank, int file) {
        return squares[rank][file];
    }
    
    @Override// 해당 위치를 입력받은 기물로 설정하고 기물의 위치 정보를 업데이트한다.
    public void setPiece(Position pos, Piece piece) {
        squares[pos.getRank()][pos.getFile()] = piece;
        if(piece == null) return;
        piece.position(this, pos);
    }
    @Override// from 위치의 기물을 to 위치로 복사하고 from 을 비운다. 유의미한 이동이 이루어지면 true 아니면 false
    public void movePiece(Position from, Position to) {
        // 출발점에 위치한 기물(이를 공격 기물이라 칭한다.)을 확인한다.
        Piece piece = getPiece(from);
        if(piece == null) return;

        // 그 목표점에 위치한 기물(이하 방어 기물이라 칭한다.)을 확인한다.
        Piece defender = getPiece(to);

        // 목표점에 기물이 있다면
        if(defender != null){
            // 방어 기물을 잡은 것으로 판정, 방어 기물의 기물 점수만큼 공격자 진영의 점수를 더한다.
            Game.addScore(piece.color(), defender.score());
            System.out.println(piece + "-> " + defender + "/ get " + defender.score());
        }
        // 목표점에 공격 기물을 배치한다.
        setPiece(to, piece);
        // 출발점을 비운다.
        setPiece(from, null);
        System.out.println(piece + "->" + "[" + to + "]");

        if(piece.pieceType() == PieceType.King) ((King)piece).moved();
        if(piece.pieceType() == PieceType.Rook) ((Rook)piece).moved();
        if(piece.pieceType() == PieceType.Pawn) {
            int movedRank = to.getRank() - from.getRank();
            Pawn pawn = (Pawn)piece;
            if(movedRank == -2 || movedRank == 2) pawn.movedTwoSteps(true);
            else pawn.movedTwoSteps(false);
        }

        // 방어 진영이 체크 되었는지 확인
        if(Game.isChecked(Game.reverseColor(piece.color())))
            // 체크 되었으면 체크메이트인지 확인
            if(Game.isCheckmate(Game.reverseColor(piece.color()))){
                // 체크메이트라면 체크메이트 선언 후 게임 종료.
                System.out.println("===/Checkmate./===");
                System.exit(1);
            } else{
                // 체크메이트가 아니면 체크 선언
                System.out.println("===/Check/===");
            }
        else{
            if(Game.isStalemate(Game.reverseColor(piece.color()))){
                System.out.println("===/Stalemate./===");
                System.exit(1);
            }
        }
        // 폰의 이동 혹은 기물의 포혹은 50수 규칙을 초기화 한다.
        if(piece.pieceType() == PieceType.Pawn || defender != null) Game.fiftyMoveRuleReset();
        else Game.fiftyMoveRuleCount();

        if(Game.isDraw(Game.reverseColor(piece.color()))){
            System.out.println("===/Draw./===");
            System.exit(1);
        }

    }

    public boolean canMove(Position from, Position to){
        Piece piece = getPiece(from);
        
        // 출발점에 아무 기물도 없는 경우
        if(piece == null) return false;
        
        // 턴 진행 중인 진영의 기물이 아닌 경우
        if(Game.whoTurn() != piece.color()){
            System.out.println("본인 진영 기물을 선택하시오.");
            return false;
        }
        
        Position[] moves = piece.getLegalMoves(this);

        // 체크 상태인 경우
        if(Game.isChecked(this, piece.color())){
            // 킹 이외 기물
            if(!(piece.pieceType() == PieceType.King)){
                Position kingPos =  Game.findKing(piece.color()).position();
                Piece attacker = Game.attackingPieces(this,kingPos, Game.reverseColor(piece.color()))[0];
                
                // 공격 기물이 비숍, 룩, 퀸인 경우 이동 가능 조건
                if(attacker.pieceType() == PieceType.Bishop || attacker.pieceType() == PieceType.Rook || attacker.pieceType() == PieceType.Queen){
                    Position[] attackPath = attacker.pathOfAttack(this, kingPos);

                    // 공격 경로로의 이동
                    for(Position move : attackPath)
                        if(to.equals(move)) return true;
                }
                // 모든 공격 기물에 대한 이동 가능 조건

                // 공격 기물을 잡는 이동
                if(to.equals(attacker.position())) return true;
            }
            else{ // 킹인 경우
                for(Position move : moves) {
                    // 이동 범위 중 목표 위치가 있다면
                    if (move.equals(to)) return true;
                }
            }
        }
        else {
            for (Position move : moves)
                if (move.equals(to)){
                    // 가상 보드 생성
                    Board temp = clone();
                    temp.setPiece(from, null);
                    temp.setPiece(to, piece);

                    return !Game.isChecked(temp, piece.color());
                }
        }
        return false;
    }

    @Override // 해당 진영의 모든 기물 배열 반환한다.
    public Piece[] getAllPieces(Color color) {
        ArrayList<Piece> pieces = new ArrayList<>();
        // 모든 칸을 다 돌아서 진영이 일치하는 것들을 저장한다.
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++) {
                Piece piece = squares[rank][file];
                if(piece == null) continue;
                if(piece.color() == color)
                    pieces.add(squares[rank][file]);
            }
        }

        return pieces.toArray(new Piece[pieces.size()]);
    }

    @Override// 현재 보드와 똑같은 보드 객체를 반환한다.
    public Board clone() {
        Board clone = new Board();
        clone.isClone = true;
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++) {
                clone.squares[rank][file] = this.squares[rank][file] != null ? squares[rank][file].clone() : null;
            }
        }

        return clone;
    }

    public void showBoard(){
        for(int rank = -1; rank < 9; rank++) {
            //print rank left
            if(0<= rank && rank < 8)
                System.out.print(8-rank+" ");
            else{
                System.out.print("  ");
            }

            //print board
            for(int file = 0; file < 8; file++) {
                // print file top and bottom
                if(rank == -1|| rank == 8) {
                    System.out.print(" "+ (char)('a' + file)+ " ");
                    if(rank == -1 && file == 7)
                        System.out.println("    White: " + Game.player(Color.WHITE).score() + " Black: " + Game.player(Color.BLACK).score());
                    if(rank == 8 && file == 7)
                        System.out.printf("\n");
                    continue;
                }
                //print piece
                Piece piece = squares[rank][file];
                if(piece == null)
                    System.out.print("[ ]");
                else
                    System.out.print(piece.toString());
            } // for file
            //print rank right
            if(0<= rank && rank < 8)
                System.out.println(" " + (8-rank));
        }// for rank
    }
    public boolean isClone() {return isClone;}
    private boolean isClone;
    private Piece[][] squares;
}
