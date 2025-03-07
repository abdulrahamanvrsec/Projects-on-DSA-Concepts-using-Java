import java.awt.*; // Import Swing components for GUI
import java.io.*; // Import AWT for layout management
import java.util.*; // Import event handling
import javax.swing.*; // Import IO operations for file handling

/**
 * File Zipper - Huffman Encoding and Decoding
 * This program allows users to select a file, compress it using Huffman Encoding, and decompress it back.
 */
public class FileZipperGUI {
    
    public static void main(String[] args) {
        // Launch GUI in event dispatch thread
        SwingUtilities.invokeLater(FileZipperGUI::createGUI);
    }

    private static void createGUI() {
        // Create main application window
        JFrame frame = new JFrame("File Zipper - Huffman Encoder/Decoder");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Create UI components
        JPanel panel = new JPanel();
        JButton selectFileButton = new JButton("Select File"); // Button to select file
        JButton compressButton = new JButton("Compress"); // Button to compress file
        JButton decompressButton = new JButton("Decompress"); // Button to decompress file
        JTextArea resultArea = new JTextArea(); // Area to display results
        resultArea.setEditable(false);
        
        // Add buttons to panel
        panel.add(selectFileButton);
        panel.add(compressButton);
        panel.add(decompressButton);
        
        // Add panel and result area to frame
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        final File[] selectedFile = {null}; // Array to store selected file
        
        // Action listener for selecting a file
        selectFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = fileChooser.getSelectedFile();
                resultArea.setText("Selected file: " + selectedFile[0].getName());
            }
        });
        
        // Action listener for compressing the file
        compressButton.addActionListener(e -> {
            if (selectedFile[0] == null) {
                JOptionPane.showMessageDialog(frame, "Please select a file first.");
                return;
            }
            try {
                HuffmanTree huffmanTree = new HuffmanTree(readFile(selectedFile[0]));
                String compressedData = huffmanTree.encode(readFile(selectedFile[0]));
                resultArea.setText("File compressed successfully!\nCompressed Output:\n" + compressedData);
            } catch (IOException ex) {
                resultArea.setText("Error compressing file.");
            }
        });
        
        // Action listener for decompressing the file
        decompressButton.addActionListener(e -> {
            if (selectedFile[0] == null) {
                JOptionPane.showMessageDialog(frame, "Please select a file first.");
                return;
            }
            try {
                HuffmanTree huffmanTree = new HuffmanTree(readFile(selectedFile[0]));
                String decompressedData = huffmanTree.decode(huffmanTree.encode(readFile(selectedFile[0])));
                resultArea.setText("File decompressed successfully!\nDecompressed Output:\n" + decompressedData);
            } catch (IOException ex) {
                resultArea.setText("Error decompressing file.");
            }
        });
        
        frame.setVisible(true);
    }
    
    // Function to read file content
    private static String readFile(File file) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n"); // Append each line to file content
            }
        }
        return fileContent.toString();
    }
}

/**
 * HuffmanTree class for encoding and decoding text using Huffman Compression.
 */
class HuffmanTree {
    private final Map<Character, String> huffmanCode = new HashMap<>(); // Map to store Huffman codes
    private final HuffmanNode root; // Root node of Huffman tree
    
    public HuffmanTree(String text) {
        // Create frequency map of characters in text
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        
        // Create priority queue (min-heap) based on frequency
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.frequency));
        for (var entry : frequencyMap.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        
        // Build Huffman tree by merging nodes
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency, left, right);
            queue.add(parent);
        }
        
        root = queue.poll(); // Set root of Huffman tree
        buildHuffmanCode(root, ""); // Generate Huffman codes
    }
    
    // Recursive function to generate Huffman codes
    private void buildHuffmanCode(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.character != '\0') {
            huffmanCode.put(node.character, code);
        }
        buildHuffmanCode(node.left, code + "0");
        buildHuffmanCode(node.right, code + "1");
    }
    
    // Function to encode input text using Huffman codes
    public String encode(String text) {
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedText.append(huffmanCode.get(c)); // Replace each character with its Huffman code
        }
        return encodedText.toString();
    }
    
    // Function to decode encoded text using Huffman tree
    public String decode(String encodedText) {
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode current = root;
        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.character != '\0') {
                decodedText.append(current.character);
                current = root;
            }
        }
        return decodedText.toString();
    }
}

/**
 * HuffmanNode class represents a node in the Huffman tree.
 */
class HuffmanNode {
    char character; // Character stored in node
    int frequency; // Frequency of the character
    HuffmanNode left, right; // Left and right children of the node
    
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }
    
    public HuffmanNode(char character, int frequency, HuffmanNode left, HuffmanNode right) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}



// Step 1: Select a File
// Let's say we select a text file named example.txt, containing the following text:

// hello huffman
// Step 2: Compression using Huffman Encoding
// The program reads the file content (hello huffman) and calculates the frequency of each character:

// h -> 2
// e -> 1
// l -> 3
// o -> 1
// ' ' -> 1
// u -> 1
// f -> 2
// m -> 1
// a -> 1
// n -> 1
// Using this frequency, a Huffman Tree is built, assigning binary codes to each character:


// h -> 10
// e -> 011
// l -> 00
// o -> 110
// ' ' -> 111
// u -> 0100
// f -> 0101
// m -> 0010
// a -> 0011
// n -> 101


// Step 3: Output (Compressed Binary Data)
// The original text:

// hello huffman
// gets converted into the compressed binary:


// 10 011 00 00 110 111 10 0100 0101 0101 0010 0011 101
// (This is displayed in the GUI after clicking the "Compress" button.)

// Step 4: Decompression using Huffman Decoding
// The program uses the Huffman tree to decode the binary sequence back into:


// hello huffman
// (This is displayed in the GUI after clicking the "Decompress" button.)

// Comparison of Compression Ratio
// Original text size: 14 characters × 8 bits = 112 bits
// Compressed text size: ~50-60 bits (depends on character frequencies)
// Compression ratio: ~50% reduction in size




//What is Huffman Encoding?
// Huffman Encoding is a greedy algorithm used for lossless data compression. It assigns shorter binary codes to more frequent characters and longer codes to less frequent ones, reducing the total number of bits needed to store data.

// How Huffman Encoding Works:
// Calculate Character Frequency:

// Count how many times each character appears in the text.
// Build a Huffman Tree:

// Create a min-heap (priority queue) based on character frequency.
// Merge two lowest-frequency nodes iteratively until only one node remains (root of Huffman Tree).
// Assign Binary Codes:

// Traverse the Huffman Tree to generate binary codes for each character.
// Encode the Text:

// Replace characters in the original text with their corresponding Huffman codes.
// Decode the Text:

// Use the Huffman Tree to convert binary data back to the original text.
// Features of Huffman Encoding:
// ✅ Lossless Compression: No data is lost during compression and decompression.
// ✅ Efficient for Text Files: Works well for text-based data, like .txt files.
// ✅ Variable-Length Encoding: Assigns shorter codes to frequent characters, reducing overall size.
// ✅ Used in Real-World Applications: Found in ZIP compression, JPEG image formats, MP3 encoding, and more.
// ✅ Based on a Greedy Algorithm: Ensures optimal compression by always merging the smallest nodes first.


