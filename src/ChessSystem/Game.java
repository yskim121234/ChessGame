package ChessSystem;

import Board.Board;
import Enum.*;
import piece.*;
import Player.Player;

import java.util.ArrayList;

public class Game {
    public static boolean move(Position from, Position to){
        // 이동한게 킹이고 킹이 캐슬링이 가능한 상황이었다면(= 처음 위치에서 이동하지 않았다는 전제가 성립된다면)
        // 캐슬링을 제외한 이동시 파일의 변화는 1 혹은 0이므로 파일의 변화가 1을 초과하면 캐슬링을 했다고 볼 수 있다.
        // 이동한 파일에 따라 퀸사이드 킹사이드를 판단해 룩을 올바른 위치로 이동시킨다.
        if(!board.canMove(from, to)) return false;
        Piece fromPiece = board.getPiece(from);

        int canEnpassant = 0;
        int canCastle = 0;

        // 움직일 기물이 폰인 경우 앙파상 검사
        if(fromPiece.pieceType() == PieceType.Pawn)
            canEnpassant = ((Pawn)fromPiece).canEnpassant(board);
        
        if(fromPiece.pieceType() == PieceType.King)
            canCastle = ((King)fromPiece).canCastling(board);

        board.movePiece(from, to);

        if(canEnpassant != 0){
            int fromFile = fromPiece.position().getFile();
            // 폰이 앙파상 가능한 경우
            switch(canEnpassant){
                case 1: // 앙파상 위치로 이동하는지 확인
                    if(to.getFile() == fromFile+1){
                        board.setPiece(new Position(from.getRank(), fromFile + 1), null);
                        addScore(fromPiece.color(), 1);
                    }break;
                case -1:
                    if(to.getFile() == fromFile-1){
                        board.setPiece(new Position(from.getRank(), fromFile -1), null);
                        addScore(fromPiece.color(), 1);
                    }
                    break;
            }
        }
        if(canCastle != 0){
            switch(to.getFile()){
                case 2:
                    if(fromPiece.color() == Color.WHITE) board.movePiece(new Position(7, 0), new Position(7, 3));
                    else board.movePiece(new Position(0, 0), new Position(0, 3));
                    System.out.println("QueenSide Castling\n");
                    break;
                case 6:
                    if(fromPiece.color() == Color.WHITE) board.movePiece(new Position(7, 7), new Position(7, 5));
                    else board.movePiece(new Position(0, 7), new Position(0, 5));
                    System.out.println("KingSide Castling\n");
                    break;
                default: break;
            }
        }

        whiteTurn = !whiteTurn;
        System.out.println(whiteTurn ? "백" : "흑" + "진영 차례");
        lastMovedPiece = fromPiece;
        return true;
    }
    public static boolean isChecked(Color color) {
        return isChecked(board, color);
    }
    public static boolean isChecked(Board board, Color color) {
        // 킹을 찾아서
        Piece king = findKing(board, color);
        if(king == null){
            System.out.println(color == Color.WHITE ? "백" : "흑" + "킹을 못찾았음. 몬가 잘못됨.");
            System.exit(0);
        }
        Piece[] attackers = attackingPieces(board, king.position(), reverseColor(color));
        // 킹을 공격 중인 기물이 있으면 체크
        return attackers.length != 0;
    }

