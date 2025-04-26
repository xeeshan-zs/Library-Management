import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ViewFrame extends JFrame implements ActionListener {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton deleteButton, issueButton, editButton;
    private String username;
    private boolean type;

    public ViewFrame(String username, boolean type) {
        this.username = username;
        this.type = type;

        setTitle("View Books");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 18, 17));

        String[] columnNames = {"Book ID", "Book Title", "Author", "Publisher", "Year", "ISBN", "Copies"};

        List<String[]> bookData = loadBooksFromFile();

        tableModel = new DefaultTableModel(columnNames, 0);
        for (String[] row : bookData) {
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

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(0, 18, 18));
        deleteButton = new JButton("Delete Book");
        issueButton = new JButton("Issue Book");
        editButton = new JButton("Edit Book");

        setButtonStyle(deleteButton);
        setButtonStyle(issueButton);
        setButtonStyle(editButton);

        buttonPanel.add(deleteButton);
        buttonPanel.add(issueButton);
        buttonPanel.add(editButton);

        deleteButton.addActionListener(this);
        issueButton.addActionListener(this);
        editButton.addActionListener(this);

        if (type) {
            deleteButton.setEnabled(false);
            editButton.setEnabled(false);
        }
        if (!type){
            issueButton.setEnabled(false);
        }

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void setButtonStyle(JButton button) {
        button.setBackground(new Color(30, 30, 30));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == deleteButton) {
            deleteBook();
        } else if (e.getSource() == issueButton) {
            issueBook();
        } else if (e.getSource() == editButton) {
            editBook();
        }
    }

    private List<String[]> loadBooksFromFile() {
        List<String[]> books = new ArrayList<>();
        File file = new File("books.txt");

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No books found. The file 'books.txt' does not exist.");
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                books.add(bookDetails);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading books: " + e.getMessage());
        }
        return books;
    }

    private void saveBooksToFile(List<String[]> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
            for (String[] book : books) {
                writer.write(String.join(",", book));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving books: " + e.getMessage());
        }
    }

    private void deleteBook() {
        String bookID = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
        if (bookID == null || bookID.trim().isEmpty()) {
            return;
        }

        boolean found = false;
        List<String[]> books = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = (String) tableModel.getValueAt(i, 0);
            if (!id.equals(bookID)) {
                books.add(getRowData(i));
            } else {
                found = true;
            }
        }

        if (found) {
            tableModel.setRowCount(0);
            for (String[] book : books) {
                tableModel.addRow(book);
            }
            saveBooksToFile(books);
            JOptionPane.showMessageDialog(this, "Book deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Book not found.");
        }
    }

    private void issueBook() {
        String bookID = JOptionPane.showInputDialog(this, "Enter Book ID to issue:");
        if (bookID == null || bookID.trim().isEmpty()) {
            return;
        }

        String user = username;

        if (isBookAlreadyIssued(bookID, user)) {
            JOptionPane.showMessageDialog(this, "This book is already issued to you.");
            return;
        }

        boolean found = false;
        List<String[]> books = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = (String) tableModel.getValueAt(i, 0);
            String copiesStr = (String) tableModel.getValueAt(i, 6);
            int copies = Integer.parseInt(copiesStr);

            if (id.equals(bookID)) {
                found = true;
                if (copies > 0) {
                    copies--;
                    tableModel.setValueAt(String.valueOf(copies), i, 6);
                    saveIssuedBook(bookID, user);
                } else {
                    JOptionPane.showMessageDialog(this, "No copies available.");
                    return;
                }
            }
            books.add(getRowData(i));
        }

        if (found) {
            saveBooksToFile(books);
            JOptionPane.showMessageDialog(this, "Book issued successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Book not found.");
        }
    }

    private boolean isBookAlreadyIssued(String bookID, String username) {
        File file = new File("issued_books.txt");
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Book ID: " + bookID) && line.contains("Issued to: " + username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error checking issued books: " + e.getMessage());
        }
        return false;
    }

    private void saveIssuedBook(String bookID, String username) {
        String record = "Book ID: " + bookID + ", Issued to: " + username;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("issued_books.txt", true))) {
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving issued book: " + e.getMessage());
        }
    }

    private void editBook() {
        String bookID = JOptionPane.showInputDialog(this, "Enter Book ID to edit:");
        if (bookID == null || bookID.trim().isEmpty()) {
            return;
        }

        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String id = (String) tableModel.getValueAt(i, 0);
            if (id.equals(bookID)) {
                found = true;

                String title = JOptionPane.showInputDialog(this, "Enter new title:", tableModel.getValueAt(i, 1));
                String author = JOptionPane.showInputDialog(this, "Enter new author:", tableModel.getValueAt(i, 2));
                String publisher = JOptionPane.showInputDialog(this, "Enter new publisher:", tableModel.getValueAt(i, 3));
                String year = JOptionPane.showInputDialog(this, "Enter new year:", tableModel.getValueAt(i, 4));
                String isbn = JOptionPane.showInputDialog(this, "Enter new ISBN:", tableModel.getValueAt(i, 5));
                String copies = JOptionPane.showInputDialog(this, "Enter new number of copies:", tableModel.getValueAt(i, 6));

                tableModel.setValueAt(title, i, 1);
                tableModel.setValueAt(author, i, 2);
                tableModel.setValueAt(publisher, i, 3);
                tableModel.setValueAt(year, i, 4);
                tableModel.setValueAt(isbn, i, 5);
                tableModel.setValueAt(copies, i, 6);

                saveBooksToFile(getAllRows());
                JOptionPane.showMessageDialog(this, "Book updated successfully.");
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Book not found.");
        }
    }

    private List<String[]> getAllRows() {
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rows.add(getRowData(i));
        }
        return rows;
    }

    private String[] getRowData(int row) {
        String[] rowData = new String[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rowData[i] = (String) tableModel.getValueAt(row, i);
        }
        return rowData;
    }

}
