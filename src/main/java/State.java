import java.util.ArrayList;

public class State {
    Board board;
    int value;
    String move;
    ArrayList<String> moves;
    ArrayList<State> children;

    public State(Board board, String move){
        this.board = board;
        this.move = move;
        this.moves = this.board.listOfMoves();
        this.value = this.board.boardValue();
        children = new ArrayList<>();
    }

    public void update(){
        this.moves = this.board.listOfMoves();
        this.value = this.board.boardValue();

    }

    public void generateChildren(){
        children.clear();
        for(String val : moves){
            Board newBoard = new Board(board);
            for(int i = 0; i<=val.length()-4; i+=2){
                newBoard.move(val.substring(i,i+2), val.substring(i+2,i+4), true);
            }
            children.add(new State(newBoard, val));
        }
    }
}
