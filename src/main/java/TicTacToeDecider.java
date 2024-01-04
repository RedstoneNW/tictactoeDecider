import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TicTacToeDecider extends JFrame {
    /**
     * 3x3 Tic Tac Toe Display Matrix
     */
    private final JButton[][] buttons = new JButton[3][3];
    /**
     * 3x3 Spielbrett Matrix
     */
    private String[][] board;
    /**
     * Boolean ob X das Spiel wirklich gewinnt
     */
    private boolean xWins;
    /**
     * BinarySearchTree zur Speicherung der Antworten des Spielers
     */
    private BinarySearchTree<PlayerAnswer> answerTree;
    /**
     * Slider fuer Faehigkeitenstufe von K.I. fuer Spieler X
     */
    private final JSlider sliderX = new JSlider(0,10,2);
    /**
     * Slider fuer Faehigkeitenstufe von K.I. fuer Spieler O
     */
    private final JSlider sliderO = new JSlider(0,10,2);
    /**
     * Wie viele Antworten schon gegeben worden sind
     */
    private int answerCount = 0;

    /**
     * Initialisiert das Spiel und die Oberflaeche
     */
    public TicTacToeDecider() {
        initializeGUI();
        initializeGame();
    }

    /**
     * Initialisiert das Spielfeld und den Antwortbaum. Es wird außerdem das erste Spiel mithilfe der Methode simulateGame generiert.
     */
    public void initializeGame() {
        board = new String[3][3];
        xWins = false;
        answerTree = new BinarySearchTree<>();
        simulateGame();
    }

    /**
     * Initialisiert das GUI des Spiels
     */
    public void initializeGUI() {
        JFrame frame = new JFrame("Tic Tac Toe");
        GridLayout grid = new GridLayout(4, 4);
        frame.setLayout(grid);
        frame.setSize(300, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        sliderX.setMinorTickSpacing(1);
        sliderX.setMajorTickSpacing(5);
        sliderX.setPaintLabels(true);
        sliderX.setPaintTicks(true);

        sliderO.setMinorTickSpacing(1);
        sliderO.setMajorTickSpacing(5);
        sliderO.setPaintLabels(true);
        sliderO.setPaintTicks(true);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[i][j].setEnabled(false);
                frame.add(buttons[i][j]);
            }
            if (i == 0) {
                frame.add(sliderX);
            } else if (i ==1) {
                frame.add(sliderO);
            }
        }

        JButton checkButton = new JButton("Check Win");

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        checkButton.addActionListener(e -> checkWin());
        frame.add(new JPanel());
        frame.add(new JPanel());
        frame.add(checkButton);
    }

    /**
     * Traversiert den übergebenen Baum im In-Order Prinzip und gibt dies aus
     * @param tree Der Binary Search Tree der Ausgegeben werden soll
     */
    public void outputInOrder(BinarySearchTree<PlayerAnswer> tree) {
        if (tree.getContent() !=null) {
            outputInOrder(tree.getLeftTree());
            PlayerAnswer PlayerAnswer = tree.getContent();
            System.out.println(PlayerAnswer.answer + "");
            outputInOrder(tree.getRightTree());
        }
    }

    /**
     * Die Methode uebernimmt die Verwaltung der Spieler eingabe. Dies enthält den Dialog, um die Spieler eingabe zu erhalten.
     * Diese Eingabe wird in den Antwortbaum eingefuegt udn dieser ausgegeben.
     * Es wird nun mithilfe der Methode makeAIMoves das Spiel zu Ende gespielt, mit jeweils der staerksten K.I. moeglich.
     * Es wird geprueft ob der Spieler richtig geantwortet hat oder nicht und dies wird dem Spieler ausgegeben.
     * Danach wird mithilfe der Methode resetGame das naechste Spiel erzeugt.
     */
    public void checkWin() {
        int playerAnswer = JOptionPane.showConfirmDialog(null,
                "Will the AI for player X win?");

        if (playerAnswer == 2) {
            return;
        }

        // Add the player's answer to the binary tree
        answerTree.insert(new PlayerAnswer(playerAnswer, answerCount++));

        int winner = makeAIMoves(false,9,1,1);

        xWins = winner == 1;
        System.out.println("---------------------------------------------");
        outputInOrder(answerTree);

        // Compare the player's answer with the actual outcome
        if (playerAnswer == 0 && xWins ||
                playerAnswer == 1 && !xWins) {
            JOptionPane.showMessageDialog(null, "Correct!");
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect!");
        }

        // Reset the game for the next round
        resetGame();
    }

    /**
     * Die Methode setzt die ersten Zuege der K.I. mithilfe der Methode makeAIMoves unter Beruecksictigung der auf den JSlidern eingestellten Faehigkeitsstufe
     */
    public void simulateGame() {
        makeAIMoves(true,3,((double) sliderX.getValue()/10), ((double) sliderO.getValue()/10));
    }

    /**
     * Die Methode macht die Zuege der K.I. anhand der uebergebenen Parameter und trägt diese entsprechend in das Spielfeld ein.
     * Dafuer werden 2 Objekte der Klasse AI zuhilfe genommen sowie die Methode der Objekte doMove
     * @param xToStart Der Parameter gibt an ob X den naechsten Zug macht oder nicht
     * @param movesToMake Der Parameter gibt wie viele Zuege zu machen sind
     * @param difX Der Parameter gibt an wie stark die K.I. fuer X ist
     * @param difO Der Parameter gibt an wie stark die K.I. fuer O ist
     * @return Die Methode gibt zurueck welcher ob und wenn ja welcher Spieler nach Spielen der uebergebenen Spielzuege gewonnen hat. Dabei steht 0 = Keiner, 1 = X, 2 = O
     */
    public int makeAIMoves(boolean xToStart, int movesToMake, double difX, double difO) {
        int index = 0;

        int[] linBoard = new int[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <3; j++) {
                if (Objects.equals(board[i][j], "X")) {
                    linBoard[index] = 1;
                } else if (Objects.equals(board[i][j], "O")) {
                    linBoard[index] = 2;
                } else {
                    linBoard[index] = 0;
                }
                index++;
            }
        }

        AI AIX = new AI(difX);
        AI AIO = new AI(difO);

        for (int i = 0; i < movesToMake; i++) {
            boolean gameOver = false;
            if (i==0 && !xToStart) {
                i = 1;
            }
            if (AI.checkForWinner(linBoard) != 0) {
                i = 60;
            }
            if (i%2 == 0 && !gameOver) {
                int move = AIX.doMove(linBoard, 1);
                linBoard[move] = 1;
            } else {
                int move = AIO.doMove(linBoard,2);
                linBoard[move] = 2;
            }
        }

        index = 0;

        // Iterate through rows and columns of the 2D array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (linBoard[index] == 1) {
                    board[i][j] = "X";
                } else if (linBoard[index] == 2) {
                    board[i][j] = "O";
                }
                index++;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(board[i][j]);
            }
        }

        return AI.checkForWinner(linBoard);
    }

    /**
     * Die Methode setzt das Spiel zurueck und bereitet mithilfe der Methode simulateGame eine neue Runde vor
     */
    public void resetGame() {
        // Reset the game state for the next round
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
                buttons[i][j].setText("");
            }
        }
        xWins = false;

        simulateGame();
    }

    /**
     * Die Main Methode startet das Spiel
     * @param args Main Methoden Notwendigkeit
     */
    public static void main(String[] args) {
        new TicTacToeDecider();
    }
}
