package modelo;

public class Product {
    private int id;
    private String productName;
    private String brand;
    private String productType;
    private String model;
    private int stockQuantity;

    // Produto NOVO: o id fica 0 porque quem gera o id e o banco.
    public Product(String productName, String brand, String productType, String model, int stockQuantity) {
        this(0, productName, brand, productType, model, stockQuantity);
    }

    // Produto que VEIO do banco: o id ja existe.
    public Product(int id, String productName, String brand, String productType, String model, int stockQuantity) {
        this.id = id;
        this.productName = productName;
        this.brand = brand;
        this.productType = productType;
        this.model = model;
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * AS REGRAS DESTE PRODUTO, sozinho.
     *
     * Devolve null quando esta tudo certo, ou o texto do problema.
     * Assim quem chama so precisa testar se veio null, e a mensagem
     * ja vem pronta para ser exibida.
     */
    public String validate() {
        if (productName == null || productName.trim().isEmpty()) {
            return "Preencha o nome do produto.";
        }
        if (brand == null || brand.trim().isEmpty()) {
            return "Preencha a marca.";
        }
        if (productType == null || productType.trim().isEmpty()) {
            return "Preencha o tipo do produto.";
        }
        if (model == null || model.trim().isEmpty()) {
            return "Preencha o modelo.";
        }
        if (stockQuantity < 0) {
            return "A quantidade em estoque nao pode ser negativa.";
        }
        return null; // null significa "sem problema"
    }

    @Override
    public String toString() {
        return String.format("Product[id=%d, name=%s, brand=%s, type=%s, model=%s, stock=%d]",
                id, productName, brand, productType, model, stockQuantity);
    }
}
