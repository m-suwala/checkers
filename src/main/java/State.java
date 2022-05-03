import java.util.ArrayList;

public class State {
    Board board;
    int value;
    ArrayList<String> moves;
    ArrayList<State> children;

    public State(Board board){
        this.board = board;
        this.moves = board.listOfMoves();
        this.value = board.boardValue();

        for(String val : moves){
            Board newBoard = new Board(board);
            for(int i = 0; i<val.length()-4; i+=2){
                newBoard.move(val.substring(i,i+1), val.substring(i+2,i+3));
            }
            children.add(new State(newBoard));

        }
    }
}
