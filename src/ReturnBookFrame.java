import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnBookFrame extends JFrame implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton returnButton;
    private String username;

    public ReturnBookFrame(String username) {
        this.username = username;

        setTitle("Return Book");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 18, 17));

        String[] columnNames = {"Book ID", "Book Title", "Issued To"};

        List<String[]> issuedBooks = loadIssuedBooks();

        tableModel = new DefaultTableModel(columnNames, 0);
        for (String[] row : issuedBooks) {
            tableModel.addRow(row);
        }
        table = new JTable(tableModel);

        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(25);
        table.setBackground(new Color(0, 18, 17));
        table.setForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(50, 50, 50));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(0, 18, 17));
        scrollPane.getViewport().setBackground(new Color(0, 18, 17));

        returnButton = new JButton("Return Book");
        returnButton.setBackground(new Color(50, 50, 50));
        returnButton.setForeground(Color.WHITE);
        returnButton.addActionListener(this);

        add(scrollPane, BorderLayout.CENTER);
        add(returnButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private List<String[]> loadIssuedBooks() {
        List<String[]> issuedBooks = new ArrayList<>();
        File file = new File("issued_books.txt");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No issued books found. The file 'issued_books.txt' does not exist.");
            return issuedBooks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(", ");
                String issuedUsername = details[1].split(": ")[1];
                if (issuedUsername.equals(username)) {
                    String bookID = details[0].split(": ")[1];
                    issuedBooks.add(new String[]{bookID, getBookTitle(bookID), issuedUsername});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading issued books: " + e.getMessage());
        }
        return issuedBooks;
    }

    private String getBookTitle(String bookID) {
        File file = new File("books.txt");

        if (!file.exists()) {
            return "Unknown";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(bookID)) {
                    return details[1];
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading book titles: " + e.getMessage());
        }
        return "Unknown";
    }

    private void returnBook() {
        String bookID = JOptionPane.showInputDialog(this, "Enter Book ID to return:");
        if (bookID == null || bookID.trim().isEmpty()) {
            return;
        }

        boolean found = false;
        List<String[]> updatedIssuedBooks = new ArrayList<>();
        File issuedBooksFile = new File("issued_books.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(issuedBooksFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(", ");
                String currentBookID = details[0].split(": ")[1];
                String currentUsername = details[1].split(": ")[1];

                if (currentBookID.equals(bookID) && currentUsername.equals(username)) {
                    found = true;
                } else {
                    updatedIssuedBooks.add(details);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading issued books: " + e.getMessage());
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Book not found in issued records.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(issuedBooksFile))) {
            for (String[] details : updatedIssuedBooks) {
                writer.write(String.join(", ", details));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating issued books: " + e.getMessage());
            return;
        }

        File booksFile = new File("books.txt");
        List<String[]> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(booksFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(bookID)) {
                    int copies = Integer.parseInt(details[6]);
                    copies++;
                    details[6] = String.valueOf(copies);
                }
                books.add(details);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading books: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(booksFile))) {
            for (String[] book : books) {
                writer.write(String.join(",", book));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating books: " + e.getMessage());
            return;
        }

        tableModel.setRowCount(0);
        for (String[] row : loadIssuedBooks()) {
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "Book returned successfully.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returnButton) {
            returnBook();
        }
    }
}
