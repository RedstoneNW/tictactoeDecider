import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TicTacToeDecider extends JFrame {
    private final JButton[][] buttons = new JButton[3][3];
    private String[][] board;
    private boolean playerCorrect;
    private BinarySearchTree<PlayerAnswer> answerTree;
    private final JSlider sliderX = new JSlider(0,10,2);
    private final JSlider sliderO = new JSlider(0,10,2);
    private int answerCount = 0;

    public TicTacToeDecider() {
        initializeGUI();
        initializeGame();
    }

    private void initializeGame() {
        board = new String[3][3];
        playerCorrect = false;
        answerTree = new BinarySearchTree<>();
        simulateGame();
    }

    private void initializeGUI() {
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

    public void outputInOrder(BinarySearchTree<PlayerAnswer> tree) {
        if (tree.getContent() !=null) {
            outputInOrder(tree.getLeftTree());
            PlayerAnswer PlayerAnswer = tree.getContent();
            System.out.println(PlayerAnswer.answer + "");
            outputInOrder(tree.getRightTree());
        }
    }

    private void checkWin() {
        int playerAnswer = JOptionPane.showConfirmDialog(null,
                "Will the AI for player X win?");

        if (playerAnswer == 2) {
            return;
        }

        // Add the player's answer to the binary tree
        answerTree.insert(new PlayerAnswer(playerAnswer, answerCount++));

        int winner = makeAIMoves(false,5,1,1);

        playerCorrect = winner == 1;
        System.out.println("---------------------------------------------");
        outputInOrder(answerTree);

        // Compare the player's answer with the actual outcome
        if (playerAnswer == 0 && playerCorrect ||
                playerAnswer == 1 && !playerCorrect) {
            JOptionPane.showMessageDialog(null, "Correct!");
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect!");
        }

        // Reset the game for the next round
        resetGame();
    }

    private void simulateGame() {
        makeAIMoves(true,3,((double) sliderX.getValue()/10), ((double) sliderO.getValue()/10));
    }

    private int makeAIMoves(boolean xToStart, int movesToMake, double difX, double difO) {
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
            if (i==0 && !xToStart) {
                i = 1;
            }
            if (i%2 == 0) {
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

    private void resetGame() {
        // Reset the game state for the next round
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
                buttons[i][j].setText("");
            }
        }
        playerCorrect = false;

        simulateGame();
    }

    public static void main(String[] args) {
        new TicTacToeDecider();
    }
}
