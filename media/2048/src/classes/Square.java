package classes;

import java.util.Scanner;

public class Square {
    private int x;
    private int y;
    private int value;
    private final int MAXLENGTH = 4;
    //-------------------------------------------------------GETTER

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }
    //-----------------------------------------------------------setter

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setValue(int value) {
        this.value = value;
    }

    //--------------------------------------Constructor
    public Square(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    public Square() {
    }


    //---------------------------------------------------


    @Override
    public String toString() {
        return "Square{" +
                "x=" + x +
                ", y=" + y +
                ", value=" + value +
                '}';
    }
}
