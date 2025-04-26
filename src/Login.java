import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Scanner;

public class Login extends JFrame implements ActionListener {
    boolean type;
    JLabel loginLbl, passLbl,signUpComplete,forgetPass;
    JTextField loginFd;
    JPasswordField passFd;
    JButton loginBtn,signUpBtn,backBtn;

    Login(boolean type){
    super("Login");
    this.type=type;
    getContentPane().setBackground(new Color(2, 57, 60));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new GridBagLayout());
    setSize(500,400);
    setLocationRelativeTo(null);
    GridBagConstraints gbc =new GridBagConstraints();
    gbc.insets = new Insets(5,5,5,5);

    loginLbl = new JLabel("Username:");
    loginLbl.setForeground(Color.white);
    passLbl = new JLabel("Password:");
    passLbl.setForeground(Color.white);
    signUpComplete = new JLabel("Sign-Up Completed");
    signUpComplete.setFont(new Font("Serif",Font.PLAIN,12));
    signUpComplete.setForeground(Color.white);
    forgetPass = new JLabel("forget password ?");
    forgetPass.setFont(new Font("Serif",Font.ITALIC,11));
    forgetPass.setForeground(Color.red);

    loginFd = new JTextField(15);
    passFd = new JPasswordField(15);

    loginBtn = new JButton("Log-in");
    loginBtn.setMnemonic(KeyEvent.VK_ENTER);
    loginBtn.setForeground(new Color(2, 57, 60));
    loginBtn.setBackground(Color.white);
    loginBtn.setFocusPainted(false);
    signUpBtn = new JButton("Sign-up");
    signUpBtn.setForeground(new Color(2, 57, 60));
    signUpBtn.setBackground(Color.white);
    signUpBtn.setFocusPainted(false);
    backBtn = new JButton("Back");
    backBtn.setForeground(new Color(2, 57, 60));
    backBtn.setBackground(Color.white);
    backBtn.setFocusPainted(false);



    gbc.gridx=0;    gbc.gridy=1;
    gbc.gridwidth=1;
    add(loginLbl,gbc);

    gbc.gridx=1;   gbc.gridwidth=2;
    add(loginFd,gbc);

    gbc.gridx=0;    gbc.gridy=2;    gbc.gridwidth=1;
    add(passLbl,gbc);

    gbc.gridx=1;    gbc.gridwidth=2;
    add(passFd,gbc);

    gbc.gridy=5;    gbc.gridwidth=1;
    add(signUpBtn,gbc);

    gbc.gridx=2;
    add(loginBtn,gbc);

    gbc.gridx=0;
    add(backBtn,gbc);

    gbc.gridx=1;    gbc.gridy=4;
    add(signUpComplete,gbc);
    signUpComplete.setVisible(false);

    gbc.gridx=2;    gbc.gridy=3;    gbc.gridwidth=2;
    add(forgetPass,gbc);

    signUpBtn.addActionListener(this);
    loginBtn.addActionListener(this);
    backBtn.addActionListener(this);
    forgetPass.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            new ForgetPass(type);
            dispose();
        }
    });
    setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== loginBtn){
            login();
        } else if (e.getSource()==signUpBtn) {
            new SignUpScreen(type);
            dispose();
        } else if (e.getSource()==backBtn) {
            new Frame1();
            dispose();
        }
    }

    void login(){

        String username = loginFd.getText(),
                password = String.valueOf(passFd.getPassword());

        File read;
        if(this.type){
            read = new File("Student Login.txt");
        }else {
            read = new File("Admin Login.txt");
        }
        try {
            Scanner obj = new Scanner(read);
            while(obj.hasNext()){
                String name = obj.nextLine();
                String user = obj.nextLine();
                String pass = obj.nextLine();

                if(username.equals(user)){
                    if( pass.equals(password)) {
                        new AfterLogin(user,name,type);
                        dispose();
                    }else {
                        JOptionPane.showMessageDialog(null,
                                "Wrong Password\nTry again",
                                "Error message",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JOptionPane.showMessageDialog(null,
                "Login not found",
                "Error message",
                JOptionPane.ERROR_MESSAGE);
    }
}

