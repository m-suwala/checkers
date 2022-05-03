import java.util.Scanner;

public class Checkers {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        board.print();
        while(!board.endGame()){
            System.out.println(board.getCurrPlayer() + " move from: ");
            String from = scanner.next();

            System.out.println(board.getCurrPlayer() + " move to: ");
            String to = scanner.next();

            board.move(from, to);
            //board.moveWherever(from, to);
            board.print();

        }
        System.out.println("GAME OVER");
        System.out.println("The winner is " + board.getWinner());
    }
}
