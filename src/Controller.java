import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
Этот класс дл отслеживания за нажатием клавиш во время игры.
*/
public class Controller extends KeyAdapter {

    // поля
    private Model model;
    private View view;

    public View getView() {
        return view;
    }

    private static final int WINNING_TILE = 2048;

    // конструкторы
    public Controller(Model model) {
        this.model = model;
        view = new View(this);
    }

    // методы
    public Tile[][] getGameTiles() { return model.getGameTiles(); }
    public int getScore() { return model.score; }
    public void resetGame(){
       model.maxTile=0;
        model.score=0;
        view.isGameLost=false;
        view.isGameWon=false;
        model.resetGameTiles();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){resetGame();}
        if(e.getKeyCode()==KeyEvent.VK_Z){model.rollback();}
        if(e.getKeyCode()==KeyEvent.VK_R){model.randomMove();}
        if(e.getKeyCode()==KeyEvent.VK_A){model.autoMove();}
        if (model.canMove() == false) { view.isGameLost = true;}
        if(view.isGameLost == false & view.isGameWon == false){
            if(e.getKeyCode()==KeyEvent.VK_RIGHT){model.right(); checkWin();}
            if(e.getKeyCode()==KeyEvent.VK_DOWN){model.down(); checkWin();}
            if(e.getKeyCode()==KeyEvent.VK_LEFT){model.left(); checkWin();}
            if(e.getKeyCode()==KeyEvent.VK_UP){model.up(); checkWin();}
        }
        view.repaint();
    }
    private void checkWin(){
        if(model.maxTile==WINNING_TILE)view.isGameWon=true;
    }
}
