package GUI;

import Board.Board;
import ChessSystem.Game;
import ChessSystem.Position;
import piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static java.awt.Image.SCALE_SMOOTH;

public class BoardPanel extends JPanel {
    public BoardPanel(){
        setSize(800, 800);
        setLayout(null);
        setLocation(0, 0);
        step = 0;
        updateBoard();
    }

    public void updateBoard(){
        removeAll();

        Board board = Game.getBoard();
        for(int rank = 0; rank < 8; rank++)
            for(int file = 0; file < 8; file++){
                Piece p = board.getPiece(rank, file);
                JButton btn;
                if(p == null)
                    btn = new JButton("");
                else {
                    Image img = p.getImageIcon().getImage();
                    btn = new JButton(new ImageIcon(img.getScaledInstance(100, 100, SCALE_SMOOTH)));
                }
                if((rank + file) % 2 == 0) btn.setBackground(Color.WHITE);
                else btn.setBackground(Color.BLACK);
                btn.setBorder(null);
                final int rank_ = rank;
                final int file_ = file;
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        switch (step){
                            case 0: // TODO 이동 가능한 칸 표시하는데 오류 있음
                                select = new int[]{rank_, file_};
                                step++;
                                btns[rank_][file_].setBorder(BorderFactory.createLineBorder(Color.RED, 5));
                                /*
                                Piece p = board.getPiece(rank_, file_);
                                if(p == null) break;
                                Position[] legalMoves = p.getLegalMoves(board);
                                for(Position pos : legalMoves){
                                    if(!board.canMove(new Position(rank_, file_),pos)) continue;
                                    btns[pos.getRank()][pos.getFile()].setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
                                }
                                */
                                break;
                            case 1:
                                if(Game.move(new Position(select[0], select[1]),new Position(rank_, file_))) {
                                    updateBoard();
                                }
                                step = 0;
                                for(JButton[] btns_ : btns) for(JButton btn_ : btns_) btn_.setBorder(null);
                                break;
                            default:
                        }
                    }});
                btn.setSize(100, 100);
                btn.setLocation(file * 100, rank * 100);
                btns[rank][file] = btn;
                add(btn);
            }
        board.showBoard();
        updateUI();
    }

    public JButton[][] getBtns(){
        return btns;
    }

    private int[] select;
    private int step;
    private JButton[][] btns = new JButton[8][8];
}
