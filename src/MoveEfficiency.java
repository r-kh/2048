public class MoveEfficiency implements Move, Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles, score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public void move() {}

    @Override
    public int compareTo(MoveEfficiency o) {
        int tiles = Integer.compare(numberOfEmptyTiles, o.numberOfEmptyTiles);
        if (tiles != 0) {
            return tiles;
        } else {
            return Integer.compare(score, o.score);
        }
    }
}
