import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AfterLogin extends JFrame implements ActionListener {

    String Uname;
    boolean type;
    JLabel topLbl, wlcmLbl;
    JButton addBookBtn, viewAllBooksBtn, issueBtn, updateBookBtn, viewIssuedBooksBtn, returnBookBtn, exitBtn, logoutBtn;

    AfterLogin(String Uname, String name, boolean type) {
        super("After login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0, 18, 17));
        setLayout(new GridBagLayout());

        this.Uname=Uname;
        this.type=type;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        topLbl = new JLabel("Library Management System");
        topLbl.setForeground(Color.white);
        topLbl.setFont(new Font("Times New Roman", Font.BOLD, 30));

        wlcmLbl = new JLabel("Welcome " + name);
        wlcmLbl.setForeground(Color.white);
        wlcmLbl.setFont(new Font("Times New Roman", Font.BOLD, 25));

        addBookBtn = new JButton("Add Book");
        viewAllBooksBtn = new JButton("View All Books");
        issueBtn = new JButton("Issue");
        updateBookBtn = new JButton("Update Book Record");
        viewIssuedBooksBtn = new JButton("View Issued Books");
        returnBookBtn = new JButton("Return Book");

        exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        exitBtn.setFocusPainted(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setBackground(new Color(0, 2, 18));
        exitBtn.setForeground(Color.WHITE);

        logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setBackground(new Color(0, 2, 18));
        logoutBtn.setForeground(Color.WHITE);

        Font buttonFont = new Font("Times New Roman", Font.PLAIN, 16);
        addBookBtn.setFont(buttonFont);
        viewAllBooksBtn.setFont(buttonFont);
        issueBtn.setFont(buttonFont);
        updateBookBtn.setFont(buttonFont);
        viewIssuedBooksBtn.setFont(buttonFont);
        returnBookBtn.setFont(buttonFont);

        JButton[] buttons = {addBookBtn, viewAllBooksBtn, issueBtn, updateBookBtn, viewIssuedBooksBtn, returnBookBtn};
        for (JButton btn : buttons) {
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBackground(new Color(0, 2, 18));
            btn.setForeground(Color.WHITE);
            btn.addActionListener(this);
        }

        if (type) {
            addBookBtn.setEnabled(false);
            updateBookBtn.setEnabled(false);
            viewIssuedBooksBtn.setEnabled(false);
        } else {
            issueBtn.setEnabled(false);
            returnBookBtn.setEnabled(false);
        }

        exitBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        gbc.gridx = gbc.gridy = 0;
        add(topLbl, gbc);

        gbc.gridy = 1;
        add(wlcmLbl, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBackground(new Color(0, 2, 18));

        buttonPanel.add(addBookBtn);
        buttonPanel.add(viewAllBooksBtn);
        buttonPanel.add(issueBtn);
        buttonPanel.add(updateBookBtn);
        buttonPanel.add(viewIssuedBooksBtn);
        buttonPanel.add(returnBookBtn);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.LINE_AXIS));
        footerPanel.setBackground(new Color(0, 2, 18));

        footerPanel.add(logoutBtn);
        footerPanel.add(exitBtn);

        gbc.gridy = 2;
        gbc.gridheight = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(buttonPanel, gbc);

        gbc.gridy = 8;
        add(footerPanel, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn) {
            System.exit(0);
        } else if (e.getSource() == logoutBtn) {
            dispose();
            new Frame1();
        } else if (e.getSource()==addBookBtn) {
            new AddFrame();
        } else if (e.getSource()==viewAllBooksBtn||e.getSource()==updateBookBtn) {
            new ViewFrame(Uname,type);
        } else if (e.getSource()==viewIssuedBooksBtn) {
            new ViewAllIssuedBooksFrame();
        }else if (e.getSource()==issueBtn){
            new ViewFrame(Uname,type);
        } else if (e.getSource()==returnBookBtn) {
            new ReturnBookFrame(Uname);
        }
    }


}
