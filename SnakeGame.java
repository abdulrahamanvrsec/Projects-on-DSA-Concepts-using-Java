import java.awt.*; // Import Swing for GUI components
import java.awt.event.ActionEvent; // Import AWT for graphics such as font,graphics,color etc...
import java.awt.event.ActionListener; // Import event handling for Timer and also handle keyboard functions
import java.awt.event.KeyAdapter; // Timer event handling
import java.awt.event.KeyEvent; // Handles keyboard input
import java.util.Random; // Key event for movement
import javax.swing.*; // Random food placement

// SnakeGame class extends JPanel and implements ActionListener for game updates
public class SnakeGame extends JPanel implements ActionListener {
    
    // Constants for game settings
    private final int TILE_SIZE = 25;  // Size of each square on the grid
    private final int BOARD_WIDTH = 600;  // Game board width
    private final int BOARD_HEIGHT = 600; // Game board height
    private final int TOTAL_TILES = (BOARD_WIDTH * BOARD_HEIGHT) / (TILE_SIZE * TILE_SIZE); // Maximum tiles on board

    // Arrays to store snake's X and Y positions
    private final int x[] = new int[TOTAL_TILES];
    private final int y[] = new int[TOTAL_TILES];

    // Initial snake length
    private int snakeLength = 3;  

    // Variables to track food position
    private int foodX, foodY;

    // Score variable
    private int score = 0;
    
    // Game state variables
    private boolean running = false; // Tracks if the game is running
    private char direction = 'R'; // Initial direction (R = Right, L = Left, U = Up, D = Down)
    private Timer timer; // Timer to update game state at fixed intervals

    // Constructor - Initializes game panel and starts the game
    public SnakeGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT)); // Set game panel size
        setBackground(Color.cyan); // Set background color
        setFocusable(true); // Allow panel to receive keyboard input

        // Add key listener to capture arrow key movements
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                changeDirection(e); // Calls method to update snake direction
            }
        });

        startGame(); // Start the game
    }

    // Method to start the game
    private void startGame() {
        running = true; // Set game running state to true
        placeFood(); // Generate the first food
        timer = new Timer(100, this); // Set timer to refresh game every 100ms
        timer.start(); // Start the game timer
    }

    // Method to place food at a random position
    private void placeFood() {
        Random rand = new Random();
        foodX = rand.nextInt(BOARD_WIDTH / TILE_SIZE) * TILE_SIZE; // Random X position
        foodY = rand.nextInt(BOARD_HEIGHT / TILE_SIZE) * TILE_SIZE; // Random Y position
    }

    // Override paintComponent to draw game elements
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // Call draw method
    }

    // Method to draw the snake, food, and score
    private void draw(Graphics g) {
        if (running) {
            // Draw food (red circle)
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, TILE_SIZE, TILE_SIZE);

            // Draw snake
            for (int i = 0; i < snakeLength; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN); // Snake head color
                } else {
                    g.setColor(Color.blue); // Snake body color
                }
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

            // Draw score
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            gameOver(g); // Display game over screen
        }
    }

    // Method to move the snake
    private void move() {
        // Move body segments forward
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1]; // Shift X positions
            y[i] = y[i - 1]; // Shift Y positions
        }

        // Move the head based on the direction
        switch (direction) {
            case 'U': y[0] -= TILE_SIZE; break; // Move up
            case 'D': y[0] += TILE_SIZE; break; // Move down
            case 'L': x[0] -= TILE_SIZE; break; // Move left
            case 'R': x[0] += TILE_SIZE; break; // Move right
        }

        checkCollision(); // Check for collisions
        checkFood(); // Check if the snake ate food
    }

    // Method to check if the snake collides with itself or the walls
    private void checkCollision() {
        // Check if the snake collides with itself
        for (int i = snakeLength; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false; // Stop the game
            }
        }

        // Check if the snake collides with walls
        if (x[0] < 0 || x[0] >= BOARD_WIDTH || y[0] < 0 || y[0] >= BOARD_HEIGHT) {
            running = false; // Stop the game
        }

        // Stop timer if game ends
        if (!running) {
            timer.stop();
        }
    }

    // Method to check if the snake eats the food
    private void checkFood() {
        if (x[0] == foodX && y[0] == foodY) { // If snake head touches food
            snakeLength++; // Increase snake length
            score += 10; // Increase score
            placeFood(); // Generate new food position
        }
    }

    // Method to change the direction based on key press
    private void changeDirection(KeyEvent e) {
        int key = e.getKeyCode(); // Get key pressed

        // Prevent snake from reversing direction
        if ((key == KeyEvent.VK_LEFT) && (direction != 'R')) {
            direction = 'L';
        }
        if ((key == KeyEvent.VK_RIGHT) && (direction != 'L')) {
            direction = 'R';
        }
        if ((key == KeyEvent.VK_UP) && (direction != 'D')) {
            direction = 'U';
        }
        if ((key == KeyEvent.VK_DOWN) && (direction != 'U')) {
            direction = 'D';
        }
    }

    // Method to display "Game Over" screen
    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over!", BOARD_WIDTH / 3, BOARD_HEIGHT / 2);

        // Display final score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Final Score: " + score, BOARD_WIDTH / 3, BOARD_HEIGHT / 2 + 50);
    }

    // Method executed on every timer tick (moves snake)
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move(); // Move the snake
        }
        repaint(); // Redraw game components
    }

    // Main method to start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game"); // Create game window
        SnakeGame game = new SnakeGame(); // Create game instance
        
        frame.add(game); // Add game panel to frame
        frame.setResizable(false); // Prevent resizing
        frame.pack(); // Adjust frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close on exit
        frame.setVisible(true); // Show game window
    }
}
