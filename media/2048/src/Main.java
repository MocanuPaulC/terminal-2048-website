import classes.*;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String name;
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Input name:");
        name =scanner.nextLine();
        Player player = new Player(name);


        Connection connection = DB_manipulator.initializeDatabase();

        game2048 game2048 = new game2048(player.getName(),connection);

        Screens screen = new Screens();
        game2048.getBoard().setBoardState(false);

        screen.display_title();
        while (true) {
            System.out.println("Write button desired");
            String button = scanner.nextLine();
            switch (button.toUpperCase()) {
                case "UP":
                    if(game2048.getBoard().isBoardState()){
                        game2048.moveSquaresup();
                    }
                    break;
                case "DOWN":
                    if(game2048.getBoard().isBoardState()){
                        game2048.moveSquaresdown();
                    }
                    break;
                case "LEFT":
                    if(game2048.getBoard().isBoardState()){
                        game2048.moveSquaresleft();
                    }
                    break;
                case "RIGHT":
                    if(game2048.getBoard().isBoardState()){
                        game2048.moveSquaresRight();
                    }
                    break;
                case "SAVE GAME":
                    if(game2048.getBoard().isBoardState()) {
                        game2048.checkSaveGame(connection, game2048.getName(),"save");
                        System.out.println("Game Saved Successfully! Type quit to quit!");
                        screen.display_game_board(game2048.getBoard().getArray(),false);
                    }
                    break;
                case "START GAME":
                    if(!game2048.getBoard().isBoardState()) {


                        game2048.getBoard().initialiseSquares();
                        game2048.getBoard().setBoardState(true);

                        screen.display_game_board(game2048.getBoard().getArray(), true);
                    }
                    break;
                case "RESTART":
                    if(game2048.getBoard().isBoardState()){
                        game2048.getBoard().setReset();
                        screen.display_game_board(game2048.getBoard().getArray(),true);
                    }
                    break;
                case "LOAD GAME":
                    if(!game2048.getBoard().isBoardState()){
                        System.out.println("Whose game do you want to load? Please type the according name:");
                        String nameLoadGame = scanner.nextLine();
                        game2048.checkSaveGame(connection,nameLoadGame,"load");
                    }
                    break;
                case "HOME":
                    if(!game2048.getBoard().isBoardState()){
                        screen.display_title();
                    }
                    break;
                case "LEADERBOARD":
                    if(!game2048.getBoard().isBoardState()){
                        System.out.println("Enter a name to search for scores(type 'a' to see all scores)");
                        String input =  scanner.nextLine();
                        if(input.equals("a")) {
                            screen.leaderboard(connection,DB_manipulator.getScoresTable(connection));
                        }
                        else {
                            screen.leaderboard(connection,DB_manipulator.getScoresName(connection,input));
                        }
                    }
                    break;
                case "INSTRUCTIONS":
                    if(!game2048.getBoard().isBoardState()){
                        screen.instructions();
                    }
                    break;
                case "RULES":
                    if(!game2048.getBoard().isBoardState()){
                        screen.rules();
                    }
                    break;
                case "QUIT":
                    if(game2048.getBoard().isBoardState()) {
                        game2048.getBoard().setBoardState(false);
                        screen.display_title();
                    }
                    else{
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid input, try again!");
                    break;
            }
        }
    }
}