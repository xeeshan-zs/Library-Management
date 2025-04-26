import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ViewAllIssuedBooksFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewAllIssuedBooksFrame() {
        setTitle("View All Issued Books");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 18, 17));

        String[] columnNames = {"Book ID", "Book Title", "Issued To"};

        List<String[]> issuedBooks = loadAllIssuedBooks();

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

        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private List<String[]> loadAllIssuedBooks() {
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
                String bookID = details[0].split(": ")[1];
                String issuedTo = details[1].split(": ")[1];
                String bookTitle = getBookTitle(bookID);
                issuedBooks.add(new String[]{bookID, bookTitle, issuedTo});
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

}
