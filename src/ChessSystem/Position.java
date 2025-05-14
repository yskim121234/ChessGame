package ChessSystem;

public class Position {
    public Position(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }
    @Override
    public String toString() {
        return "Position [rank=" + (8-rank) + ", file=" + (char)(file+97) + "]";
    }
    public boolean equals(Position p) {
        return rank == p.getRank() && file == p.getFile();
    }
    public void setPosition(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public int getRank() {
        return rank;
    }
    public int getFile() {
        return file;
    }
    private int rank;
    private int file;
}