    public static boolean isCheckmate(Color color){
        return isCheckmate(board, color);
    }
    public static boolean isCheckmate(Board board, Color color){
        // 체크가 아니면 체크메이트도 아님.
        if(!isChecked(board, color)) return false;
        
        // 킹을 찾아서
        Piece king = findKing(board, color);
        if(king == null){
            System.out.println(color == Color.WHITE ? "백" : "흑" + "킹을 못찾았음. 몬가 잘못됨.");
            System.exit(0);
        }
        
        Position[] kingsMove = king.getLegalMoves(board);
        // 킹의 특성: 공격 받지 않는 곳으로만 이동 가능하다.
        // => 킹이 이동 가능하다면 체크메이트가 아니다.
        if(kingsMove.length != 0) return false;
        
        // 킹을 공격하는 기물들
        Piece[] attackers = attackingPieces(board, king.position(), reverseColor(color));

        // 킹이 못 움직이는데 동시에 2개 이상의 공격을 받고 있으면 체크메이트이다.
        if(attackers.length >= 2) return true;


        // 킹이 이동 할 수 없고 1개의 기물에게 공격 받고 있을 경우
        Piece attacker = attackers[0];

        // 경로를 고려하지 않는 기물
        // 폰-> 바로 붙어서 공격하기 때문에 경로가 없음.
        // 나이트-> 기물을 건너뛰기 때문에 막을 수 없음.
        //Todo instanceof => pieceType == PieceType.?
        if(!(attacker instanceof Bishop || attacker instanceof Rook || attacker instanceof  Queen)){
            // 공격 기물 위치로 이동 가능한 기물이 없으면 체크메이트이다.
           Piece[] defenders = attackingPieces(board, attacker.position(), reverseColor(color));
           return defenders.length == 0;
        }
        else{// 공격 기물이 비숍, 룩, 퀸 중 하나인 경우
            // 공격자 to 공격 받는 킹의 경로를 구한다.
            Position[] path = attacker.pathOfAttack(board, king.position());
            Piece[] canDefencePath = new Piece[0];
            for(Position pos: path)
                canDefencePath = attackingPieces(board, pos, reverseColor(color));

            Piece[] canDefenceAttacker = attackingPieces(board, attacker.position(), reverseColor(color));

            // 공격 경로를 막거나 공격 기물을 잡을 수 있는 방어 기물이 없으면 체크메이트이다.
            return canDefencePath.length + canDefenceAttacker.length == 0;
        }
    }

    public static boolean isStalemate(Color color){
        if(isChecked(color)) return false;
        // 체크가 아닌데, 못움직이면 스테일메이트
        Piece[] pieces = board.getAllPieces(color);

        // 모든 기물 중 단 하나라도 가능한 수가 있으면 스테일메이트가 아님
        for(Piece piece : pieces){
            if(piece.getLegalMoves(board).length != 0) return false;
        }
        return true;
    }
    
    // 3회 동형 규칙 구현 안함-> 연산량이 과하게 많아질 것으로 예상됨.
    // Piece 배열이 아닌 해시를 이용했어야 할 듯
    public static boolean isDraw(Color color){
        // 50수 규칙에 의한 무승부
        if(fiftyMoveRule >= 100) return true;

        Piece[] myPieces = board.getAllPieces(color);
        Piece[] opponentPieces = board.getAllPieces(reverseColor(color));

        // 킹 vs 킹 무승부
        if(myPieces.length <= 1 && opponentPieces.length <= 1) return true;

        // 마이너 기물 이외의 기물을 가지고 있으면 체크메이트 가능함
        ArrayList<Piece> myMiners = new ArrayList<>();
        for(Piece piece : myPieces){
            if(piece.isMiner() && !(piece instanceof King)) myMiners.add(piece);
            else return false;
        }

        ArrayList<Piece> opponentMiners = new ArrayList<>();
        for(Piece piece : opponentPieces){
            if(piece.isMiner() && !(piece instanceof King)) opponentMiners.add(piece);
            else return false;
        }

        switch (myMiners.size() + opponentMiners.size()) {
            // 킹 + 마이너 vs 킹 무승부
            case 1 -> {
                return true;
            }
            case 2 -> {
                // 킹 + 같은 마이너 2개 vs 킹 무승부
                if (myMiners.size() == 2)
                    return myMiners.get(0).getClass().equals(myMiners.get(1).getClass());
                if (opponentMiners.size() == 2)
                    return opponentMiners.get(0).getClass().equals(opponentMiners.get(1).getClass());

                // 킹 + 마이너 vs 킹 + 마이너 무승부
                return true;
            }
            // 마이너 기물의 총합이 3개 이상인 경우는 체크메이트 가능
            default -> {
                return false;
            }
        }
    }

