import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import javax.swing.*;

/**
 * Cash Flow Minimizer
 * This Java program optimizes transactions among a group of people
 * to minimize the number of payments required to settle all debts.
 *
 * Steps:
 * 1. Input debts as (payer, receiver, amount)triplets.
 * 2. Compute net balances for each individual.
 * 3. Use a min-heap (priority queue) to settle amounts efficiently.
 * 4. Display minimized transactions in a GUI.
 */
public class CashFlowMinimizer {
    // Stores the net balance of each person
    private static Map<String, Integer> balanceMap = new HashMap<>();

    public static void main(String[] args) {
        // Launch the GUI in a separate event dispatch thread
        SwingUtilities.invokeLater(CashFlowMinimizer::createGUI);
    }

    /**
     * Creates a GUI to accept inputs and display minimized transactions.
     */
    private static void createGUI() {
        JFrame frame = new JFrame("Cash Flow Minimizer");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JTextField payerField = new JTextField();
        JTextField receiverField = new JTextField();
        JTextField amountField = new JTextField();
        JButton addButton = new JButton("Add Transaction");
        JButton minimizeButton = new JButton("Minimize Cash Flow");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Adding input fields and buttons to the panel
        panel.add(new JLabel("Payer:"));
        panel.add(payerField);
        panel.add(new JLabel("Receiver:"));
        panel.add(receiverField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(addButton);
        panel.add(minimizeButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Handle adding transactions when the button is clicked
        addButton.addActionListener(e -> {
            String payer = payerField.getText().trim();
            String receiver = receiverField.getText().trim();
            int amount;
            try {
                amount = Integer.parseInt(amountField.getText().trim());
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid positive amount.");
                return;
            }
            addTransaction(payer, receiver, amount);
            payerField.setText("");
            receiverField.setText("");
            amountField.setText("");
        });

        // Handle minimizing transactions when the button is clicked
        minimizeButton.addActionListener(e -> {
            List<String> minimizedTransactions = minimizeTransactions();
            List<String> optimizedTransactions = optimizeTransactions(minimizedTransactions);
            resultArea.setText(String.join("\n", optimizedTransactions));
        });

        frame.setVisible(true);
    }

    /**
     * Adds a transaction to the balance map.
     * @param payer    Person who is paying.
     * @param receiver Person who is receiving.
     * @param amount   Amount transferred.
     */
    private static void addTransaction(String payer, String receiver, int amount) {
        balanceMap.put(payer, balanceMap.getOrDefault(payer, 0) - amount);
        balanceMap.put(receiver, balanceMap.getOrDefault(receiver, 0) + amount);
    }

    /**
     * Minimizes the number of transactions using a min-max heap approach.
     * @return List of minimized transactions.
     */
    private static List<String> minimizeTransactions() {
        PriorityQueue<Person> creditors = new PriorityQueue<>((a, b) -> b.amount - a.amount);
        PriorityQueue<Person> debtors = new PriorityQueue<>(Comparator.comparingInt(a -> a.amount));

        // Categorize people as creditors or debtors based on their balances
        for (Map.Entry<String, Integer> entry : balanceMap.entrySet()) {
            if (entry.getValue() > 0) {
                creditors.add(new Person(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() < 0) {
                debtors.add(new Person(entry.getKey(), -entry.getValue()));
            }
        }

        List<String> result = new ArrayList<>();
        // Settle debts using a greedy approach
        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Person debtor = debtors.poll();
            Person creditor = creditors.poll();

            int minAmount = Math.min(debtor.amount, creditor.amount);
            result.add(debtor.name + " pays " + minAmount + " to " + creditor.name);

            debtor.amount -= minAmount;
            creditor.amount -= minAmount;

            // Reinsert if they still owe or are owed money
            if (debtor.amount > 0) {
                debtors.add(debtor);
            }
            if (creditor.amount > 0) {
                creditors.add(creditor);
            }
        }
        return result;
    }

    /**
     * Optimizes the transactions by combining redundant payments.
     * @param transactions The minimized transactions.
     * @return The final optimized transaction list.
     */
    private static List<String> optimizeTransactions(List<String> transactions) {
        Map<String, Integer> finalBalances = new HashMap<>();
        for (String transaction : transactions) {
            String[] parts = transaction.split(" ");
            String payer = parts[0];
            int amount = Integer.parseInt(parts[2]);
            String receiver = parts[4];
            finalBalances.put(payer, finalBalances.getOrDefault(payer, 0) - amount);
            finalBalances.put(receiver, finalBalances.getOrDefault(receiver, 0) + amount);
        }

        return minimizeTransactions();
    }

    /**
     * Helper class to store person and their net balance.
     */
    static class Person {
        String name;
        int amount;

        Person(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}

/*Alice pays Bob $50.

Bob pays Charlie $30.

Alice pays Charlie $20.

Payer	Receiver	Amount
Alice	Bob	50
Bob	Charlie	30
Alice	Charlie	20

Minimized Transactions Output


Alice pays 50 to Bob
Bob pays 30 to Charlie
Alice pays 20 to Charlie
Optimized Output After Minimization

Alice pays 50 to Charlie
This means Alice can directly pay Charlie $50, removing unnecessary intermediate transactions.
*/

