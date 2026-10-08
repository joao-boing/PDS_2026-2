package visao;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.UserDAO;
import modelo.User;

public class LoginWindow extends JFrame {

    private static final long serialVersionUID = 1L;

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

        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        ImageIcon icone = carregarIcone("/img/icone.png", 32, 32);
        if (icone != null) {
            setIconImage(icone.getImage());
        }

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon logo = carregarIcone("/img/logo.png", 240, 80);
        if (logo != null) {
            lblLogo.setIcon(logo);
        } else {
            lblLogo.setText("Loja de Hardware");
        }
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        contentPane.add(lblLogo, g);
        g.gridwidth = 1;

        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        g.gridx = 0; g.gridy = 1; g.weightx = 0; g.anchor = GridBagConstraints.EAST;
        contentPane.add(lblUsername, g);

        txtUsername = new JTextField(15);
        g.gridx = 1; g.weightx = 1;
        contentPane.add(txtUsername, g);

        JLabel lblPassword = new JLabel("Senha:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        contentPane.add(lblPassword, g);

        txtPassword = new JPasswordField(15);
        g.gridx = 1; g.weightx = 1;
        contentPane.add(txtPassword, g);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(e -> login());

        JButton btnExit = new JButton("Sair");
        btnExit.addActionListener(e -> System.exit(0));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.add(btnLogin);
        buttons.add(btnExit);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        contentPane.add(buttons, g);

        lblStatus = new JLabel(" ");
        g.gridy = 4;
        contentPane.add(lblStatus, g);

        getRootPane().setDefaultButton(btnLogin);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        txtUsername.requestFocusInWindow();
    }

    private ImageIcon carregarIcone(String caminho, int largura, int altura) {
        URL url = LoginWindow.class.getResource(caminho);
        if (url == null) {
            System.err.println("Imagem nao encontrada: " + caminho);
            return null;
        }
        ImageIcon original = new ImageIcon(url);
        double escala = Math.min((double) largura / original.getIconWidth(),
                (double) altura / original.getIconHeight());
        int w = (int) (original.getIconWidth() * escala);
        int h = (int) (original.getIconHeight() * escala);
        Image reduzida = original.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(reduzida);
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