    public static Piece[] attackingPieces(Position attackedPos, Color attacker) {
        return attackingPieces(board, attackedPos, attacker);
    }
    public static Piece[] attackingPieces(Board board, Position attackedPos, Color attacker){
            // 공격 진영의 모든 기물을 가져온다.
            Piece[] pieces = board.getAllPieces(attacker);

            ArrayList<Piece> attackPieces = new ArrayList<>();
            for(Piece piece : pieces){
                // 공격 범위가 짧은 기물들은 간단한 연산으로 걸러준다.
                // 킹은 공격 못함
                if(piece instanceof King) continue;

                if(piece instanceof Pawn || piece instanceof Knight){
                    // 거리 절댓값을 구해서
                    int distanceRank = attackedPos.getRank() - piece.position().getRank();
                    if(distanceRank < 0) distanceRank*=-1;

                    int distanceFile = attackedPos.getFile() - piece.position().getFile();
                    if(distanceFile < 0) distanceFile*=-1;

                    // 폰의 경우 [1,-1], [1,1] 밖에 공격하지 못하기 때문에 rank 나 file 거리가 1을 초과하면 공격할 수 없음.
                    if(piece instanceof Pawn && (distanceRank > 1 || distanceFile > 1)) continue;
                    if(piece instanceof Pawn && attackedPos.getFile() == piece.position().getFile()) continue;
                    // 나이트의 경우
                    // 같은 rank나 같은 file을 공격할 수 없음
                    // rank 거리가 2 file 거리가 1이거나, rank 거리가 1 file 거리가 2 둘 중 하나를 충족하지 않으면 공격할 수 없음.
                    if(piece instanceof Knight) {
                        if (distanceRank == 0 || distanceFile == 0) continue;
                        if (!((distanceRank == 2 && distanceFile == 1) || (distanceRank == 1 && distanceFile == 2))) continue;
                    }
                }

                // 기물의 이동 가능 위치를 가져온다.
                Position[] moves = piece.getLegalMoves(board);

                for(Position move : moves){
                    // 대상 위치로 이동 가능하면 리스트에 추가, 이 기물의 이후 이동 범위는 탐색할 필요가 없다.
                    if(move.equals(attackedPos)) {
                        attackPieces.add(piece);
                        break;
                    }
                }
            }

            return attackPieces.toArray(new Piece[attackPieces.size()]);
        }

    public static Piece findKing(Color color) {
        return findKing(board, color);
    }

    public static Piece findKing(Board board, Color color){
        // 해당 진영의 모든 기물들을 가져와서
        Piece[] pieces = board.getAllPieces(color);
        for (Piece p : pieces) {
            // 기물의 클래스가 킹이면 그 기물 반환
            if(p instanceof King) {
                return p;
            }
        }
        return null;
    }

    public static  void addScore(Color color, int score){ if(color == Color.WHITE){ white.addScore(score); } else { black.addScore(score); }}

    public static Board getBoard(){return board;}

    public static Color reverseColor(Color color){return color == Color.WHITE ? Color.BLACK : Color.WHITE;}

    public static Player player(Color color){return color == Color.WHITE ? white: black;}

    public static Color whoTurn(){return whiteTurn ? Color.WHITE: Color.BLACK;}

    public static void fiftyMoveRuleCount(){fiftyMoveRule++;}

    public static void fiftyMoveRuleReset(){fiftyMoveRule = 0;}

    public static Piece lastMovedPiece(){
        return lastMovedPiece;
    }

    private static Piece lastMovedPiece = null;
    private static Board board = new Board();
    private static Player white = new Player();
    private static Player black = new Player();
    private static boolean whiteTurn = true;
    private static int fiftyMoveRule = 0;
}