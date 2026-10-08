package visao;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import modelo.Product;

public class ProductTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private static final String[] COLUMNS = { "ID", "Nome do produto", "Marca", "Tipo", "Modelo", "Estoque" };

    private List<Product> data = new ArrayList<>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Product p = data.get(row);
        switch (column) {
            case 0: return p.getId();
            case 1: return p.getProductName();
            case 2: return p.getBrand();
            case 3: return p.getProductType();
            case 4: return p.getModel();
            case 5: return p.getStockQuantity();
            default: return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return (column == 0 || column == 5) ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void setData(List<Product> newData) {
        this.data = newData;
        fireTableDataChanged();
    }

    public Product getProduct(int row) {
        return data.get(row);
    }
}
