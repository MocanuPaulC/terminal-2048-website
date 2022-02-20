package classes;

public class Score {
    private int score;



    public Score() {
    }

    //THIS METHOD CALCULATES THE TOTAL SCORE BY ADDING UP ALL THE VALUES IN EACH BOX
    public void setScore(Square[][] squares) {
        this.score=0;
        final int MAXLENGTH = 4;
//THE LOOP ITERATES THROUGH ALL THE SQUARES
        for (int i = 0; i < MAXLENGTH; i++) {
            for (int j = 0; j < MAXLENGTH; j++) {
                this.score += squares[i][j].getValue();
            }
        }
        System.out.println("Score: " +this.score);
    }

    public int getScore() {
       // setScore();
        return score;
    }
}
