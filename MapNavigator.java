import java.awt.*; // Import Swing components for GUI
import java.awt.event.ActionEvent; // Import AWT for layout management
import java.util.*; // Import event handling
import java.util.List; // Import List explicitly to avoid ambiguity
import javax.swing.*; // Import utilities for graph representation and Dijkstra's algorithm

/**
 * Map Navigator using Dijkstra's Algorithm
 * This program allows users to find the shortest path between two locations on a map.
 */
public class MapNavigator {
    
    // Graph representation as an adjacency list
    private static final Map<String, Map<String, Integer>> graph = new HashMap<>();
    
    public static void main(String[] args) {
        // Launch GUI in event dispatch thread
        SwingUtilities.invokeLater(MapNavigator::createGUI);
    }
    
    private static void createGUI() {
        // Create main application window
        JFrame frame = new JFrame("Map Navigator - Dijkstra's Algorithm");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create UI components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2)); // Grid layout for input fields and button

        JLabel startLabel = new JLabel("Start Location:"); // Label for start location
        JTextField startField = new JTextField(); // Input field for start location
        JLabel endLabel = new JLabel("End Location:"); // Label for end location
        JTextField endField = new JTextField(); // Input field for end location
        JButton findPathButton = new JButton("Find Shortest Path"); // Button to trigger path finding
        JTextArea resultArea = new JTextArea(); // Area to display results
        resultArea.setEditable(false);

        // Add components to the panel
        panel.add(startLabel);
        panel.add(startField);
        panel.add(endLabel);
        panel.add(endField);
        panel.add(findPathButton);

        // Add panel and result area to frame
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Initialize graph with sample locations and distances
        initializeGraph();

        // Action listener for finding the shortest path
        findPathButton.addActionListener((ActionEvent e) -> {
            String start = startField.getText().trim(); // Get user input for start location
            String end = endField.getText().trim(); // Get user input for end location
            if (!graph.containsKey(start) || !graph.containsKey(end)) {
                resultArea.setText("Invalid locations. Please enter valid start and end points.");
                return;
            }
            List<String> shortestPath = dijkstra(start, end); // Find shortest path using Dijkstra's algorithm
            resultArea.setText("Shortest Path: " + String.join(" -> ", shortestPath)); // Display result
        });

        frame.setVisible(true);
    }
    
    private static void initializeGraph() {
        // Initialize a sample graph with locations and distances
        graph.put("A", Map.of("B", 5, "C", 10));
        graph.put("B", Map.of("A", 5, "D", 7));
        graph.put("C", Map.of("A", 10, "D", 3));
        graph.put("D", Map.of("B", 7, "C", 3, "E", 8));
        graph.put("E", Map.of("D", 8));
    }
    
    private static List<String> dijkstra(String start, String end) {
        // Priority queue (min-heap) to store nodes with the smallest known distances
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));
        
        // Map to store the shortest distance from the start node to each node
        Map<String, Integer> distances = new HashMap<>();
        
        // Map to track the previous node in the shortest path
        Map<String, String> previous = new HashMap<>();
        
        // Set to track visited nodes
        Set<String> visited = new HashSet<>();

        // Initialize distances: Set all to infinity except the start node
        for (String node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(new Node(start, 0)); // Start node with distance 0

        while (!pq.isEmpty()) {
            Node current = pq.poll(); // Retrieve node with smallest known distance
            if (visited.contains(current.name)) continue;
            visited.add(current.name);

            // Iterate through neighbors of the current node
            for (Map.Entry<String, Integer> neighbor : graph.get(current.name).entrySet()) {
                String neighborName = neighbor.getKey();
                int newDistance = distances.get(current.name) + neighbor.getValue();
                
                // If a shorter path is found, update the distance and priority queue
                if (newDistance < distances.get(neighborName)) {
                    distances.put(neighborName, newDistance);
                    previous.put(neighborName, current.name);
                    pq.add(new Node(neighborName, newDistance));
                }
            }
        }

        // Construct the shortest path from end to start
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path); // Reverse to get the correct order
        return path;
    }
}

/**
 * Node class to represent a location in the graph.
 */
class Node {
    String name; // Name of the node (location)
    int distance; // Distance from the start node

    public Node(String name, int distance) {
        this.name = name;
        this.distance = distance;
    }
}
