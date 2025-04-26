import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame1 extends JFrame implements ActionListener {
    JButton adminBtn, stdBtn;

    Frame1(){
        super("Library System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800,450);
        setLocationRelativeTo(null);
        setResizable(false);


        ImageIcon i1 = new ImageIcon(".\\app\\resources\\assets\\img.png");
        JLabel img = new JLabel(i1);
        img.setLayout(null);
        add(img,BorderLayout.CENTER);


        adminBtn = new JButton("Admin Login") ;
        adminBtn.setBounds(43,300,120,25);
        adminBtn.setBackground(Color.WHITE);
        adminBtn.setFocusPainted(false);

        stdBtn = new JButton("Student Login");
        stdBtn.setFocusPainted(false);
        stdBtn.setBackground(Color.WHITE);
        stdBtn.setBounds(220,300,120,25);
        img.add(adminBtn) ;
        img.add(stdBtn);

        adminBtn.addActionListener(this);
        stdBtn.addActionListener(this);


        setVisible(true);

    }

    public static void main(String[] args) {
        new Frame1();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== stdBtn){
            new Login(true);
            dispose();
        } else if (e.getSource()==adminBtn) {
            new Login(false);
            dispose();
        }
    }
}
