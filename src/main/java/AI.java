import java.util.Random;

public class AI {

    private static final Random RANDOM = new Random();

    private final double strength;

    /**
     * Creates a minimax AI with a given strength
     * that can play for both sides.
     * @param strength  The AI strength from 0 to 1 (0: random; 1: perfect)
     */
    public AI(double strength) {
        this.strength = strength;
    }

    /**
     * This method is used to get the next move of the AI.
     * @param board  The current state of the game as a flat int array (0: empty; 1: player 1; 2: player 2).
     * @param color  The color of the current player (AI) (1: player 1; 2: player 2).
     * @return int  Returns the index of the move inside the array
     */
    public int doMove(int[] board, int color) {
        if(strength != 1 && RANDOM.nextDouble() >= strength) {
            int emptyCells = 0;
            for (int k : board) {
                if (k == 0) emptyCells++;
            }
            int j = RANDOM.nextInt(emptyCells);
            for(int i = 0; i < board.length; i++) {
                if(board[i] == 0) {
                    if(j == 0) return i;
                    else j--;
                }
            }
        }

        int bestMove = 0;
        int bestMoveScore = Integer.MIN_VALUE;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 0) {
                board[i] = color;

                int score = min(board, color % 2 + 1, 1, bestMoveScore);
                if(score > bestMoveScore) {
                    bestMoveScore = score;
                    bestMove = i;
                }

                board[i] = 0;
            }
        }
        return bestMove;
    }
    private int min(int[] board, int color, int depth, int currentMaxScore) {
        int winner = checkForWinner(board);
        if(winner != 0) {
            int score = 100 - depth;
            return winner == color ? -score : score;
        }

        if(isFull(board)) return 0;

        int bestMoveScore = Integer.MAX_VALUE;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 0) {
                board[i] = color;

                int score = max(board, color % 2 + 1, depth + 1, bestMoveScore);
                if(score < bestMoveScore) {
                    bestMoveScore = score;
                }

                board[i] = 0;
                if(bestMoveScore <= currentMaxScore) return bestMoveScore;
            }
        }
        return bestMoveScore;
    }
    private int max(int[] board, int color, int depth, int currentMinScore) {
        int winner = checkForWinner(board);
        if(winner != 0) {
            int score = 100 - depth;
            return winner == color ? score : -score;
        }

        if(isFull(board)) return 0;

        int bestMoveScore = Integer.MIN_VALUE;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 0) {
                board[i] = color;

                int score = min(board, color % 2 + 1, depth + 1, bestMoveScore);
                if(score > bestMoveScore) {
                    bestMoveScore = score;
                }

                board[i] = 0;
                if(bestMoveScore >= currentMinScore) return bestMoveScore;
            }
        }
        return bestMoveScore;
    }

    public static int checkForWinner(int[] board) {
        int w=checkHorizontally(board);
        if(w!=0) return w;
        w=checkVertically(board);
        if(w!=0) return w;
        w=checkDiagonally(board,1);
        if(w!=0) return w;
        w=checkDiagonally(board,2);
        return w;
    }
    private static int checkVertically(int[] board) {
        for(int x=0; x<3; x++) {
            boolean b1=true;
            boolean b2=true;
            for(int y=0; y<3; y++) {
                if(board[y*3+x]!=1) {
                    b1=false;
                }
                if(board[y*3+x]!=2) {
                    b2=false;
                }
            }
            if(b1) return 1;
            if(b2) return 2;
        }
        return 0;
    }
    private static int checkHorizontally(int[] board) {
        for(int y=0; y<3; y++) {
            boolean b1=true;
            boolean b2=true;
            for(int x=0; x<3; x++) {
                if(board[y*3+x]!=1) {
                    b1=false;
                }
                if(board[y*3+x]!=2) {
                    b2=false;
                }
            }
            if(b1) return 1;
            if(b2) return 2;
        }
        return 0;
    }
    private static int checkDiagonally(int[] board, int color) {
        boolean b1=true;
        boolean b2=true;
        for(int x=0; x<3; x++) {
            if(board[x*3+x]!=color) b1=false;
            if(board[(2-x)*3+x]!=color) b2=false;
        }
        if(b1||b2) return color;
        return 0;
    }
    private static boolean isFull(int[] board) {
        for (int j : board) {
            if (j == 0) {
                return false;
            }
        }
        return true;
    }
}