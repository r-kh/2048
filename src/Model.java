import java.util.*;

/*
Этот класс будет содержать игровую логику и хранить игровое поле.
*/
public class Model {
    // поля
    private static final int FIELD_WIDTH = 4;
    private com.javarush.task.task35.task3513.Tile[][] gameTiles;
    public int score=0;   // хранить текущий счет
    public int maxTile=2; // хранит максимальный вес плитки на игровом поле
    private Stack<com.javarush.task.task35.task3513.Tile[][]> previousStates = new Stack<>(); // предыдущие состояния игрового поля
    private Stack<Integer> previousScores = new Stack<>(); // предыдущие счета
    private boolean isSaveNeeded=true;

    // конструкторы
    public Model() {
        resetGameTiles();
    }


    // методы
    private void addTile() {
        // должен изменять значение случайной пустой плитки в массиве gameTiles на 2 или 4 с вероятностью 0.9 и 0.1 соответственно.
        List<com.javarush.task.task35.task3513.Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()) {
            int index = (int) (Math.random() * emptyTiles.size()) % emptyTiles.size();
            com.javarush.task.task35.task3513.Tile emptyTile = emptyTiles.get(index);
            emptyTile.value = Math.random() < 0.9 ? 2 : 4;
        }
    }
    public void autoMove(){
        PriorityQueue<com.javarush.task.task35.task3513.MoveEfficiency> moveEfficiencies = new PriorityQueue<>(4, Collections.reverseOrder());

        moveEfficiencies.offer(getMoveEfficiency(this::left));
        moveEfficiencies.offer(getMoveEfficiency(this::up));
        moveEfficiencies.offer(getMoveEfficiency(this::right));
        moveEfficiencies.offer(getMoveEfficiency(this::down));

        moveEfficiencies.peek().getMove().move();
    }
    public boolean canMove(){
        boolean isMoveable=false;
        for (int y = 0; y < FIELD_WIDTH; y++) {
            for (int x = 0; x < FIELD_WIDTH; x++) {
                if(gameTiles[y][x].value==0){return true;}
                if((x+1!=FIELD_WIDTH)&&(gameTiles[y][x].value==gameTiles[y][x+1].value)){return true;}
                if((x-1>=0)&&(gameTiles[y][x].value==gameTiles[y][x-1].value)){return true;}
                if((y+1!=FIELD_WIDTH)&&(gameTiles[y][x].value==gameTiles[y+1][x].value)){return true;}
                if((y-1>=0)&&(gameTiles[y][x].value==gameTiles[y-1][x].value)){return true;}
            }
        }
        return false;
    }
    private boolean compressTiles(com.javarush.task.task35.task3513.Tile[] tiles) {
        boolean wasCompressed=false;
        int numberToChange = 0;
        for (int x = 0; x < FIELD_WIDTH; x++) {
            if(tiles[x].value==0){
                for (int i = x; i < FIELD_WIDTH; i++) {
                    if(tiles[i].value>0){
                        numberToChange=tiles[i].value;
                        tiles[i].value=0;
                        tiles[x].value=numberToChange;
                        wasCompressed=true;
                        break;
                    }
                }
            }
        }
        return wasCompressed;
    }
    private List<com.javarush.task.task35.task3513.Tile> getEmptyTiles() {
        List<com.javarush.task.task35.task3513.Tile> tiles = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty()) tiles.add(gameTiles[i][j]);
            }
        }
        return tiles;
    }
    public com.javarush.task.task35.task3513.Tile[][] getGameTiles() {
        return gameTiles;
    }
    public void right() {
        saveState(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        left();
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
    }
    public void down() {
        saveState(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        left();
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
    }
    public boolean hasBoardChanged(){
        com.javarush.task.task35.task3513.Tile[][] lastBoard = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (lastBoard[i][j].value != gameTiles[i][j].value) {
                    return true;
                }
            }
        }
        return false;
    }
    public void left() {
        // один раз сохранять текущее игровое состояние и счет в соответствующие стеки.
        if(isSaveNeeded){saveState(gameTiles);}
        boolean isNewNumberNeeded = false;
        for (com.javarush.task.task35.task3513.Tile[] row : gameTiles) {
            boolean wasCompressed = compressTiles(row);
            boolean wasMerged = mergeTiles(row);
            if (wasMerged) { compressTiles(row); }
            if (wasCompressed || wasMerged) { isNewNumberNeeded = true; }
        }
        if (isNewNumberNeeded) { addTile(); }
    }
    public void up() {
        saveState(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        left();
        gameTiles = rotateClockwise(gameTiles);
    }
    private boolean mergeTiles(com.javarush.task.task35.task3513.Tile[] tiles) {
        // соединяет соседние пары одинаковых ненулевых элементов массива row. Соединение должно происходить при сдвиге влево.
        boolean wasMerged=false;
        for (int x = 0; x < FIELD_WIDTH; x++) {
            if ((tiles[x].value > 0) & (x + 1 != FIELD_WIDTH)) {
                if (tiles[x + 1].value == tiles[x].value) {
                    tiles[x].value *= 2;
                    wasMerged=true;
                    score+=tiles[x].value;
                    if(tiles[x].value>maxTile)maxTile=tiles[x].value;
                    tiles[x + 1].value = 0;
                    compressTiles(tiles);
                }
            }
        }
        return wasMerged;
    }
    public com.javarush.task.task35.task3513.MoveEfficiency getMoveEfficiency(com.javarush.task.task35.task3513.Move move){
        saveState(gameTiles);
        com.javarush.task.task35.task3513.MoveEfficiency moveEfficiency = new com.javarush.task.task35.task3513.MoveEfficiency(getEmptyTiles().size(), score, move);
        move.move();
        if(hasBoardChanged()) { rollback(); return moveEfficiency;}
        rollback();
        return new com.javarush.task.task35.task3513.MoveEfficiency(-1, 0, move);
    }
    public void randomMove(){
        int n = ((int) (Math.random() * 100)) % 4;
        if(n==0){up();}
        if(n==1){right();}
        if(n==2){down();}
        if(n==3){left();}
    }
    public void resetGameTiles() {
        gameTiles = new com.javarush.task.task35.task3513.Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new com.javarush.task.task35.task3513.Tile();
            }
        }
        addTile();
        addTile();
    }
    public void rollback (){
        // Каждый вызов метода rollback должен уменьшать количество элементов в стеках на единицу, до тех пор пока это возможно.
        if(previousStates.isEmpty()||previousScores.isEmpty()){return;}
        gameTiles = previousStates.pop();
        score = previousScores.pop();
    }
    private com.javarush.task.task35.task3513.Tile[][] rotateClockwise(com.javarush.task.task35.task3513.Tile[][] tiles) {
        final int N = tiles.length;
        com.javarush.task.task35.task3513.Tile[][] result = new com.javarush.task.task35.task3513.Tile[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                result[c][N - 1 - r] = tiles[r][c];
            }
        }
        return result;
    }
    private void saveState (com.javarush.task.task35.task3513.Tile[][] tiles){
        // при сохранении массива gameTiles необходимо создать новый массив и заполнить его новыми объектами типа Tile перед сохранением в стек.
        // вес плиток в массиве который находится на вершине стека должны совпадать с весами плиток массива полученного в качестве параметра.
        // Каждый вызов метода saveState должен увеличивать количество элементов в стеках на единицу.
        com.javarush.task.task35.task3513.Tile[][] tempTiles = new com.javarush.task.task35.task3513.Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tempTiles[i][j] = new com.javarush.task.task35.task3513.Tile(tiles[i][j].value);
            }
        }
        previousStates.push(tempTiles);
        previousScores.push(score);
        isSaveNeeded=false;
    }
}
