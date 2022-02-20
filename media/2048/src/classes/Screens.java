package classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Screens {

    public Screens() {
    }

    //This method displays the game board and sets its state to true (meaning the game has started)
    public void display_game_board(Square[][] square, boolean setRandom) {
        Board board = new Board(true);
        board.setBoardState(true);

        //This if checks if the Square array provided should receive new random values for the empty squares
        if(setRandom) {
            board.setRandomValues(square);
        }
        System.out.println("Type restart to restart the game");
        System.out.println("Type save game to save the current state of the game");

        for (int i = 0; i < square.length; i++) {
            if (i == 0) {
                //TOP BAR FOR LAYOUT
                System.out.print("+-----------------------+\n" +
                        "|\t  |\t    |\t  |\t    |\n");
            }
            System.out.print("|");
            for (int j = 0; j < square.length; j++) {
                //THE NUMBER GRID
                if (j < 3) {
                    System.out.printf(" %-4d|", square[i][j].getValue());
                } else {
                    System.out.printf("\t%-4d", square[i][j].getValue());
                }
                //LAYOUT
                if (j == 3) {
                    System.out.print("|\n");
                    if (i != 3) {

                        System.out.print("|\t  |\t    |\t  |\t    |\n");
                        System.out.print("+-----------------------+\n");
                        System.out.print("|\t  |\t    |\t  |\t    |");

                    }
                }
            }
            if (i == 3) {
                { //BAR ON THE BOTTOM FOR LAYOUT
                    System.out.print(
                            "|\t  |\t    |\t  |\t    |\n" +
                                    "+-----------------------+\n");
                }
            }
            System.out.println();

        }
        System.out.println("""
                Type up to move the boxes upward
                Type down to move the boxes down
                Type left to move all boxes left
                Type right to move all boxes right""");
    }

    public void display_title() {
        System.out.println
                ("""
                        +-------------------------------------------+
                        |                                           |
                        |                    2048                   |
                        |                  THE GAME                 |
                        |                                           |
                        |                  GROUP 13                 |
                        |                                           |
                        |                                           |
                        |                                           |
                        |           START GAME  LEADERBOARD         |
                        |                  LOAD GAME                |
                        |                                           |
                        |                                           |
                        |      TYPE START GAME TO START PLAYING     |
                        |     TYPE LOAD GAME TO GO TO LOAD A GAME   |
                        |         TYPE RULES TO GO TO RULES         |
                        |  TYPE INSTRUCTIONS TO GO TO INSTRUCTIONS  |
                        |   TYPE LEADERBOARD TO GO TO LEADERBOARD   |
                        |                                           |
                        |                                           |
                        +-------------------------------------------+""");

    }

    public void leaderboard(Connection connection, ResultSet resultSet) {
        int score = 0;
        String name = "";
        String date = "";
        int cnt = 1;
        StringBuilder row = new StringBuilder();
        try {

            System.out.print
                    ("""
                            +-----------------------------------+
                            |    LEADERBOARD \t                |
                            | \t                                |
                            """);
            while (resultSet.next()) {
//row.setLength resets the row to an empty string so that we can reuse it
                row.setLength(0);
                name = resultSet.getString(1);
                date = resultSet.getString(2);
                score = resultSet.getInt(3);
                row.append(cnt).append(". ").append(name).append(" ").append(score).append(" ").append(date);
                System.out.printf("| %-34s|\n", row);
                cnt++;
            }
            System.out.print("""
                    |                                   |
                    |                                   |
                    |      TYPE HOME TO GO HOME         |
                    +-----------------------------------+
                    """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void rules() {
        System.out.println
                ("""
                        +----------------------------------------------------------------------------------------+
                        |                      ### The goal of the game is to reach 2048 ###                     |
                        |                                                                                        |
                        |  ### the boxes with the same value will add eachother when you swipe them together ### |
                        |  ### two boxes with different values will not be added ###                             |
                        |  ### if there are no spaces left you lose ###                                          |
                        |                                                                                        |
                        |                                                                                        |
                        |                               TYPE HOME TO GO TO HOME                                  |
                        +--------------------------------------------------------------------------------------- +
                        """);


    }

    public void instructions() {
        System.out.println
                ("""
                        +------------------------------------------------------------------------------------------+
                        |                                           COMMANDS                                       |
                        |                      ### Use arrow up to move the boxes upward ###                       |
                        |                      ### Use arrow down to move the boxes down ###                       |
                        |                    ### Use the arrow left to move all boxes left ###                     |
                        |                   ### Use the arrow right to move all boxes right ###                    |
                        |            ### Type the corresponding word in order to utilise the button ###            |
                        |                                                                                          |
                        |                                                                                          |
                        |                                    TYPE HOME TO GO TO HOME                               |
                        +------------------------------------------------------------------------------------------+
                        """);


    }

    public void display_end_game(int score, int highscoreTable, int highscorePlayer, String player, String highscoreTableName, String dateTable, String datePlayer, boolean wonGame) {
        System.out.println("""
                +-----------------------------------------+
                |                           	 	      |""");
        if(!wonGame){
            System.out.println("|                Game Over                |");
        }
        else {
            System.out.println("|                You Won!                 |");
        }
        System.out.println("""
                |                           		      |
                |                 		 	              |
                |                   GG      		      |
                |               		                  |
                |         Player name     Score           |""");
        System.out.printf("|%20s     %5d           |\n", player, score);
        System.out.println("""
                |                           		      |
                |                Highscore:               |
                |   Player name Score      Date           |""");
        System.out.printf("|%14s %5d  %s|\n",highscoreTableName,highscoreTable,dateTable);
        System.out.println("""
                |                                         |
                |            Personal Highscore:          |
                |   Player name Score     Date            |""");

        System.out.printf("|%14s %5d  %s|\n", player, highscorePlayer, datePlayer);
        System.out.println("""
                |                           		      |
                |                           		      |
                |           Hard           Home           |
                |                           		      |
                |                           		      |
                +-----------------------------------------+
                Type hard to play again
                Type home to go home""");
    }

}

