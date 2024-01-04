import java.util.Random;

public class AI {

    private static final Random RANDOM = new Random();

    /**
     * Die Staerke der K.I.
     */
    private final double strength;

    /**
     * Konstruktormethode zum Erstellen einer K.I. mit der uebergebenen Faehigkeitsstufe
     * @param strength Die Faehigkeitsstufe der K.I. (0: random; 1: perfekt)
     */
    public AI(double strength) {
        this.strength = strength;
    }

    /**
     * Die Methode bestimmt den bestmoeglichen Spielzug fuer den uebergebenen Spieler
     * @param board  Das aktuelle Spielfeld als flaches Array fuer einfachere Benutzung des MinMax Algorithmus (0: frei; 1: X; 2: O).
     * @param player Der Spieler der aktuell am Zug ist (1: X; 2: O).
     * @return int gibt den Index des besten Zuges im Array zurueck
     */
    public int doMove(int[] board, int player) {
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
                board[i] = player;

                int score = min(board, player % 2 + 1, 1, bestMoveScore);
                if(score > bestMoveScore) {
                    bestMoveScore = score;
                    bestMove = i;
                }

                board[i] = 0;
            }
        }
        return bestMove;
    }

    /**
     * Ermittlung des niedrigst moeglichem Evaluierungswert des Spielverlaufes fuer den Gegner.
     * Dafuer wird fuer jedes Feld den folgenden Spielbaum und dessen Evaulierungswert generiert.
     * Dies geschieht durch das rekursive Durchlaufen des Spielbaums wobei nicht maximierende Spielpfade fuer den Spieler nicht weiter verfolgt werden.
     * @param board Das Spielbrett
     * @param player Der Spieler der am Zug ist
     * @param depth Die Tiefe des Spielbaums bzw. der Rekursionsstufe
     * @param currentMaxScore Der aktuelle maximale Evaluierungswert fuer den zu Spielenden
     * @return den besten Evaluierungswert
     */
    private int min(int[] board, int player, int depth, int currentMaxScore) {
        int winner = checkForWinner(board);
        if(winner != 0) {
            int score = 100 - depth;
            return winner == player ? -score : score;
        }

        if(isFull(board)) return 0;

        int bestMoveScore = Integer.MAX_VALUE;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 0) {
                board[i] = player;

                int score = max(board, player % 2 + 1, depth + 1, bestMoveScore);
                if(score < bestMoveScore) {
                    bestMoveScore = score;
                }

                board[i] = 0;
                if(bestMoveScore <= currentMaxScore) return bestMoveScore;
            }
        }
        return bestMoveScore;
    }

    /**
     * Ermittlung des niedrigst moeglichem Evaluierungswert des Spielverlaufes fuer den Gegner.
     * Dafuer wird fuer jedes Feld den folgenden Spielbaum und dessen Evaulierungswert generiert.
     * Dies geschieht durch das rekursive Durchlaufen des Spielbaums wobei nicht minimierende Spielpfade fuer den Gegner nicht weiter verfolgt werden.
     * @param board Das Spielbrett
     * @param player Der Spieler der am Zug ist
     * @param depth Die Tiefe des Spielbaums bzw. der Rekursionsstufe
     * @param currentMinScore Der aktuelle minimale Evaluierungswert fuer den Gegner
     * @return den besten Evaluierungswert
     */
    private int max(int[] board, int player, int depth, int currentMinScore) {
        int winner = checkForWinner(board);
        if(winner != 0) {
            int score = 100 - depth;
            return winner == player ? score : -score;
        }

        if(isFull(board)) return 0;

        int bestMoveScore = Integer.MIN_VALUE;
        for(int i = 0; i < board.length; i++) {
            if(board[i] == 0) {
                board[i] = player;

                int score = min(board, player % 2 + 1, depth + 1, bestMoveScore);
                if(score > bestMoveScore) {
                    bestMoveScore = score;
                }

                board[i] = 0;
                if(bestMoveScore >= currentMinScore) return bestMoveScore;
            }
        }
        return bestMoveScore;
    }

    /**
     * Die Methode ueberprueft, ob ein Spieler auf dem uebergebenden Spielbrett bereits gewonnen hat.
     * Dies wird durch das Aufrufen der Methoden checkHorizontally, checkVertically und checkDiagonally bewerkstelligt
     * @param board Das aktuelle Spielbrett
     * @return Den Gewinner auf dem aktuellen Spielbrett (0: Kein Gewinner; 1: X, 2: O)
     */
    public static int checkForWinner(int[] board) {
        int winner=checkHorizontally(board);
        if(winner!=0) return winner;
        winner=checkVertically(board);
        if(winner!=0) return winner;
        winner=checkDiagonally(board,1);
        if(winner!=0) return winner;
        winner=checkDiagonally(board,2);
        return winner;
    }
    /**
     * Ueberprueft das Spielbrett horizontal auf einen Gewinner
     * @param board Das aktuelle Spielbrett
     * @return Den Gewinner auf dem aktuellen Spielbrett (0: Kein Gewinner; 1: X, 2: O)
     */
    private static int checkHorizontally(int[] board) {
        for(int y=0; y<3; y++) {
            boolean winnerIsX=true;
            boolean winnerIsO=true;
            for(int x=0; x<3; x++) {
                if(board[y*3+x]!=1) {
                    winnerIsX=false;
                }
                if(board[y*3+x]!=2) {
                    winnerIsO=false;
                }
            }
            if(winnerIsX) return 1;
            if(winnerIsO) return 2;
        }
        return 0;
    }

    /**
     * Ueberprueft das Spielbrett vertikal auf einen Gewinner
     * @param board Das aktuelle Spielbrett
     * @return Den Gewinner auf dem aktuellen Spielbrett (0: Kein Gewinner; 1: X, 2: O)
     */
    private static int checkVertically(int[] board) {
        for(int x=0; x<3; x++) {
            boolean winnerIsX=true;
            boolean winnerIsO=true;
            for(int y=0; y<3; y++) {
                if(board[y*3+x]!=1) {
                    winnerIsX=false;
                }
                if(board[y*3+x]!=2) {
                    winnerIsO=false;
                }
            }
            if(winnerIsX) return 1;
            if(winnerIsO) return 2;
        }
        return 0;
    }

    /**
     * Ueberprueft das Spielbrett diagonal ob der Gewinner dem uebergebenen Spieler entspricht
     * @param board Das aktuelle Spielbrett
     * @param player Der zu ueberpruefende Spieler
     * @return Den Gewinner auf dem aktuellen Spielbrett (0: Kein Gewinner; 1: X, 2: O)
     */
    private static int checkDiagonally(int[] board, int player) {
        boolean b1=true;
        boolean b2=true;
        for(int x=0; x<3; x++) {
            if(board[x*3+x]!=player) b1=false;
            if(board[(2-x)*3+x]!=player) b2=false;
        }
        if(b1||b2) return player;
        return 0;
    }

    /**
     * Ueberprueft, ob das Spielbrett voll ist und keine Zuege mehr getaetigt werden koennen
     * @param board Das aktuelle Spielbrett
     * @return true, wenn das Spielbrett voll ist. Wenn es freie Positionen gibt false
     */
    private static boolean isFull(int[] board) {
        for (int j : board) {
            if (j == 0) {
                return false;
            }
        }
        return true;
    }
}