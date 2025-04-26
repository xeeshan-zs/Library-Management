import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ForgetPass extends JFrame implements ActionListener {

    boolean type;
    JPanel pan;
    JLabel userLbl,nameLbl,codeLbl,msgLbl;
    JTextField userFd,nameFd,codeFd;
    JButton okBtn;


    ForgetPass(boolean type){

        super("Forget password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 350);
        setLocationRelativeTo(null);

        this.type=type;
        setLayout(new BorderLayout());

        GridBagConstraints gbc  = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        pan = new JPanel(new GridBagLayout());

        userLbl = new JLabel("User Name:");
        userLbl.setForeground(Color.WHITE);
        nameLbl = new JLabel("Your Name:");
        nameLbl.setForeground(Color.WHITE);
        codeLbl = new JLabel("Recovery code:");
        codeLbl.setForeground(Color.WHITE);
        msgLbl = new JLabel("⚠️ You should know \"Recovery code\" to reset your password");
        msgLbl.setFont(new Font("Serif",Font.BOLD,16));
        msgLbl.setForeground(Color.red);

        userFd = new JTextField(15);
        nameFd = new JTextField(15);
        codeFd = new JTextField(15);

        okBtn = new JButton("  Ok  ");
        okBtn.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
        okBtn.setBackground(new Color(0xFFFFFF));
        okBtn.setForeground(new Color(2, 57, 60));
        okBtn.setFocusPainted(false);

        gbc.gridx=0;    gbc.gridy=1;    gbc.anchor=GridBagConstraints.EAST;
        pan.add(userLbl,gbc);

        gbc.gridx=0;    gbc.gridy=2;    gbc.anchor=GridBagConstraints.EAST;
        pan.add(nameLbl,gbc);

        gbc.gridx=0;    gbc.gridy=3;    gbc.anchor=GridBagConstraints.EAST;
        pan.add(codeLbl,gbc);

        gbc.gridx=1;    gbc.gridy=1;    gbc.gridwidth=4;
        pan.add(userFd,gbc);

        gbc.gridx=1;    gbc.gridy=2;    gbc.gridwidth=4;
        pan.add(nameFd,gbc);

        gbc.gridx=1;    gbc.gridy=3;    gbc.gridwidth=6;
        pan.add(codeFd,gbc);

        gbc.gridx=4;    gbc.gridy=4;    gbc.gridwidth=1;
        gbc.anchor = GridBagConstraints.CENTER;
        pan.add(okBtn,gbc);


        add(msgLbl,BorderLayout.NORTH);
        pan.setBackground(new Color(2, 57, 60));
        add(pan,BorderLayout.CENTER);
        setVisible(true);

        okBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==okBtn) {
            if(!allFilled()){
                JOptionPane.showMessageDialog(null,"Fill all fields","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(codeMatches()) {
                if(checkUser()){
                    String newPAss = JOptionPane.showInputDialog("Enter new password");
                    changePass(newPAss);

                }else {
                    JOptionPane.showMessageDialog(null, "This user does not exists in our record");

                }
            }else {
                JOptionPane.showMessageDialog(null,"Enter correct Recovery code","Recovery Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    boolean checkUser() {
        File read;
        if(this.type){
            read = new File("Student Login.txt");
        }else {
            read = new File("Admin Login.txt");
        }
        try {
            Scanner obj = new Scanner(read);
            while (obj.hasNextLine()) {
                obj.nextLine();
                String user = obj.nextLine();
                obj.nextLine();
                if (user.equals(userFd.getText())) {
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    void changePass(String newPass) {
        String[] name = new String[100], user = new String[100], pass = new String[100];
        int i = 0;
        File read;
        if(this.type){
            read = new File("Student Login.txt");
        }else {
            read = new File("Admin Login.txt");
        }
        try {
            Scanner obj = new Scanner(read);
            while (obj.hasNextLine()) {
                name[i] = obj.nextLine();
                user[i] = obj.nextLine();
                pass[i] = obj.nextLine();

                if (userFd.getText().equals(user[i])) {
                    pass[i] = newPass;
                }
                i++;
            }
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        try {
            FileWriter zout;
            if(this.type){
                zout = new FileWriter("Student Login.txt");
            }else {
                zout = new FileWriter("Admin Login.txt");
            }
            for (int j = 0; j < i; j++) {
                zout.write(name[j] + System.lineSeparator());
                zout.write(user[j] + System.lineSeparator());
                zout.write(pass[j] + System.lineSeparator());
            }
            zout.close();
            JOptionPane.showMessageDialog(null, "Password changed successfully ✅ ");
            new Frame1();
            dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    boolean codeMatches(){
        return codeFd.getText().equals("0000");
    }
    boolean allFilled() {
        return !userFd.getText().isEmpty() &&
                !nameFd.getText().isEmpty() &&
                !codeFd.getText().isEmpty();
    }
}