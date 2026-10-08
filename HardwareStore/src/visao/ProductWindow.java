package visao;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import dao.ProductDAO;
import modelo.Images;
import modelo.Product;

public class ProductWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int PHOTO_SIZE = 150;

    private final ProductDAO dao = new ProductDAO();

    private ProductTableModel tableModel = new ProductTableModel();
    private JTable table = new JTable(tableModel);

    private JButton btnNew = new JButton("Novo");
    private JButton btnEdit = new JButton("Editar");
    private JButton btnDelete = new JButton("Excluir");
    private JTextField txtSearch = new JTextField(14);
    private JComboBox<String> cbType = new JComboBox<>();
    private JPanel summaryPanel = new JPanel(new GridLayout(1, 0, 8, 0));

    private CardLayout detailCards = new CardLayout();
    private JPanel detailPanel = new JPanel(detailCards);
    private JLabel lblName = new JLabel();
    private JLabel lblBrand = new JLabel();
    private JLabel lblType = new JLabel();
    private JLabel lblModel = new JLabel();
    private JLabel lblStock = new JLabel();
    private JLabel lblPhotoView = new JLabel();
    private JLabel lblPhotoEdit = new JLabel();
    private JTextField txtProductName = new JTextField();
    private JTextField txtBrand = new JTextField();
    private JTextField txtProductType = new JTextField();
    private JTextField txtModel = new JTextField();
    private JTextField txtStockQuantity = new JTextField();
    private JButton btnChoosePhoto = new JButton("Escolher foto");
    private JButton btnSave = new JButton("Salvar");
    private JButton btnCancel = new JButton("Cancelar");

    private JLabel lblStatus = new JLabel(" Pronto.");

    private boolean reloading = false;
    private boolean editing = false;
    private int editingId = 0;
    private byte[] chosenPhoto;
    private Timer searchTimer;

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
        setSize(950, 580);
        setMinimumSize(new Dimension(800, 540));
        setLocationRelativeTo(null);

        URL urlIcone = ProductWindow.class.getResource("/img/icone.png");
        if (urlIcone != null) {
            setIconImage(new ImageIcon(urlIcone).getImage());
        }
        setLayout(new BorderLayout(6, 6));

        summaryPanel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        JPanel top = new JPanel(new BorderLayout());
        top.add(createCommandBar(), BorderLayout.NORTH);
        top.add(summaryPanel, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        table.setRowHeight(22);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        add(createDetailPanel(), BorderLayout.EAST);
        add(lblStatus, BorderLayout.SOUTH);

        btnNew.addActionListener(e -> newProduct());
        btnEdit.addActionListener(e -> enterEdit(getSelectedProduct()));
        btnDelete.addActionListener(e -> delete());
        btnChoosePhoto.addActionListener(e -> choosePhoto());
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());
        cbType.addActionListener(e -> {
            if (!reloading) {
                refreshScreen(selectedId());
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !reloading && !editing) {
                showSelected();
            }
        });

        searchTimer = new Timer(300, e -> refreshScreen(selectedId()));
        searchTimer.setRepeats(false);
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchChanged(); }
            public void removeUpdate(DocumentEvent e) { searchChanged(); }
            public void changedUpdate(DocumentEvent e) { searchChanged(); }
        });

        refreshScreen(0);
    }

    private JPanel createCommandBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        bar.add(btnNew);
        bar.add(btnEdit);
        bar.add(btnDelete);
        bar.add(Box.createHorizontalStrut(16));
        bar.add(new JLabel("Buscar:"));
        bar.add(txtSearch);
        bar.add(new JLabel("Tipo:"));
        bar.add(cbType);
        return bar;
    }

    private JPanel createDetailPanel() {
        detailPanel.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        detailPanel.setPreferredSize(new Dimension(280, 0));
        detailPanel.add(createEmptyCard(), "vazio");
        detailPanel.add(createViewCard(), "ver");
        detailPanel.add(createEditCard(), "editar");
        return detailPanel;
    }

    private JPanel createEmptyCard() {
        JLabel msg = new JLabel("<html><center>Selecione um produto<br>na tabela.</center></html>",
                SwingConstants.CENTER);
        msg.setForeground(Color.GRAY);
        JPanel p = new JPanel(new BorderLayout());
        p.add(msg, BorderLayout.CENTER);
        return p;
    }

    private JPanel createViewCard() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);

        setupPhotoLabel(lblPhotoView);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        p.add(lblPhotoView, g);
        g.gridwidth = 1;

        String[] labels = { "Nome:", "Marca:", "Tipo:", "Modelo:", "Estoque:" };
        JLabel[] values = { lblName, lblBrand, lblType, lblModel, lblStock };
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i + 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
            g.anchor = GridBagConstraints.EAST;
            p.add(new JLabel(labels[i]), g);

            values[i].setFont(values[i].getFont().deriveFont(Font.BOLD));
            g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
            g.anchor = GridBagConstraints.WEST;
            p.add(values[i], g);
        }
        g.gridx = 0; g.gridy = labels.length + 1; g.gridwidth = 2; g.weighty = 1;
        g.fill = GridBagConstraints.BOTH;
        p.add(new JPanel(), g);
        return p;
    }

    private JPanel createEditCard() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);

        setupPhotoLabel(lblPhotoEdit);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        p.add(lblPhotoEdit, g);
        g.gridy = 1;
        p.add(btnChoosePhoto, g);
        g.gridwidth = 1;
        g.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = { "Nome:", "Marca:", "Tipo:", "Modelo:", "Estoque:" };
        JTextField[] fields = { txtProductName, txtBrand, txtProductType, txtModel, txtStockQuantity };
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i + 2; g.weightx = 0; g.anchor = GridBagConstraints.EAST;
            p.add(new JLabel(labels[i]), g);
            g.gridx = 1; g.weightx = 1;
            p.add(fields[i], g);
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttons.add(btnSave);
        buttons.add(btnCancel);
        g.gridx = 0; g.gridy = labels.length + 2; g.gridwidth = 2;
        p.add(buttons, g);

        g.gridy = labels.length + 3; g.weighty = 1; g.fill = GridBagConstraints.BOTH;
        p.add(new JPanel(), g);
        return p;
    }

    private void setupPhotoLabel(JLabel label) {
        label.setPreferredSize(new Dimension(PHOTO_SIZE, PHOTO_SIZE));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    private void searchChanged() {
        if (!reloading) {
            searchTimer.restart();
        }
    }

    private void refreshScreen(int idToSelect) {
        reloading = true;
        try {
            String type = getSelectedType();
            loadTypes(dao.listTypes(), type);

            List<Product> list = dao.listFiltered(getSelectedType(), txtSearch.getText().trim());
            tableModel.setData(list);
            loadSummary(dao.countByType());
            selectById(idToSelect);
            lblStatus.setText(" " + list.size() + " produto(s) em exibicao.");
        } catch (SQLException ex) {
            error("Erro ao carregar produtos", ex);
        } finally {
            reloading = false;
        }
        showSelected();
    }

    private void showSelected() {
        Product p = getSelectedProduct();
        if (p == null) {
            showEmpty();
        } else {
            showDetails(p);
        }
        updateButtons(p != null);
    }

    private void showEmpty() {
        detailPanel.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        detailCards.show(detailPanel, "vazio");
    }

    private void showDetails(Product p) {
        lblName.setText(p.getProductName());
        lblBrand.setText(p.getBrand());
        lblType.setText(p.getProductType());
        lblModel.setText(p.getModel());
        lblStock.setText(String.valueOf(p.getStockQuantity()));
        try {
            showPhoto(lblPhotoView, Images.fromBytes(dao.findPhoto(p.getId())));
        } catch (SQLException | IOException ex) {
            showPhoto(lblPhotoView, null);
            lblStatus.setText(" Erro ao carregar a foto: " + ex.getMessage());
        }
        detailPanel.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        detailCards.show(detailPanel, "ver");
    }

    private void updateButtons(boolean hasSelection) {
        btnNew.setEnabled(!editing);
        btnEdit.setEnabled(hasSelection && !editing);
        btnDelete.setEnabled(hasSelection && !editing);
        table.setEnabled(!editing);
        txtSearch.setEnabled(!editing);
        cbType.setEnabled(!editing);
    }

    private void newProduct() {
        table.clearSelection();
        enterEdit(null);
    }

    private void enterEdit(Product p) {
        editing = true;
        editingId = (p == null) ? 0 : p.getId();
        txtProductName.setText(p == null ? "" : p.getProductName());
        txtBrand.setText(p == null ? "" : p.getBrand());
        txtProductType.setText(p == null ? "" : p.getProductType());
        txtModel.setText(p == null ? "" : p.getModel());
        txtStockQuantity.setText(p == null ? "" : String.valueOf(p.getStockQuantity()));

        chosenPhoto = null;
        try {
            if (p != null) {
                chosenPhoto = dao.findPhoto(p.getId());
            }
            showPhoto(lblPhotoEdit, Images.fromBytes(chosenPhoto));
        } catch (SQLException | IOException ex) {
            error("Erro ao carregar a foto", ex);
        }

        detailPanel.setBorder(BorderFactory.createTitledBorder(p == null ? "Novo produto" : "Editar produto"));
        detailCards.show(detailPanel, "editar");
        updateButtons(p != null);
        txtProductName.requestFocusInWindow();
    }

    private void choosePhoto() {
        File file = chooseFile();
        if (file == null) {
            return;
        }
        try {
            chosenPhoto = Images.readReduced(file);
            showPhoto(lblPhotoEdit, Images.fromBytes(chosenPhoto));
        } catch (IOException ex) {
            error("Erro ao ler a imagem", ex);
        }
    }

    private void save() {
        Product p = readForm();
        if (p == null) {
            return;
        }
        p.setId(editingId);
        p.setPhoto(chosenPhoto);
        try {
            if (p.getId() == 0) {
                dao.insert(p);
            } else {
                dao.update(p);
            }
            editing = false;
            refreshScreen(p.getId());
            lblStatus.setText(" Salvo: " + p.getProductName());
        } catch (SQLException ex) {
            error("Erro ao salvar produto", ex);
        }
    }

    private void cancel() {
        editing = false;
        showSelected();
    }

    private void delete() {
        Product p = getSelectedProduct();
        if (p == null) {
            return;
        }
        int option = JOptionPane.showConfirmDialog(this,
                "Excluir o produto " + p.getProductName() + "?", "Confirmacao",
                JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.delete(p.getId());
            refreshScreen(0);
            lblStatus.setText(" Produto excluido.");
        } catch (SQLException ex) {
            error("Erro ao excluir produto", ex);
        }
    }

    private Product readForm() {
        String productName = txtProductName.getText().trim();
        String brand = txtBrand.getText().trim();
        String productType = txtProductType.getText().trim();
        String model = txtModel.getText().trim();

        int stockQuantity;
        try {
            stockQuantity = Integer.parseInt(txtStockQuantity.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade em estoque deve ser um numero inteiro!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            txtStockQuantity.requestFocus();
            return null;
        }

        Product p = new Product(productName, brand, productType, model, stockQuantity);

        String problem = p.validate();
        if (problem != null) {
            JOptionPane.showMessageDialog(this, problem, "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return p;
    }

    private File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Escolher foto");
        chooser.setFileFilter(new FileNameExtensionFilter("Imagens (jpg, jpeg, png)", "jpg", "jpeg", "png"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private void showPhoto(JLabel label, BufferedImage img) {
        if (img == null) {
            label.setIcon(null);
            label.setText("sem foto");
            return;
        }
        Image small = (img.getWidth() >= img.getHeight())
                ? img.getScaledInstance(PHOTO_SIZE, -1, Image.SCALE_SMOOTH)
                : img.getScaledInstance(-1, PHOTO_SIZE, Image.SCALE_SMOOTH);
        label.setText("");
        label.setIcon(new ImageIcon(small));
    }

    private void loadTypes(List<String> types, String keep) {
        cbType.removeAllItems();
        cbType.addItem("(todas)");
        for (String t : types) {
            cbType.addItem(t);
        }
        if (keep != null) {
            cbType.setSelectedItem(keep);
        }
    }

    private void loadSummary(Map<String, Integer> totals) {
        summaryPanel.removeAll();
        for (Map.Entry<String, Integer> e : totals.entrySet()) {
            JLabel number = new JLabel(String.valueOf(e.getValue()), SwingConstants.CENTER);
            number.setFont(number.getFont().deriveFont(Font.BOLD, 22f));
            JLabel name = new JLabel(e.getKey(), SwingConstants.CENTER);

            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            card.add(number, BorderLayout.CENTER);
            card.add(name, BorderLayout.SOUTH);
            summaryPanel.add(card);
        }
        summaryPanel.revalidate();
        summaryPanel.repaint();
    }

    private String getSelectedType() {
        Object sel = cbType.getSelectedItem();
        return (sel == null) ? "" : sel.toString();
    }

    private Product getSelectedProduct() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        return tableModel.getProduct(table.convertRowIndexToModel(viewRow));
    }

    private int selectedId() {
        Product p = getSelectedProduct();
        return (p == null) ? 0 : p.getId();
    }

    private void selectById(int id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getProduct(i).getId() == id) {
                int viewRow = table.convertRowIndexToView(i);
                table.setRowSelectionInterval(viewRow, viewRow);
                table.scrollRectToVisible(table.getCellRect(viewRow, 0, true));
                return;
            }
        }
        table.clearSelection();
    }

    private void error(String context, Exception ex) {
        JOptionPane.showMessageDialog(this, context + ": " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        lblStatus.setText(" " + context + ".");
    }
}
