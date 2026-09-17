package visao;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.UserDAO;
import modelo.User;

public class LoginWindow extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblStatus;

    private final UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginWindow frame = new LoginWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LoginWindow() {
        setTitle("Loja de Hardware - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 340, 230);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblUsername.setBounds(30, 30, 90, 25);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(130, 30, 160, 25);
        contentPane.add(txtUsername);

        JLabel lblPassword = new JLabel("Senha:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPassword.setBounds(30, 70, 90, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 70, 160, 25);
        contentPane.add(txtPassword);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        btnLogin.setBounds(30, 115, 120, 30);
        contentPane.add(btnLogin);

        JButton btnExit = new JButton("Sair");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnExit.setBounds(170, 115, 120, 30);
        contentPane.add(btnExit);

        lblStatus = new JLabel(" ");
        lblStatus.setBounds(30, 155, 280, 25);
        contentPane.add(lblStatus);

        txtUsername.requestFocusInWindow();
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Preencha usuario e senha.");
            return;
        }

        try {
            User user = userDAO.authenticate(username, password);
            if (user == null) {
                lblStatus.setText("Usuario ou senha invalidos.");
                txtPassword.setText("");
                return;
            }
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + user.getUsername() + "!");
            new ProductWindow().setVisible(true);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao fazer login: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
