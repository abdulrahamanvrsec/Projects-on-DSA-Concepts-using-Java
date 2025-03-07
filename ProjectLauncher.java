import java.awt.*;  // Import AWT for layout management
import java.net.URI;  // Import URI to open GitHub link in browser
import javax.swing.*;  // Import Swing for GUI components

/**
 * ProjectLauncher - A GUI-based launcher for multiple Java projects.
 * This program provides buttons to open different projects such as:
 * 1. Snake Game (Arrays)
 * 2. Map Navigator (Dijkstra's Algorithm)
 * 3. Cash Flow Minimizer (Graphs, Multisets, Heaps)
 * 4. File Zipper (Greedy Huffman Encoder)
 * 5. Sudoku Solver (Backtracking)
 *
 * Source Code: Replace with your GitHub URL
 */
public class ProjectLauncher {
    private JFrame mainFrame;  // Main GUI window to hold all components

    /**
     * Constructor to initialize and display the GUI.
     */
    public ProjectLauncher() {
        // Create the main window with title "Project Launcher"
        mainFrame = new JFrame("Project Launcher");
        mainFrame.setSize(500, 450);  // Adjusted window size for better fit
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exit application when closed
        mainFrame.setLayout(new GridLayout(8, 1)); // Increased to 8 rows to fit all buttons properly

        // Heading labels
        JLabel label = new JLabel("Select a Project", SwingConstants.CENTER);
        JLabel label2 = new JLabel("These projects cover DSA topics using Java.", SwingConstants.CENTER);
        
        // Add labels to the main window
        mainFrame.add(label);
        mainFrame.add(label2);

        // Create buttons for each project with concise labels
        JButton snakeGameButton = new JButton("Snake Game (using Arrays)");
        JButton mapNavigatorButton = new JButton("Map Navigator (Dijkstra's Algorithm)");
        JButton cashFlowButton = new JButton("Cash Flow Minimizer (Graphs,Multisets,Heaps)");
        JButton fileZipperButton = new JButton("File Zipper (Greedy Huffman Encoder)");
        JButton sudokuSolverButton = new JButton("Sudoku Solver (Backtracking)");
        JButton githubButton = new JButton("View Source Code on GitHub");

        // Add event listeners to handle button clicks
        snakeGameButton.addActionListener(e -> launchProject("Snakegame"));
        mapNavigatorButton.addActionListener(e -> launchProject("MapNavigator"));
        cashFlowButton.addActionListener(e -> launchProject("CashFlowMinimizer"));
        fileZipperButton.addActionListener(e -> launchProject("FileZipper"));
        sudokuSolverButton.addActionListener(e -> launchProject("SudokuSolverGUI"));
        githubButton.addActionListener(e -> openGitHubRepo());

        // Add buttons to the GUI window
        mainFrame.add(snakeGameButton);
        mainFrame.add(mapNavigatorButton);
        mainFrame.add(cashFlowButton);
        mainFrame.add(fileZipperButton);
        mainFrame.add(sudokuSolverButton);
        mainFrame.add(githubButton); // Add GitHub button to the GUI

        // Make the GUI visible to the user
        mainFrame.setVisible(true);
    }

    /**
     * Method to launch the selected project. It calls the respective main()
     * method of the selected Java class.
     *
     * @param projectName The name of the project to launch.
     */
    private void launchProject(String projectName) {
        try {
            switch (projectName) {
                case "Snakegame":
                    SnakeGame.main(new String[]{});  // Call the main() method of Snakegame
                    break;
                case "MapNavigator":
                    MapNavigator.main(new String[]{});  // Call the main() method of MapNavigator
                    break;
                case "CashFlowMinimizer":
                    CashFlowMinimizer.main(new String[]{});  // Call the main() method of CashFlowMinimizer
                    break;
                case "FileZipper":
                    FileZipperGUI.main(new String[]{});  // Call the main() method of FileZipper
                    break;
                case "SudokuSolverGUI":
                    SudokuSolverGUI.main(new String[]{});  // Call the main() method of SudokuSolverGUI
                    break;
                default:
                    // Show an error message if project is not found
                    JOptionPane.showMessageDialog(mainFrame, "Project not found!");
            }
        } catch (Exception e) {
            // Show an error message if project fails to launch
            JOptionPane.showMessageDialog(mainFrame, "Error launching " + projectName);
        }
    }

    /**
     * Method to open the GitHub repository URL in the default web browser.
     */
    private void openGitHubRepo() {
        try {
            // Replace "your-username" with your actual GitHub repository link
            URI githubURL = new URI("https://github.com/your-username/DSA-Java-Projects");
            Desktop.getDesktop().browse(githubURL);  // Open the URL in the default web browser
        } catch (Exception e) {
            // Show an error message if GitHub link fails to open
            JOptionPane.showMessageDialog(mainFrame, "Unable to open GitHub link.");
        }
    }

    /**
     * Main method - Entry point of the program. Creates an instance of
     * ProjectLauncher and starts the GUI.
     */
    public static void main(String[] args) {
        // Run GUI in event dispatch thread for thread safety
        SwingUtilities.invokeLater(ProjectLauncher::new);
    }
}
