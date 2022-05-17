import org.javatuples.Pair;

import java.util.Random;

public class AlphaBeta {

    public static Pair<Integer, String> alphaBeta(State state, int depth, int alpha, int beta, boolean maximizingPlayer){
        Random rand = new Random();
        if(depth == 0 || state.board.endGame()){
            return Pair.with(state.value, state.move);
        }
        if(maximizingPlayer){
            int maxEval = Integer.MIN_VALUE;
            String bestMove = "";
            state.generateChildren();
            for(State child : state.children){
                Pair<Integer, String> pair = alphaBeta(child, depth-1, alpha, beta, false);
                int eval = pair.getValue0();
                if(eval > maxEval || (eval == maxEval && rand.nextDouble() < 1.0/state.board.listOfMoves().size())){
                    maxEval = eval;
                    bestMove = child.move;
                }
                alpha = Integer.max(alpha, eval);
                if(beta<=alpha) break;
            }
            return Pair.with(maxEval, bestMove);
        }
        else{
            int minEval = Integer.MAX_VALUE;
            String bestMove = "";
            state.generateChildren();
            for(State child : state.children){
                Pair<Integer, String> pair = alphaBeta(child, depth-1, alpha, beta,true);
                int eval = pair.getValue0();
                if(eval < minEval || (eval == minEval && rand.nextDouble() < 1.0/state.board.listOfMoves().size())){
                    minEval = eval;
                    bestMove = child.move;
                }
                beta = Integer.min(beta, eval);
                if(beta <= alpha) break;
            }
            return Pair.with(minEval, bestMove);
        }
    }

}
