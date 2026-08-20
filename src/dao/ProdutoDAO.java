package dao;

import db.ConexaoBD;
import modelo.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por toda a comunicação com a tabela "produtos".
 * Use estes métodos a partir da tela criada no WindowBuilder
 * (ex: no clique de um botão "Buscar", "Salvar", "Excluir", etc).
 */
public class ProdutoDAO {

    // Insere um novo produto
    public void inserir(Produto p) {
        String sql = "INSERT INTO produtos (codigo_barras, marca, modelo, categoria, preco, quantidade_estoque) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBD.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigoBarras());
            stmt.setString(2, p.getMarca());
            stmt.setString(3, p.getModelo());
            stmt.setString(4, p.getCategoria());
            stmt.setBigDecimal(5, p.getPreco());
            stmt.setInt(6, p.getQuantidadeEstoque());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto: " + e.getMessage(), e);
        }
    }

    // Atualiza um produto existente (usando o id)
    public void atualizar(Produto p) {
        String sql = "UPDATE produtos SET codigo_barras = ?, marca = ?, modelo = ?, "
                + "categoria = ?, preco = ?, quantidade_estoque = ? WHERE id = ?";

        try (Connection con = ConexaoBD.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, p.getCodigoBarras());
            stmt.setString(2, p.getMarca());
            stmt.setString(3, p.getModelo());
            stmt.setString(4, p.getCategoria());
            stmt.setBigDecimal(5, p.getPreco());
            stmt.setInt(6, p.getQuantidadeEstoque());
            stmt.setInt(7, p.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto: " + e.getMessage(), e);
        }
    }

    // Exclui um produto pelo id
    public void excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection con = ConexaoBD.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir produto: " + e.getMessage(), e);
        }
    }

    // Retorna todos os produtos cadastrados (para popular a JTable, por exemplo)
    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY marca, modelo";

        try (Connection con = ConexaoBD.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProduto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }

        return lista;
    }

    // Busca um produto pelo código de barras (ideal para leitor de código de barras)
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT * FROM produtos WHERE codigo_barras = ?";

        try (Connection con = ConexaoBD.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, codigoBarras);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
        }

        return null;
    }

    // Busca produtos por marca ou modelo (para uma barra de pesquisa)
    public List<Produto> buscarPorMarcaOuModelo(String termo) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE marca LIKE ? OR modelo LIKE ? ORDER BY marca";

        try (Connection con = ConexaoBD.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            String like = "%" + termo + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProduto(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos: " + e.getMessage(), e);
        }

        return lista;
    }

    // Converte uma linha do ResultSet em um objeto Produto
    private Produto mapearProduto(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setCodigoBarras(rs.getString("codigo_barras"));
        p.setMarca(rs.getString("marca"));
        p.setModelo(rs.getString("modelo"));
        p.setCategoria(rs.getString("categoria"));
        p.setPreco(rs.getBigDecimal("preco"));
        p.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
        p.setDataCadastro(rs.getTimestamp("data_cadastro"));
        return p;
    }
}
