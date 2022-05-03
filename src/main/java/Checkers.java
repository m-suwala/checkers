import org.javatuples.Pair;

import java.util.Scanner;

public class Checkers {

    public static void main(String[] args) {

        PlayerAndAIGame(true);

        //twoPlayerGame();

        //twoAIsGame();

    }

    public static void twoPlayerGame(){
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        board.print();
        while(!board.endGame()){
            System.out.println(board.getCurrPlayer() + " move from: ");
            String from = scanner.next();

            System.out.println(board.getCurrPlayer() + " move to: ");
            String to = scanner.next();

            board.move(from, to, false);
            board.print();

        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + board.getWinner());
    }

    public static void twoAIsGame(){
        State state = new State(new Board(), "");
        state.board.print();
        boolean whiteTurn = true;
        Pair<Integer, String> pair;

        while(!state.board.endGame()){
            pair = Minmax.minmax(state, 3, whiteTurn);
            for(int i = 0; i<=pair.getValue1().length()-4; i+=2){
                state.board.move(pair.getValue1().substring(i,i+2), pair.getValue1().substring(i+2,i+4), false);
            }
            state.update();
            whiteTurn = !whiteTurn;

            state.board.print();
        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + state.board.getWinner());
    }

    public static void PlayerAndAIGame(boolean playerIsWhite){
        Scanner scanner = new Scanner(System.in);
        State state = new State(new Board(), "");
        state.board.print();
        boolean playerTurn = playerIsWhite;
        Pair<Integer, String> pair;

        while(!state.board.endGame()){
            if(playerTurn){
                System.out.println(state.board.getCurrPlayer() + " move from: ");
                String from = scanner.next();

                System.out.println(state.board.getCurrPlayer() + " move to: ");
                String to = scanner.next();

                state.board.move(from, to, false);
            }
            else{
                pair = Minmax.minmax(state, 3, false);
                for(int i = 0; i<=pair.getValue1().length()-4; i+=2){
                    state.board.move(pair.getValue1().substring(i,i+2), pair.getValue1().substring(i+2,i+4), false);
                }
            }
            state.update();
            playerTurn = !playerTurn;
            state.board.print();
        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + state.board.getWinner());

    }
}
