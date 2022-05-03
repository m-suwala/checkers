import org.javatuples.Pair;

public class Minmax {

    public static Pair<Integer, String> minmax(State state, int depth, boolean maximizingPlayer){
        if(depth == 0 || state.board.endGame()){
            return Pair.with(state.value, state.move);
        }
        if(maximizingPlayer){
            int maxEval = Integer.MIN_VALUE;
            String bestMove = "";
            state.generateChildren();
            for(State child : state.children){
                Pair<Integer, String> pair = minmax(child, depth-1, false);
                int eval = pair.getValue0();
                if(eval > maxEval){
                    maxEval = eval;
                    bestMove = child.move;
                }
            }
            return Pair.with(maxEval, bestMove);
        }
        else{
            int minEval = Integer.MAX_VALUE;
            String bestMove = "";
            state.generateChildren();
            for(State child : state.children){
                Pair<Integer, String> pair = minmax(child, depth-1, true);
                int eval = pair.getValue0();
                if(eval < minEval){
                    minEval = eval;
                    bestMove = child.move;
                }
            }
            return Pair.with(minEval, bestMove);
        }
    }
}
