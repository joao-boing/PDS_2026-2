package visao;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import dao.ProductDAO;
import modelo.Product;

public class ProductWindow extends JFrame {

    private JPanel contentPane;
    private JTextField txtProductName;
    private JTextField txtBrand;
    private JTextField txtProductType;
    private JTextField txtModel;
    private JTextField txtStockQuantity;
    private JTextField txtSearch;
    private JTable table;
    private JLabel lblStatus;

    private DefaultTableModel tableModel;
    private final ProductDAO dao = new ProductDAO();
    private int selectedId = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProductWindow frame = new ProductWindow();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ProductWindow() {
        setTitle("Loja de Hardware - Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 550);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblProductName = new JLabel("Nome do produto:");
        lblProductName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblProductName.setBounds(30, 30, 120, 25);
        contentPane.add(lblProductName);

        txtProductName = new JTextField();
        txtProductName.setBounds(160, 30, 220, 25);
        contentPane.add(txtProductName);

        JLabel lblBrand = new JLabel("Marca:");
        lblBrand.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblBrand.setBounds(30, 70, 120, 25);
        contentPane.add(lblBrand);

        txtBrand = new JTextField();
        txtBrand.setBounds(160, 70, 220, 25);
        contentPane.add(txtBrand);

        JLabel lblProductType = new JLabel("Tipo:");
        lblProductType.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblProductType.setBounds(30, 110, 120, 25);
        contentPane.add(lblProductType);

        txtProductType = new JTextField();
        txtProductType.setBounds(160, 110, 220, 25);
        contentPane.add(txtProductType);

        JLabel lblModel = new JLabel("Modelo:");
        lblModel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblModel.setBounds(30, 150, 120, 25);
        contentPane.add(lblModel);

        txtModel = new JTextField();
        txtModel.setBounds(160, 150, 220, 25);
        contentPane.add(txtModel);

        JLabel lblStockQuantity = new JLabel("Quantidade em estoque:");
        lblStockQuantity.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblStockQuantity.setBounds(30, 190, 170, 25);
        contentPane.add(lblStockQuantity);

        txtStockQuantity = new JTextField();
        txtStockQuantity.setBounds(210, 190, 100, 25);
        contentPane.add(txtStockQuantity);

        JLabel lblSearch = new JLabel("Buscar:");
        lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblSearch.setBounds(430, 30, 70, 25);
        contentPane.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setBounds(500, 30, 220, 25);
        contentPane.add(txtSearch);

        JButton btnRegister = new JButton("Cadastrar");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        btnRegister.setBounds(30, 235, 110, 30);
        contentPane.add(btnRegister);

        JButton btnUpdate = new JButton("Alterar");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        btnUpdate.setBounds(150, 235, 110, 30);
        contentPane.add(btnUpdate);

        JButton btnDelete = new JButton("Excluir");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        btnDelete.setBounds(270, 235, 110, 30);
        contentPane.add(btnDelete);

        JButton btnClear = new JButton("Limpar");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btnClear.setBounds(390, 235, 110, 30);
        contentPane.add(btnClear);

        JButton btnListAll = new JButton("Listar todos");
        btnListAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText("");
                listProducts();
            }
        });
        btnListAll.setBounds(510, 235, 120, 30);
        contentPane.add(btnListAll);

        JButton btnSearch = new JButton("Buscar");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        btnSearch.setBounds(640, 235, 110, 30);
        contentPane.add(btnSearch);

        table = new JTable();
        tableModel = new DefaultTableModel(
                new String[] { "ID", "Nome do produto", "Marca", "Tipo", "Modelo", "Estoque" }, 0);
        table.setModel(tableModel);
        table.setRowHeight(22);
        table.setDefaultEditor(Object.class, null);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    loadSelected();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 285, 780, 190);
        contentPane.add(scrollPane);

        lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 485, 600, 25);
        contentPane.add(lblStatus);

        listProducts();
    }

    private void listProducts() {
        try {
            fillTable(dao.list());
        } catch (SQLException ex) {
            error("Erro ao listar produtos", ex);
        }
    }

    private void search() {
        try {
            fillTable(dao.searchByName(txtSearch.getText().trim()));
        } catch (SQLException ex) {
            error("Erro ao buscar produtos", ex);
        }
    }

    private void register() {
        Product p = readForm();
        if (p == null) {
            return;
        }
        try {
            dao.insert(p);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com o id " + p.getId() + ".");
            clear();
            listProducts();
        } catch (SQLException ex) {
            error("Erro ao cadastrar produto", ex);
        }
    }

    private void update() {
        if (selectedId == 0) {
            JOptionPane.showMessageDialog(this, "Selecione primeiro uma linha da tabela.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Product p = readForm();
        if (p == null) {
            return;
        }
        // O id vem da SELECAO, nao do que esta digitado.
        p.setId(selectedId);
        try {
            dao.update(p);
            JOptionPane.showMessageDialog(this, "Produto alterado.");
            clear();
            listProducts();
        } catch (SQLException ex) {
            error("Erro ao alterar produto", ex);
        }
    }

    private void delete() {
        if (selectedId == 0) {
            JOptionPane.showMessageDialog(this, "Selecione primeiro uma linha da tabela.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int option = JOptionPane.showConfirmDialog(this,
                "Excluir o produto " + txtProductName.getText() + "?", "Confirmacao",
                JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.delete(selectedId);
            JOptionPane.showMessageDialog(this, "Produto excluido.");
            clear();
            listProducts();
        } catch (SQLException ex) {
            error("Erro ao excluir produto", ex);
        }
    }

    private void fillTable(List<Product> list) {
        tableModel.setRowCount(0);
        for (Product p : list) {
            tableModel.addRow(new Object[] {
                    p.getId(), p.getProductName(), p.getBrand(), p.getProductType(),
                    p.getModel(), p.getStockQuantity()
            });
        }
        lblStatus.setText(list.size() + " produto(s) na tabela.");
    }

    private Product readForm() {
        String productName = txtProductName.getText().trim();
        String brand = txtBrand.getText().trim();
        String productType = txtProductType.getText().trim();
        String model = txtModel.getText().trim();

        // CONVERSAO: tudo que vem de um JTextField e TEXTO, mesmo que
        // pareca numero. Converter e trabalho de quem faz fronteira com a
        // tela - por isso este try/catch fica aqui, e nao no Model.
        int stockQuantity;
        try {
            stockQuantity = Integer.parseInt(txtStockQuantity.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade em estoque deve ser um numero inteiro!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            txtStockQuantity.requestFocus();
            return null;
        }

        // Objeto NOVO a cada cadastro: reaproveitar um unico faria o
        // segundo sobrescrever o primeiro.
        Product p = new Product(productName, brand, productType, model, stockQuantity);

        String problem = p.validate();
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return p;
    }

    private void clear() {
        selectedId = 0;
        txtProductName.setText("");
        txtBrand.setText("");
        txtProductType.setText("");
        txtModel.setText("");
        txtStockQuantity.setText("");
        table.clearSelection();
        txtProductName.requestFocus();
        lblStatus.setText("Formulario limpo.");
    }

    private void error(String context, SQLException ex) {
        JOptionPane.showMessageDialog(this, context + ": " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        lblStatus.setText(context + ".");
    }

    private void loadSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }

        selectedId = (int) tableModel.getValueAt(row, 0);

        txtProductName.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtBrand.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtProductType.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        txtModel.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        txtStockQuantity.setText(String.valueOf(tableModel.getValueAt(row, 5)));

        lblStatus.setText("Editando o produto de id " + selectedId + ". Altere os campos e clique em Alterar.");
    }
}
