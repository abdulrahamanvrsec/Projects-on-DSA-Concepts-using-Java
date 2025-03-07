import java.awt.*; // Importing Swing for GUI components
import java.awt.event.ActionEvent; // Importing AWT for layout management
import javax.swing.*; // Importing event handling

public class SudokuSolverGUI {
    
    // 9x9 grid to store Sudoku values
    private static int[][] sudokuGrid = new int[9][9];
    // Text fields for user input in the grid
    private static JTextField[][] textFields = new JTextField[9][9];
    
    public static void main(String[] args) {
        // Launch GUI in event dispatch thread
        SwingUtilities.invokeLater(SudokuSolverGUI::createGUI);
    }
    
    private static void createGUI() {
        // Create main application window
        JFrame frame = new JFrame("Sudoku Solver");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Create a panel to hold the Sudoku grid
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Create a text field for each cell
                textFields[row][col] = new JTextField(1);
                textFields[row][col].setHorizontalAlignment(JTextField.CENTER);
                gridPanel.add(textFields[row][col]); // Add text field to the grid panel
            }
        }
        
        // Create a button to solve the Sudoku
        JButton solveButton = new JButton("Solve Sudoku");
        solveButton.addActionListener((ActionEvent e) -> {
            readInputGrid(); // Read user input from the grid
            if (solveSudoku(sudokuGrid)) {
                updateGridWithSolution(); // Update the grid with the solution
            } else {
                JOptionPane.showMessageDialog(frame, "No solution exists.");
            }
        });
        
        // Add components to the main frame
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(solveButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private static void readInputGrid() {
        // Read user input from text fields and store in sudokuGrid
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = textFields[row][col].getText().trim();
                if (!text.isEmpty() && text.matches("[1-9]")) {
                    sudokuGrid[row][col] = Integer.parseInt(text);
                } else {
                    sudokuGrid[row][col] = 0; // Empty cells are set to 0
                }
            }
        }
    }
    
    private static void updateGridWithSolution() {
        // Update the text fields with the solved Sudoku grid
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                textFields[row][col].setText(String.valueOf(sudokuGrid[row][col]));
            }
        }
    }
    
    private static boolean solveSudoku(int[][] board) {
        // Backtracking algorithm to solve Sudoku
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) { // Find an empty cell
                    for (int num = 1; num <= 9; num++) { // Try numbers 1 to 9
                        if (isValid(board, row, col, num)) { // Check if valid
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true; // If solved, return true
                            }
                            board[row][col] = 0; // Backtrack if not valid
                        }
                    }
                    return false; // No valid number found, backtrack
                }
            }
        }
        return true; // Puzzle solved
    }
    
    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row and column for duplicate numbers
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        // Check the 3x3 subgrid for duplicates
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }
        return true; // Valid placement
    }
}

// 5 3 _ | _ 7 _ | _ _ _
// 6 _ _ | 1 9 5 | _ _ _
// _ 9 8 | _ _ _ | _ 6 _
// ---------------------
// 8 _ _ | _ 6 _ | _ _ 3
// 4 _ _ | 8 _ 3 | _ _ 1
// 7 _ _ | _ 2 _ | _ _ 6
// ---------------------
// _ 6 _ | _ _ _ | 2 8 _
// _ _ _ | 4 1 9 | _ _ 5
// _ _ _ | _ 8 _ | _ 7 9

