package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class Board {
    private boolean boardState;
    private final int MAXLENGTH = 4;
    private Square[][] values = new Square[MAXLENGTH][MAXLENGTH];
    private final Random random = new Random();
    private int count=0;

    public int getMAXLENGTH() {
        return MAXLENGTH;
    }

    public boolean isBoardState() {
        return boardState;
    }

    public void setBoardState(boolean boardState) {
        this.boardState = boardState;
    }

    public Board(boolean boardState) {
        setBoardState(boardState);
    }

    public Board() {

    }

    public void initialiseSquares() {
        for (int i = 0; i < MAXLENGTH; i++) {
            for (int j = 0; j < MAXLENGTH; j++) {
                //THIS IS TO RESET THE GAME AND PUT ALL VALUES ON 0

                values[i][j] = new Square(i, j, 0);

            }
        }
    }

    public Square[][] initialiseSquaresFromLoad(ResultSet resultSet){
        int i=3,j=3;
        try {
            while (resultSet.next()) {
                values[i][j]=new Square(i,j,resultSet.getInt(1));
                j--;
                if(j==-1){
                    j=3;
                    i--;
                }
            }
            return values;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int getCountSquares(){
        count=0;
        for (int i = 0; i < MAXLENGTH; i++) {
            for (int j = 0; j < MAXLENGTH; j++) {
                if(values[i][j].getValue()!=0)count++;

            }
        }
        return count;
    }

    public void setRandomValues(Square[][] square) {
        //THIS IS TO RESET THE GAME AND PUT ALL VALUES ON 0
        values = square;
        int cnt = getCountSquares();
        if (cnt <= 14) {
            for (int i = 0; i < 2; i++) {
                RandomiseSquares();
            }

        }
        else if(cnt == 15){
            RandomiseSquares();
        }
    }

    public void RandomiseSquares() {
        int origin = 0;
        int bound = 4;
        int x;
        int y;
        while (true) {
            x = random.nextInt(origin, bound);
            y = random.nextInt(origin, bound);
            if (values[x][y].getValue() == 0) {
                //sets value to random choice of 2 or 4
                if (random.nextInt(1, 3) == 1) {

                    values[x][y].setValue(2);

                } else {
                    values[x][y].setValue(4);

                }

                break;
            }
        }
    }

    public void setReset() {
        for (int i = 0; i < MAXLENGTH; i++) {
            for (int j = 0; j < MAXLENGTH; j++) {
                //THIS IS TO RESET THE GAME AND PUT ALL VALUES ON 0
                values[i][j].setValue(0);
            }
        }
    }


    public Square[][] getArray() {
        //THIS IS TO GET THE VALUES FROM ALL SQUARES TO CALCULATE THE SCORE

        return values;
    }


}
