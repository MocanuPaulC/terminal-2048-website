package classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class game2048 {
    Connection connection;
    private final Screens screen = new Screens();
    private final Board board = new Board();
    private Score score = new Score();
    private String name;

    public String getName() {
        return name;
    }

    public game2048(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }


    public Board getBoard() {
        return board;
    }

    //this function checks if the game is over
    public void checkEndGame() {
        score.setScore(board.getArray());
        Square[][] squares = board.getArray();
        for (int i = 0; i < board.getMAXLENGTH(); i++) {
            for (int j = 0; j < board.getMAXLENGTH(); j++) {

                Square square = squares[i][j];
                if (square.getValue() == 2048) {
                    endGame(this.connection, true);
                    return;
                }
                if (square.getValue() != 0) {

                    for (int y = i - 1; y <= i + 1; y++) {
                        for (int x = j - 1; x <= j + 1; x++) {
                            if (!((y < i && x < j) || (y < i && x > j) || (y == i && x == j) || (y > i && x < j) || (y > i && x > j))) {
                                if (!(y < 0 || y >= board.getMAXLENGTH() || x < 0 || x >= board.getMAXLENGTH())) {
                                    Square currentSquare = squares[y][x];
                                    if (square.getValue() == currentSquare.getValue()) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return;
                }
            }

        }
        endGame(this.connection, false);
    }

    //moving squares up
    public void moveSquaresup() {
        Square[][] squares = board.getArray();
        for (int i = 1; i < board.getMAXLENGTH(); i++) {
            for (int j = 0; j < board.getMAXLENGTH(); j++) {
                if (squares[i][j].getValue() != 0) {
                    for (int k = i; k > 0; k--) {
                        if (squares[k][j].getValue() == squares[k - 1][j].getValue()) {
                            squares[k - 1][j].setValue(squares[k][j].getValue() * 2);
                            squares[k][j].setValue(0);

                        }
                        if (squares[k - 1][j].getValue() == 0) {
                            squares[k - 1][j].setValue(squares[k][j].getValue());
                            squares[k][j].setValue(0);
                        }
                    }

                }
            }
        }
        screen.display_game_board(board.getArray(), true);
        checkEndGame();
    }


    //move squares down
    public void moveSquaresdown() {
        Square[][] squares = board.getArray();
        for (int i = 2; i >= 0; i--) {
            for (int j = 0; j < board.getMAXLENGTH(); j++) {
                if (squares[i][j].getValue() != 0) {
                    for (int k = i; k < board.getMAXLENGTH() - 1; k++) {
                        if (squares[k][j].getValue() == squares[k + 1][j].getValue()) {
                            squares[k + 1][j].setValue(squares[k][j].getValue() * 2);
                            squares[k][j].setValue(0);

                        }
                        if (squares[k + 1][j].getValue() == 0) {
                            squares[k + 1][j].setValue(squares[k][j].getValue());
                            squares[k][j].setValue(0);
                        }
                    }

                }
            }
        }
        screen.display_game_board(board.getArray(), true);
        checkEndGame();
    }

    // move squares left
    public void moveSquaresleft() {
        Square[][] squares = board.getArray();
        for (int j = 1; j < board.getMAXLENGTH(); j++) {
            for (int i = 0; i < board.getMAXLENGTH(); i++) {
                if (squares[i][j].getValue() != 0) {
                    for (int k = j; k > 0; k--) {
                        if (squares[i][k].getValue() == squares[i][k - 1].getValue()) {
                            squares[i][k - 1].setValue(squares[i][k - 1].getValue() * 2);
                            squares[i][k].setValue(0);

                        }
                        if (squares[i][k - 1].getValue() == 0) {
                            squares[i][k - 1].setValue(squares[i][k].getValue());
                            squares[i][k].setValue(0);
                        }
                    }
                }
            }
        }
        screen.display_game_board(board.getArray(), true);
        checkEndGame();
    }

    // move squares right
    public void moveSquaresRight() {
        Square[][] squares = board.getArray();
        for (int j = 2; j >= 0; j--) {
            for (int i = 0; i < board.getMAXLENGTH(); i++) {
                if (squares[i][j].getValue() != 0) {
                    for (int k = j; k < board.getMAXLENGTH() - 1; k++) {
                        if (squares[i][k].getValue() == squares[i][k + 1].getValue()) {
                            squares[i][k + 1].setValue(squares[i][k + 1].getValue() * 2);
                            squares[i][k].setValue(0);

                        }
                        if (squares[i][k + 1].getValue() == 0) {
                            squares[i][k + 1].setValue(squares[i][k].getValue());
                            squares[i][k].setValue(0);
                        }
                    }
                }
            }
        }
        screen.display_game_board(board.getArray(), true);
        checkEndGame();
    }

    public void endGame(Connection connection, boolean won) {
        DB_manipulator.insertValues(connection, score.getScore(), getName());
        ResultSet resultSet = DB_manipulator.getScoresTable(connection);
        String highscorePlayerDate = "";
        int highscorePlayer = 0;
        String highscoreTableName = "";
        String highscoreTableDate = "";
        int highscoreTable = 0;
        try {
            while (resultSet.next()) {
                highscoreTableName = resultSet.getString(1);
                highscoreTableDate = resultSet.getString(2);
                highscoreTable = resultSet.getInt(3);
                break;
            }
            resultSet = DB_manipulator.getScoresName(connection, getName());
            while (resultSet.next()) {
                highscorePlayerDate = resultSet.getString(2);
                highscorePlayer = resultSet.getInt(3);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        screen.display_end_game(score.getScore(), highscoreTable, highscorePlayer, getName(), highscoreTableName, highscoreTableDate, highscorePlayerDate, won
        );
        board.setBoardState(false);
    }

    public void checkSaveGame(Connection connection, String name, String saveORload) {
        if (saveORload.equals("save")) {
            if (DB_manipulator.checkSaveGameExists(connection, name)) {
                DB_manipulator.replaceSaveGame(connection, score.getScore(), name, board.getArray());
            } else {
                DB_manipulator.newSaveGame(connection, score.getScore(), name, board.getArray());
            }
        } else {
            if (DB_manipulator.checkSaveGameExists(connection, name)) {
                ResultSet resultSet = DB_manipulator.loadGame(connection, name);
                screen.display_game_board(board.initialiseSquaresFromLoad(resultSet), false);
                board.setBoardState(true);

            } else {
                System.out.println("No save file for that player, sorry!");
            }
        }
    }
}
