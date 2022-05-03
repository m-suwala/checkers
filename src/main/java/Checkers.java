import java.util.Scanner;

public class Checkers {

    public static void main(String[] args) {

        State state = new State(new Board());


    }

    public static int minimax(State state, int depth, boolean maximizingPlayer){
        if(depth == 0 || state.board.endGame()){
            return state.value;
        }
        if(maximizingPlayer){
            int maxEval = Integer.MIN_VALUE;
            for(State child : state.children){
                int eval = minimax(child, depth-1, false);
                maxEval = Integer.max(maxEval, eval);
            }
            return maxEval;
        }
        else{
            int minEval = Integer.MAX_VALUE;
            for(State child : state.children){
                int eval = minimax(child, depth-1, true);
                minEval = Integer.min(minEval, eval);
            }
            return minEval;
        }
    }


    public static void testCheckers(){
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        board.print();
        while(!board.endGame()){
            System.out.println(board.getCurrPlayer() + " move from: ");
            String from = scanner.next();

            System.out.println(board.getCurrPlayer() + " move to: ");
            String to = scanner.next();

            board.move(from, to);
            board.print();

        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + board.getWinner());
    }
}
