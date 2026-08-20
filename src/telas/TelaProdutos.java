package telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ProdutoDAO;
import modelo.Produto;

/**
 * Tela inicial simples listando os produtos em uma JTable.
 *
 * Abra este arquivo no Eclipse com "Open With > WindowBuilder Editor"
 * para editar visualmente (adicionar botões de Novo/Editar/Excluir,
 * campo de busca, etc). O código de acesso ao banco já está pronto
 * na classe ProdutoDAO, então na tela você só precisa chamar os métodos.
 */
public class TelaProdutos extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel modeloTabela;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TelaProdutos frame = new TelaProdutos();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TelaProdutos() {
        setTitle("Loja de Hardware - Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 450);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        String[] colunas = { "ID", "Código de Barras", "Marca", "Modelo", "Categoria", "Preço", "Estoque" };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabela somente leitura
            }
        };

        table = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        carregarProdutos();
    }

    private void carregarProdutos() {
        modeloTabela.setRowCount(0); // limpa a tabela antes de recarregar

        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.listarTodos();

        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[] {
                    p.getId(),
                    p.getCodigoBarras(),
                    p.getMarca(),
                    p.getModelo(),
                    p.getCategoria(),
                    p.getPreco(),
                    p.getQuantidadeEstoque()
            });
        }
    }
}
