package classes;

import java.sql.*;

public class DB_manipulator {
    //This method initializes the database for the leaderboard and the save&load function on the local machine
    //The password needs to be changed to the password used for pgAdmin on the local machine
    //This method also returns a Connection object which we will use until the game ends
    public static Connection initializeDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/DB2048", "postgres", "coltulderai.8");
            Statement statement = connection.createStatement();
            //This statement.execute has the sql script to create all the tables with the constraints and the sequences required
            //We use timestamp to get the date and exact moment of the game's ending
            //We use numeric(3) for playerid and boardid because there will be no more than 999 players on a local machine
            //and we use numeric(2) for the squareid since we only have ids from 1 to 16
            //We use sequences so that there's no need to hard code values
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS INT_leaderboard
                    (
                        name VARCHAR(20)
                            constraint ch_name_length check ( LENGTH(name) <= 20 ),
                        date_of_play TIMESTAMP,
                        score NUMERIC(5)
                        constraint ch_score check ( score >= 0 )
                    );
                                        
                    CREATE TABLE IF NOT EXISTS INT_players
                    (
                        playerid   numeric(3)
                            constraint pk_player PRIMARY KEY,
                        name VARCHAR(20)
                            constraint ch_name_length check ( LENGTH(name) <= 20 )

                    );


                    CREATE TABLE IF NOT EXISTS INT_Boards
                    (
                        playerid    numeric(3)
                            constraint fk_board_player references INT_players (playerid),
                        boardid numeric(3)
                            constraint pk_boards PRIMARY KEY,
                        score numeric(4)
                            constraint ch_score check ( score >= 0 )
                    );


                    CREATE TABLE IF NOT EXISTS INT_squares
                    (
                        squareid numeric(2),
                        boardid numeric(3)
                            constraint fk_boards references INT_boards(boardid),
                        value numeric(4),
                        constraint pk_squares PRIMARY KEY(squareid, boardid)
                    );

                    CREATE SEQUENCE IF NOT EXISTS seq_player_id START WITH 1 INCREMENT BY 1;
                    CREATE SEQUENCE IF NOT EXISTS seq_board_id START WITH 1 INCREMENT BY 1;

                    """);
            statement.close();
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //This method checkes to see if a saved game exists for a certain player.
    //It is used both when loading and saving a game
    //It needs a connection object to connect to the database and the name of the player that will be checked
       public static boolean checkSaveGameExists(Connection connection, String name) {
        try {
            Statement statement = connection.createStatement();
            //The resultSet object will either be empty or contain exactly one row

            ResultSet resultSet = statement.executeQuery("SELECT playerid FROM int_players WHERE name ='" + name + "'");
            //It enters the while loop if it has a value which for us means that a saved game exists for that player
            //and as such we return true and the method stops
            while (resultSet.next()) {


                statement.close();
                resultSet.close();
                return true;
            }
            //It doesn't enter the while loop if it's empty, meaning that there is no saved game and returning a false boolean.
            resultSet.close();
            statement.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //This method creates a new save game instance in the database for new players.
    //It asks for a connection object to connect to the database
    //A score for the whole board
    //A name for the player
    //And the array of squares on which the game was played
    public static void newSaveGame(Connection connection, int score, String name, Square[][] squares) {
        //This int is used in the for loop to insert the values accordingly
        int squareid = 1;

        try {
            Statement statement = connection.createStatement();
            //These two lines insert the respective values into the correct tables and they use sequences
            statement.executeUpdate("INSERT INTO  int_players values (nextval('seq_player_id'),'" + name + "')");
            statement.executeUpdate("INSERT INTO int_boards values (currval('seq_player_id'),nextval('seq_board_id')," + score + ")");
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    statement.executeUpdate("INSERT INTO int_squares values (" + squareid++ + ",currval('seq_board_id')," + squares[i][j].getValue() + ")");
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //This method replaces a save game instance in the database for recurring players.
    //It asks for a connection object to connect to the database
    //A score for the whole board
    //A name for the player
    //And the array of squares on which the game was played
    public static void replaceSaveGame(Connection connection, int score, String name, Square[][] squares) {
        //This int is used in the for loop to insert the values accordingly
        int squareid = 1;
        //The int playerid is used to update the correct rows in the database
        int playerid = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT playerid FROM int_players WHERE name ='" + name + "'");
            while (resultSet.next()) {
                playerid = resultSet.getInt(1);
            }
            statement.executeUpdate("UPDATE int_boards SET score=" + score + " WHERE boardid=" + playerid + "");
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    statement.executeUpdate("UPDATE int_squares SET value =" + squares[i][j].getValue() + " WHERE boardid =" + playerid + " AND squareid=" + squareid++ + "");
                }
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //This method loads a game from the database based on a given name
    public static ResultSet loadGame(Connection connection, String name) {
        try {
            Statement statement = connection.createStatement();
            //Two players with the same name will never appear in the database because when saving it checks
            //if a saved game exists with that name, thus only unique names appear for the save&load functionality
            ResultSet resultSet = statement.executeQuery("SELECT value FROM int_squares sq" +
                    " JOIN int_boards ib on sq.boardid = ib.boardid " +
                    " JOIN int_players ip on ip.playerid = ib.playerid" +
                    " WHERE ip.name='" + name + "'");
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //This method insets the values into the leaderboard table
    public static void insertValues(Connection connection, int score, String name) {

        try {
            Statement statement = connection.createStatement();
            //The statement is simple, we need the name, current timestamp and the score at the end of the game
            statement.executeUpdate(" INSERT INTO INT_leaderboard VALUES ('" + name + "',current_timestamp," + score + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //This method retrieves the highest score on the leaderboard for all players
    public static ResultSet getScoresTable(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name, to_char(date_of_play,'YYYY-MM-DD HH:MI:SS'),score FROM int_leaderboard ORDER BY score DESC FETCH FIRST 5 ROWS ONLY ");
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //This method retrieves the highest score on the leaderboard for one player
    public static ResultSet getScoresName(Connection connection, String name) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name, to_char(date_of_play,'YYYY-MM-DD HH:MI:SS'),score FROM int_leaderboard WHERE name='" + name + "' ORDER BY score DESC FETCH FIRST 5 ROWS ONLY ");
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
