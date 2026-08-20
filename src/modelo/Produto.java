package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Produto {

    private int id;
    private String codigoBarras;
    private String marca;
    private String modelo;
    private String categoria;
    private BigDecimal preco;
    private int quantidadeEstoque;
    private Timestamp dataCadastro;

    public Produto() {
    }

    public Produto(String codigoBarras, String marca, String modelo, String categoria,
                    BigDecimal preco, int quantidadeEstoque) {
        this.codigoBarras = codigoBarras;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Timestamp getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Timestamp dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return marca + " " + modelo;
    }
}
