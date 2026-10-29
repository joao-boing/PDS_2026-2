package visao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

public class JanelaManual extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO = "/manual/manual.html";

    public JanelaManual(JFrame dono) {
        super(dono, "Manual de uso", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 460);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(dono);

        JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JEditorPane editor = new JEditorPane();
        editor.setEditable(false);
        carregar(editor);
        contentPane.add(new JScrollPane(editor), BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotao.add(btnFechar);
        contentPane.add(painelBotao, BorderLayout.SOUTH);

        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void carregar(JEditorPane editor) {
        URL url = JanelaManual.class.getResource(ARQUIVO);
        if (url == null) {
            editor.setContentType("text/plain");
            editor.setText("Manual nao encontrado: " + ARQUIVO);
            return;
        }
        try {
            editor.setPage(url);
        } catch (IOException erro) {
            editor.setContentType("text/plain");
            editor.setText("Nao foi possivel abrir o manual: " + erro.getMessage());
        }
    }
}