/*
Этот класс описывает плитку
В игре 2048 поле состоит из 16 плиток,
каждая плитка имеет определенный вес.
Кроме веса у плитки еще будет собственный цвет и цвет текста (которым будет отображаться вес плитки)
Цвета плиток находятся в диапазоне от светло-серого до красного,
а цвет текста будет зависеть от цвета плитки.
*/

import java.awt.*;

public class Tile {
    // поля
    int value;

    // конструкторы
    public Tile() {
        this.value = 0;
    }
    public Tile(int value) {
        this.value = value;
    }

    // методы
    public boolean isEmpty() {
        if (value == 0) return true;
        return false;
    }
    public Color getFontColor() {
        if (value < 16) return new Color(0x776e65);
        return new Color(0xf9f6f2);
    }
    public Color getTileColor(){
        switch (value){
            case 0:return new Color(0xcdc1b4);
            case 2:return new Color(0xeee4da);
            case 4:return new Color(0xede0c8);
            case 8:return new Color(0xf2b179);
            case 16:return new Color(0xf59563);
            case 32:return new Color(0xf67c5f);
            case 64:return new Color(0xf65e3b);
            case 128:return new Color(0xedcf72);
            case 256:return new Color(0xedcc61);
            case 512:return new Color(0xedc850);
            case 1024:return new Color(0xedc53f);
            case 2048:return new Color(0xedc22e);
            default:return new Color(0xff0000);
        }
    }
}
