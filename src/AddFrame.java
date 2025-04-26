import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class AddFrame extends JFrame implements ActionListener {
    private JLabel label1, label2, label3, label4, label5, label6, label7;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    private JButton addButton;

    public AddFrame() {
        setTitle("Add Book");
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        getContentPane().setBackground(new Color(0, 2, 18));

        label1 = new JLabel("Book ID:");
        label1.setForeground(Color.WHITE);
        label2 = new JLabel("Book Title:");
        label2.setForeground(Color.WHITE);
        label3 = new JLabel("Author:");
        label3.setForeground(Color.WHITE);
        label4 = new JLabel("Publisher:");
        label4.setForeground(Color.WHITE);
        label5 = new JLabel("Year of Publication:");
        label5.setForeground(Color.WHITE);
        label6 = new JLabel("ISBN:");
        label6.setForeground(Color.WHITE);
        label7 = new JLabel("Number of Copies:");
        label7.setForeground(Color.WHITE);

        textField1 = new JTextField(15);
        textField2 = new JTextField(15);
        textField3 = new JTextField(15);
        textField4 = new JTextField(15);
        textField5 = new JTextField(15);
        textField6 = new JTextField(15);
        textField7 = new JTextField(15);

        addButton = new JButton("Add");
        addButton.addActionListener(this);
        addButton.setBackground(Color.white);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setForeground(new Color(0, 2, 18));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label1, gbc);
        gbc.gridx = 1;
        add(textField1, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(label2, gbc);
        gbc.gridx = 1;
        add(textField2, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(label3, gbc);
        gbc.gridx = 1;
        add(textField3, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(label4, gbc);
        gbc.gridx = 1;
        add(textField4, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(label5, gbc);
        gbc.gridx = 1;
        add(textField5, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(label6, gbc);
        gbc.gridx = 1;
        add(textField6, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(label7, gbc);
        gbc.gridx = 1;
        add(textField7, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(addButton, gbc);

        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String bookID = textField1.getText().trim();
                if (!bookID.isEmpty() && isBookIDExists(bookID)) {
                    String existingTitle = getBookTitleByID(bookID);
                    JOptionPane.showMessageDialog(null,
                            "This Book ID already exists! Title: " +
                                    existingTitle + ".\nIf you want to update reocrd then go to the \"View\" option and updated info of this book",
                            "Duplicate Book ID", JOptionPane.ERROR_MESSAGE);

                    dispose();

                }
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            if (isAnyFieldEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                saveToFile();
            }
        }
    }

    private boolean isAnyFieldEmpty() {
        return textField1.getText().trim().isEmpty() ||
                textField2.getText().trim().isEmpty() ||
                textField3.getText().trim().isEmpty() ||
                textField4.getText().trim().isEmpty() ||
                textField5.getText().trim().isEmpty() ||
                textField6.getText().trim().isEmpty() ||
                textField7.getText().trim().isEmpty();
    }

    private boolean isBookIDExists(String bookID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(bookID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getBookTitleByID(String bookID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(bookID)) {
                    return fields[1];  // Book title is the second field
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateBookCopies(String bookID) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(bookID)) {
                    fields[6] = textField7.getText().trim();  // Update the number of copies
                    line = String.join(",", fields);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Book updated successfully");
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + e.getMessage());
        }
    }

    private void saveToFile() {
        String bookID = textField1.getText();
        String title = textField2.getText();
        String author = textField3.getText();
        String publisher = textField4.getText();
        String year = textField5.getText();
        String isbn = textField6.getText();
        String copies = textField7.getText();

        String record = String.join(",", bookID, title, author, publisher, year, isbn, copies);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt", true))) {
            writer.write(record);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Book added successfully");
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving book: " + e.getMessage());
        }
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }

}
