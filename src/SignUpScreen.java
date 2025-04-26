import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SignUpScreen extends JFrame implements ActionListener {

    boolean type;
    JLabel nameLbl, userNameLbl, passLbl, confirmPassLbl;
    JTextField nameFd, userNameFd;
    JButton signUpBtn, backBtn;
    JPasswordField passFd, confirmPassFd;

    SignUpScreen(boolean type) {
        super("Sign-Up");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        this.type = type;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        nameLbl = new JLabel("Name:");
        nameLbl.setForeground(Color.white);
        userNameLbl = new JLabel("User Name:");
        userNameLbl.setForeground(Color.white);
        passLbl = new JLabel("Password:");
        passLbl.setForeground(Color.white);
        confirmPassLbl = new JLabel("Confirm Pass:");
        confirmPassLbl.setForeground(Color.white);

        nameFd = new JTextField(15);
        userNameFd = new JTextField(15);
        passFd = new JPasswordField(15);
        confirmPassFd = new JPasswordField(15);

        signUpBtn = new JButton("Sign Up");
        signUpBtn.setBackground(Color.white);
        signUpBtn.setFocusPainted(false);
        backBtn = new JButton("Back");
        backBtn.setBackground(Color.white);
        backBtn.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(nameLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userNameLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(confirmPassLbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(nameFd, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(userNameFd, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passFd, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(confirmPassFd, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(backBtn, gbc);

        gbc.gridx = 3;
        add(signUpBtn, gbc);

        getContentPane().setBackground(new Color(2, 57, 60));

        signUpBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpBtn) {
            if (isFilled()) {
                if (userIsNew()) {
                    if (String.valueOf(passFd.getPassword()).equals(String.valueOf(confirmPassFd.getPassword()))) {
                        saveInfo();
                        Login fm = new Login(type);
                        fm.signUpComplete.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Password don't match..!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "This user name already exists\n   try another",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Fill all the fields...!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == backBtn) {
            new Login(type);
            dispose();
        }
    }

    boolean isFilled() {
        return !userNameFd.getText().isEmpty() &&
                !nameFd.getText().isEmpty() &&
                !String.valueOf(passFd.getPassword()).isEmpty() &&
                !String.valueOf(confirmPassFd.getPassword()).isEmpty();
    }

    void saveInfo() {
        String name = nameFd.getText(), user = userNameFd.getText(),
                pass = String.valueOf(passFd.getPassword());

        try {
            FileWriter zout;
            if (this.type) {
                zout = new FileWriter("Student Login.txt", true);
            } else {
                zout = new FileWriter("Admin Login.txt", true);
            }
            zout.write(name + System.lineSeparator());
            zout.write(user + System.lineSeparator());
            zout.write(pass + System.lineSeparator());
            zout.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean userIsNew() {
        File read;
        if (this.type) {
            read = new File("Student Login.txt");
        } else {
            read = new File("Admin Login.txt");
        }
        try (Scanner obj = new Scanner(read)) {
            while (obj.hasNext()) {
                obj.nextLine();
                String user = obj.nextLine();
                obj.nextLine();

                if (user.equals(userNameFd.getText())) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            return true;
        }
        return true;
    }
}
