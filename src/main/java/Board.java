import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    public final char whitePawn = 'W';
    private final char whiteDame = 'M';
    public final char blackPawn = 'b';
    private final char blackDame = 'P';
    private final char empty = ' ';
    private final char whiteSpace = '~';

    private static final int lowestWeight = 1;
    private static final int middleWeight = 2;
    private static final int highestWeight = 3;
    private static final int pawnValue = 1;
    private static final int dameValue = 3;

    private char currPlayer;
    private final char[][] board;
    private int blackPawnsCount;
    private int whitePawnsCount;
    private int dameMoveCounter;
    private char winner;
    private int captureStart;

    private int whiteMoveCount;
    private int blackMoveCount;

    public char getWinner() {
        return winner;
    }

    public int getWinnerMoveCount(){
        if (winner == blackPawn)
            return blackMoveCount;
        else if (winner == whitePawn)
            return whiteMoveCount;

        return 0;
    }

    public Board(){
        board = new char[8][8];
        currPlayer = whitePawn;
        blackPawnsCount = whitePawnsCount = 8;
        dameMoveCounter = 0;
        captureStart = -1;
        whiteMoveCount = 0;
        blackMoveCount = 0;

        for(int i = 0; i<8; i++){
            for(int k = 0; k<8; k++){
                if((i+k)%2 == 0) board[i][k] = whiteSpace;
                else if(i<2) board[i][k] = blackPawn;
                else if(i>5) board[i][k] = whitePawn;
                else board[i][k] = empty;
            }
        }
    }

    public Board(Board boardToCopy){
        board = Arrays.copyOf(boardToCopy.board, boardToCopy.board.length);
        for(int i = 0; i<boardToCopy.board.length; i++){
            board[i] = Arrays.copyOf(boardToCopy.board[i], boardToCopy.board[i].length);
        }
        currPlayer = boardToCopy.currPlayer;
        blackPawnsCount = boardToCopy.blackPawnsCount;
        whitePawnsCount = boardToCopy.whitePawnsCount;
        dameMoveCounter = boardToCopy.dameMoveCounter;
        winner = boardToCopy.winner;
        captureStart = boardToCopy.captureStart;
        whiteMoveCount = boardToCopy.whiteMoveCount;
        blackMoveCount = boardToCopy.blackMoveCount;

    }

    public char getCurrPlayer(){
        return currPlayer;
    }

    public void print(){
        System.out.println("   a   b   c   d   e   f   g   h");
        System.out.println("  --- --- --- --- --- --- --- ---");
        for(int i = 0; i<8; i++){
            System.out.print( i+1 + "|");
            for(int k = 0; k<8; k++){
                //if(board[i][k] == whiteSpace) System.out.print(" " + " | ");
                System.out.print(" " + board[i][k] + " |");
            }

            System.out.println(i+1);
            System.out.println("  --- --- --- --- --- --- --- ---");
        }
        System.out.println("   a   b   c   d   e   f   g   h");

    }

    public boolean move(String from, String to, boolean silent){
        int i, k, l, m;

        if(from.length() == 2 && to.length() == 2){
            if((from.charAt(0) > 47 && from.charAt(0) < 58) || (to.charAt(0) > 47 && to.charAt(0) <58)){ // only numbers
                k = from.charAt(0) - 48;
                m = to.charAt(0) - 48;
                i = from.charAt(1) - 48;
                l = to.charAt(1) - 48;
            }
            else { //letters and numbers
                k = from.charAt(1) - 49;
                m = to.charAt(1) - 49;
                i = from.charAt(0) - 97;
                l = to.charAt(0) - 97;
            }


            //System.out.println("i = " + i + ", k = " + k + ", l = " + l + ", m = " + m);

            if(checkConditions(i,k,l,m, silent)){
                if(currPlayer == whitePawn && board[k][i] == whiteDame){
                    board[m][l] = whiteDame;
                    dameCapture(i, k, l, m);
                    dameMoveCounter++;
                }
                else if(currPlayer == blackPawn && board[k][i] == blackDame){
                    board[m][l] = blackDame;
                    dameCapture(i, k, l, m);
                    dameMoveCounter++;
                }
                else {
                    if(Math.abs(i-l ) == 2){
                        board[(k+m)/2][(i+l)/2] = empty;
                        if(currPlayer == whitePawn) blackPawnsCount--;
                        else whitePawnsCount--;
                    }
                    board[m][l] = currPlayer;
                    dameMoveCounter=0;
                }

                board[k][i] = ' ';


                if(!silent) System.out.println(currPlayer + " has moved from " + (char) (i + 97) + (char) (k + 49) + " to " + (char) (l + 97) + (char) (m + 49));


                if(captureStart == -1){
                    if(checkEndOfBoard(m)) switchToDame(l,m);
                    if(currPlayer == whitePawn) currPlayer = blackPawn;
                    else currPlayer = whitePawn;
                }

                if(currPlayer == blackPawn) blackMoveCount++;
                else whiteMoveCount++;

                return true;

            }
            else{
                if(!silent){
                    System.out.println("The move cannot be made");
                    System.out.println(currPlayer + " please make a move again");
                    return false;
                }
            }
        }

        else{
            if(!silent){
                System.out.println("Incorrect input");
                System.out.println(currPlayer + " please make a move again");
                return false;
            }
        }

        return false;
    }

    public boolean endGame(){
        if(blackPawnsCount == 0){
            winner = whitePawn;
            return true;
        } else if(whitePawnsCount == 0){
            winner = blackPawn;
            return true;
        } else if(dameMoveCounter == 15) {
            winner = '-';
            return true;
        } else if(listOfMoves().isEmpty()){
            if(currPlayer == blackPawn) winner = whitePawn;
            else winner = blackPawn;
            return true;
        }

        return false;
    }

    public ArrayList<String> listOfMoves(){

        ArrayList<String> movements = new ArrayList<>();

        if(checkIfCapturePossible()){
            ArrayList<String> captures = multipleCaptures();
            ArrayList<String> longestCaptures = new ArrayList<>();
            longestCaptures.add("");
            for(String val : captures){
                if(val.length() > longestCaptures.get(0).length()){
                    longestCaptures.clear();
                    longestCaptures.add(val);
                }
                else if(val.length() == longestCaptures.get(0).length()){
                    longestCaptures.add(val);
                }
            }

            movements.addAll(longestCaptures);
        }
        else{
            for(int i = 0; i<8; i++){
                for(int k = 0; k<8; k++){

                    if(board[k][i] == blackPawn && currPlayer == blackPawn) {
                        if (k < 7) {
                            if (i < 7 && board[k+1][i+1] == empty) {
                                StringBuilder s = new StringBuilder(String.valueOf((k * 10 + i) * 100 + (k + 1) * 10 + i + 1));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                            if (i > 0 && board[k+1][i-1] == empty) {
                                StringBuilder s  = new StringBuilder(String.valueOf((k * 10 + i) * 100 + (k + 1) * 10 + i - 1));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                        }
                    }

                    if(board[k][i] == whitePawn && currPlayer == whitePawn){
                        if(k > 0) {
                            if(i < 7 && board[k-1][i+1] == empty){
                                StringBuilder s  = new StringBuilder(String.valueOf((k*10 + i) * 100 + (k-1)*10 + i+1));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                            if(i > 0 && board[k-1][i-1] == empty){
                                StringBuilder s  = new StringBuilder(String.valueOf((k*10 + i) * 100 + (k-1)*10 + i-1));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                        }
                    }

                    if((board[k][i] == whiteDame && currPlayer == whitePawn) || (board[k][i] == blackDame && currPlayer == blackPawn)){
                        int j = 1;
                        while(k+j < 7 && i+j <7){
                            if(board[k+j][i+j] == empty){
                                StringBuilder s  = new StringBuilder(String.valueOf(((k*10 + i) * 100) + (k+j) * 10 + i+j));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                            else{
                                break;
                            }
                            j++;
                        }

                        j = 1;
                        while(k+j < 7 && i-j >= 1){
                            if(board[k+j][i-j] == empty){
                                StringBuilder s  = new StringBuilder(String.valueOf(((k*10 + i) * 100) + (k+j) * 10 + i-j));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                            else{
                                break;
                            }
                            j++;
                        }

                        j = 1;
                        while(k-j >= 1 && i+j < 7){
                            if(board[k-j][i+j] == empty){
                                StringBuilder s  = new StringBuilder(String.valueOf(((k*10 + i) * 100) + (k-j) * 10 + i+j));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                            else{
                                break;
                            }
                            j++;
                        }

                        j = 1;
                        while(k-j >= 1 && i-j >= 1){
                            if(board[k-j][i-j] == empty){
                                StringBuilder s  = new StringBuilder(String.valueOf(((k*10 + i) * 100) + (k-j) * 10 + i-j));
                                while(s.length() < 4){
                                    s.insert(0, "0");
                                }
                                movements.add(s.toString());
                            }
                            else{
                                break;
                            }
                            j++;
                        }
                    }
                }
            }
        }


        return movements;
    }

    public int boardValue(){
        int val = 0;
        for(int i = 0; i<8; i++){
            for(int k = 0; k<8; k++){
                if(i==0 || i==7 || k==0 || k==7){ // lowest weight
                    if(board[i][k] == blackPawn) val-= lowestWeight * pawnValue;
                    if(board[i][k] == blackDame) val-= lowestWeight * dameValue;
                    if(board[i][k] == whitePawn) val+= lowestWeight * pawnValue;
                    if(board[i][k] == whiteDame) val+= lowestWeight * dameValue;
                }
                else if(i==1 || i==6 || k==1 || k==6){ // middle weight
                    if(board[i][k] == blackPawn) val-= middleWeight * pawnValue;
                    if(board[i][k] == blackDame) val-= middleWeight * dameValue;
                    if(board[i][k] == whitePawn) val+= middleWeight * pawnValue;
                    if(board[i][k] == whiteDame) val+= middleWeight * dameValue;
                }
                else{ // highest weight
                    if(board[i][k] == blackPawn) val-= highestWeight * pawnValue;
                    if(board[i][k] == blackDame) val-= highestWeight * dameValue;
                    if(board[i][k] == whitePawn) val+= highestWeight * pawnValue;
                    if(board[i][k] == whiteDame) val+= highestWeight * dameValue;
                }
            }
        }
        return val;
    }


    private void moveWithoutConstraints(int from, int to){
        int i, k, l, m;

        i = from % 10;
        k = from / 10;

        l = to % 10;
        m = to / 10;

        if(currPlayer == whitePawn && board[k][i] == whiteDame){
            board[m][l] = whiteDame;
            dameCapture(i, k, l, m);
        }
        else if(currPlayer == blackPawn && board[k][i] == blackDame){
            board[m][l] = blackDame;
            dameCapture(i, k, l, m);
        }
        else {
            if(Math.abs(i-l ) == 2){
                board[(k+m)/2][(i+l)/2] = empty;
            }
            board[m][l] = currPlayer;
        }

        board[k][i] = ' ';



    }

    private void dameCapture(int i, int k, int l, int m){
        if(i<l && k<m){
            for(int j = 1; j<l-i; j++){
                board[k+j][i+j] = empty;
            }
        }
        if(i<l && k>m){
            for(int j = 1; j<l-i; j++){
                board[k-j][i+j] = empty;
            }

        }
        if(i>l && k<m){
            for(int j = 1; j<i-l; j++){
                board[k+j][i-j] = empty;
            }
        }

        if(i>l && k>m){
            for(int j = 1; j<i-l; j++){
                board[k-j][i-j] = empty;
            }
        }
    }

    private boolean checkConditions(int i, int k, int l, int m, boolean silent){
        if(k < 8 && k >= 0 && m < 8 && m >= 0 && i < 8 && i >= 0 && l < 8 && l >= 0){
            if(board[m][l] == empty){
                if(board[m][l] != whiteSpace){
                    if(checkIfCapturePossible()){
                        ArrayList<String> captures = new ArrayList<>();
                        if(captureStart == -1){
                            captures = multipleCaptures();
                        }
                        else{
                            captures.add(String.valueOf(captureStart));
                            multipleCaptures(captureStart%10, captureStart/10, String.valueOf(captureStart), captures);
                        }
                        ArrayList<String> longestCaptures = new ArrayList<>();
                        longestCaptures.add("");
                        for(String val : captures){
                            if(val.length() > longestCaptures.get(0).length()){
                                longestCaptures.clear();
                                StringBuilder valBuilder = new StringBuilder(val);
                                while(valBuilder.length() < 4){
                                    valBuilder.insert(0, "0");
                                }
                                val = valBuilder.toString();
                                longestCaptures.add(val);
                            }
                            else if(val.length() == longestCaptures.get(0).length()){
                                StringBuilder valBuilder = new StringBuilder(val);
                                while(valBuilder.length() < 4){
                                    valBuilder.insert(0, "0");
                                }
                                val = valBuilder.toString();
                                longestCaptures.add(val);
                            }
                        }


                        StringBuilder move = new StringBuilder(String.valueOf((10 * k + i) * 100 + 10 * m + l));
                        while(move.length() < 4){
                            move.insert(0, "0");
                        }

                        if(longestCaptures.get(0).length() == 4){
                            if(longestCaptures.contains(move.toString())){
                                captureStart = -1;
                                return true;
                            }
                            else {
                                if(!silent) System.out.println("You have to capture opponent's pawn");
                            }
                        }
                        else{
                            for(String val : longestCaptures){
                                if(val.substring(0, 4).equals(move.toString())){
                                    if(captureStart == -1 || captureStart == 10*k + i){
                                        captureStart = 10*m + l;
                                        return true;
                                    }
                                    else {
                                        if(!silent) System.out.println("You have to capture opponent's pawn");
                                    }
                                }
                                else {
                                    if(!silent) System.out.println("You have to choose maximal capture possible");
                                }
                            }
                        }

                    }
                    else{
                        captureStart = -1;
                        if(board[k][i] == currPlayer){
                            return (currPlayer == blackPawn && k + 1 == m && (i + 1 == l || i - 1 == l)) || (currPlayer == whitePawn && k - 1 == m && (i + 1 == l || i - 1 == l));
                        }
                        else if((currPlayer == blackPawn && board[k][i] == blackDame) || (currPlayer == whitePawn && board[k][i] == whiteDame)){
                            return Math.abs(i - l) == Math.abs(k - m);
                        }
                    }

                }
            }
        }
        return false;
    }

    private void switchToDame(int l, int m){
        if(currPlayer == whitePawn) board[m][l] = whiteDame;
        else board[m][l] = blackDame;
    }

    private boolean checkEndOfBoard(int m){
        return (currPlayer == whitePawn && m == 0) || (currPlayer == blackPawn && m == 7);
    }

    private boolean checkIfCapturePossible(){
        for(int i = 0; i<8; i++){
            for(int k = 0; k<8; k++){
                if(!checkIfPawnCanCaptureAnother(i, k).isEmpty()) {
                    if((currPlayer == blackPawn && (board[k][i] == blackPawn || board[k][i] == blackDame)) || (currPlayer == whitePawn && (board[k][i] == whitePawn || board[k][i] == whiteDame)))
                    //System.out.println("pawn " + i + " " + k);
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Integer> checkIfPawnCanCaptureAnother(int i, int k){
        ArrayList<Integer> captures = new ArrayList<>();
        if(board[k][i] == blackPawn && currPlayer == blackPawn){ // board[k][i] srodek board[k-1][i-1] [k-1][i+1] [k+1][i-1] [k+1][i+1]
            if(k >= 2){
                if(i >= 2){
                    // check board[k-1][i-1]
                    if(board[k-1][i-1] == whitePawn || board[k-1][i-1] == whiteDame){
                        if(board[k-2][i-2] == empty){
                            captures.add((k - 2) * 10 + (i - 2));
                        }
                    }
                }
                if(i < 6){
                    // check board[k-1][i+1]
                    if(board[k-1][i+1] == whitePawn || board[k-1][i+1] == whiteDame){
                        if(board[k-2][i+2] == empty){
                            captures.add((k - 2) * 10 + (i + 2));
                        }
                    }
                }
            }
            if(k < 6){
                if(i >= 2){
                    // check board[k+1][i-1]
                    if(board[k+1][i-1] == whitePawn || board[k+1][i-1] == whiteDame){
                        if(board[k+2][i-2] == empty){
                            captures.add((k + 2) * 10 + (i - 2));
                        }
                    }
                }
                if(i < 6){
                    // check board[k+1][i+1]
                    if(board[k+1][i+1] == whitePawn || board[k+1][i+1] == whiteDame){
                        if(board[k+2][i+2] == empty){
                            captures.add((k + 2) * 10 + (i + 2));
                        }
                    }
                }
            }
        }

        else if(board[k][i] == whitePawn && currPlayer == whitePawn){ // board[k][i] srodek board[k-1][i-1] [k-1][i+1] [k+1][i-1] [k+1][i+1]
            if(k >= 2){
                if(i >= 2){
                    // check board[k-1][i-1]
                    if(board[k-1][i-1] == blackPawn || board[k-1][i-1] == blackDame){
                        if(board[k-2][i-2] == empty){
                            captures.add((k - 2) * 10 + (i - 2));
                        }
                    }
                }
                if(i < 6){
                    // check board[k-1][i+1]
                    if(board[k-1][i+1] == blackPawn || board[k-1][i+1] == blackDame){
                        if(board[k-2][i+2] == empty){
                            captures.add((k - 2) * 10 + (i + 2));
                        }
                    }
                }
            }
            if(k < 6){
                if(i >= 2){
                    // check board[k+1][i-1]
                    if(board[k+1][i-1] == blackPawn || board[k+1][i-1] == blackDame){
                        if(board[k+2][i-2] == empty){
                            captures.add((k + 2) * 10 + (i - 2));
                        }
                    }
                }
                if(i < 6){
                    // check board[k+1][i+1]
                    if(board[k+1][i+1] == blackPawn || board[k+1][i+1] == blackDame){
                        if(board[k+2][i+2] == empty){
                            captures.add((k + 2) * 10 + (i + 2));
                        }
                    }
                }
            }
        }

        else if(board[k][i] == whiteDame && currPlayer == whitePawn){
            boolean found = false;
            int j = 1;
            while(k+j < 7 && i+j < 7){
                if(board[k+j][i+j] == blackPawn || board[k+j][i+j] == blackDame || found){
                    if(board[k+j+1][i+j+1] == empty) {
                        captures.add((k+j+1) * 10 + (i+j+1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k+j][i+j] == whitePawn || board[k+j][i+j] == whiteDame || (found && (board[k+j][i+j] == blackPawn || board[k+j][i+j] == blackDame)))
                    break;
                j++;
            }
            j = 1;
            found = false;

            while(k+j < 7 && i-j >= 1){
                if(board[k+j][i-j] == blackPawn || board[k+j][i-j] == blackDame || found){
                    if(board[k+j+1][i-j-1] == empty){
                        captures.add((k+j+1) * 10 + (i-j-1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k+j][i-j] == whitePawn || board[k+j][i-j] == whiteDame || (found && (board[k+j][i-j] == blackPawn || board[k+j][i-j] == blackDame)))
                    break;
                j++;
            }
            j = 1;
            found = false;

            while(k-j >= 1 && i+j < 7){
                if(board[k-j][i+j] == blackPawn || board[k-j][i+j] == blackDame || found){
                    if(board[k-j-1][i+j+1] == empty){
                        captures.add((k-j-1) * 10 + (i+j+1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k-j][i+j] == whitePawn || board[k-j][i+j] == whiteDame || (found && (board[k-j][i+j] == blackPawn || board[k-j][i+j] == blackDame)))
                    break;
                j++;
            }
            j = 1;
            found = false;

            while(k-j >= 1 && i-j >= 1){
                if(board[k-j][i-j] == blackPawn || board[k-j][i-j] == blackDame || found){
                    if(board[k-j-1][i-j-1] == empty){
                        captures.add((k-j-1) * 10 + (i-j-1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k-j][i-j] == whitePawn || board[k-j][i-j] == whiteDame || (found && (board[k-j][i-j] == blackPawn || board[k-j][i-j] == blackDame)))
                    break;
                j++;
            }
        }

        else if(board[k][i] == blackDame && currPlayer == blackPawn){
            int j = 1;
            boolean found = false;
            while(k+j < 7 && i+j < 7){
                if(board[k+j][i+j] == whitePawn || board[k+j][i+j] == whiteDame || found){
                    if(board[k+j+1][i+j+1] == empty){
                        captures.add((k+j+1) * 10 + (i+j+1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k+j][i+j] == blackPawn || board[k+j][i+j] == blackDame || (found && (board[k+j][i+j] == whitePawn || board[k+j][i+j] == whiteDame)))
                    break;
                j++;
            }
            j = 1;
            found = false;

            while(k+j < 7 && i-j >= 1){
                if(board[k+j][i-j] == whitePawn || board[k+j][i-j] == whiteDame || found){
                    if(board[k+j+1][i-j-1] == empty){
                        captures.add((k+j+1) * 10 + (i-j-1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k+j][i-j] == blackPawn || board[k+j][i-j] == blackDame || (found && (board[k+j][i-j] == whitePawn || board[k+j][i-j] == whiteDame)))
                    break;
                j++;
            }
            j = 1;
            found = false;

            while(k-j >=1 && i+j < 7){
                if(board[k-j][i+j] == whitePawn || board[k-j][i+j] == whiteDame || found){
                    if(board[k-j-1][i+j+1] == empty){
                        captures.add((k-j-1) * 10 + (i+j+1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k-j][i+j] == blackPawn || board[k-j][i+j] == blackDame || (found && (board[k-j][i+j] == whitePawn || board[k-j][i+j] == whiteDame)))
                    break;
                j++;
            }
            j = 1;
            found = false;

            while(k-j >= 1 && i-j >= 1){
                if(board[k-j][i-j] == whitePawn || board[k-j][i-j] == whiteDame || found){
                    if(board[k-j-1][i-j-1] == empty){
                        captures.add((k-j-1) * 10 + (i-j-1));
                        found = true;
                    }
                    else{
                        break;
                    }
                }
                if(board[k-j][i-j] == blackPawn || board[k-j][i-j] == blackDame || (found && (board[k-j][i-j] == whitePawn || board[k-j][i-j] == whiteDame)))
                    break;
                j++;
            }
        }

        return captures;
    }

    private ArrayList<String> multipleCaptures(){
        ArrayList<String> captures = new ArrayList<>();
        for(int i = 0; i<8; i++) {
            for (int k = 0; k < 8; k++) {
                String move = String.valueOf(k*10 + i);
                if(k==0){
                    move = "0" + move;
                }
                captures.add(move);
                multipleCaptures(i, k, move, captures);
            }
        }
        return captures;
    }

    private void multipleCaptures(int i, int k, String movement, ArrayList<String> moves){
        for (Integer val : checkIfPawnCanCaptureAnother(i, k)) {
            StringBuilder movementBuilder = new StringBuilder(movement);
            if(val < 10){
                movementBuilder.append("0");
            }
            movementBuilder.append(val);
            Board newBoard = new Board(this);
            newBoard.moveWithoutConstraints(k*10 + i, val);
            moves.add(movementBuilder.toString());
            newBoard.multipleCaptures(val%10, val/10, movementBuilder.toString(), moves);
        }
    }

}
