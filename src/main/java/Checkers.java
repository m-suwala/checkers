import org.javatuples.Pair;

import java.util.Scanner;

import static java.lang.System.currentTimeMillis;

public class Checkers {

    public static void main(String[] args) {

        //twoPlayerGame();

        //PlayerAndAIGameWithMinMax(true);

        //PlayerAndAIGameWithAlphaBeta(true);

        //twoAIsGameWithMinMax();

        twoAIsGameWithAlphaBeta();

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

    public static void twoAIsGameWithMinMax(){
        long processingTimeWhite = 0;
        long processingTimeBlack = 0;
        long start;
        State state = new State(new Board(), "");
        state.board.print();
        boolean whiteTurn = true;
        Pair<Integer, String> pair;

        while(!state.board.endGame()){
            start = currentTimeMillis();
            pair = Minmax.minmax(state, 3, whiteTurn);
            for(int i = 0; i<=pair.getValue1().length()-4; i+=2){
                state.board.move(pair.getValue1().substring(i,i+2), pair.getValue1().substring(i+2,i+4), false);
            }
            if(whiteTurn) processingTimeWhite += currentTimeMillis() - start;
            else processingTimeBlack += currentTimeMillis() - start;
            state.update();
            whiteTurn = !whiteTurn;

            state.board.print();
        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + state.board.getWinner());
        System.out.println("Winner moved " + state.board.getWinnerMoveCount() + " times");
        if(state.board.getWinner() == state.board.blackPawn){
            System.out.println("Average winner's processing time: " + processingTimeBlack / state.board.getWinnerMoveCount() + " ms");
        } else {
            System.out.println("Average winner's processing time: " + processingTimeWhite / state.board.getWinnerMoveCount() + " ms");
        }
    }

    public static void twoAIsGameWithAlphaBeta(){
        long processingTimeWhite = 0;
        long processingTimeBlack = 0;
        long start;
        State state = new State(new Board(), "");
        state.board.print();
        boolean whiteTurn = true;
        Pair<Integer, String> pair;

        while(!state.board.endGame()){
            start = currentTimeMillis();
            pair = AlphaBeta.alphaBeta(state, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, whiteTurn);
            for(int i = 0; i<=pair.getValue1().length()-4; i+=2){
                state.board.move(pair.getValue1().substring(i,i+2), pair.getValue1().substring(i+2,i+4), false);
            }
            if(whiteTurn) processingTimeWhite += currentTimeMillis() - start;
            else processingTimeBlack += currentTimeMillis() - start;
            state.update();
            whiteTurn = !whiteTurn;

            state.board.print();
        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + state.board.getWinner());
        System.out.println("Winner moved " + state.board.getWinnerMoveCount() + " times");
        if(state.board.getWinner() == state.board.blackPawn){
            System.out.println("Average winner's processing time: " + processingTimeBlack / state.board.getWinnerMoveCount() + " ms");
        }
        else{
            System.out.println("Average winner's processing time: " + processingTimeWhite / state.board.getWinnerMoveCount() + " ms");
        }

    }

    public static void PlayerAndAIGameWithMinMax(boolean playerIsWhite){
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

                if(state.board.move(from, to, false)) playerTurn = !playerTurn;
            }
            else{
                pair = Minmax.minmax(state, 3, false);
                for(int i = 0; i<=pair.getValue1().length()-4; i+=2){
                    state.board.move(pair.getValue1().substring(i,i+2), pair.getValue1().substring(i+2,i+4), false);
                }
                playerTurn = !playerTurn;
            }
            state.update();
            state.board.print();
        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + state.board.getWinner());

    }

    public static void PlayerAndAIGameWithAlphaBeta(boolean playerIsWhite){
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

                if(state.board.move(from, to, false)) playerTurn = !playerTurn;
            }
            else{
                pair = AlphaBeta.alphaBeta(state, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                for(int i = 0; i<=pair.getValue1().length()-4; i+=2){
                    state.board.move(pair.getValue1().substring(i,i+2), pair.getValue1().substring(i+2,i+4), false);
                }
                playerTurn = !playerTurn;
            }
            state.update();
            state.board.print();
        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + state.board.getWinner());

    }
}
