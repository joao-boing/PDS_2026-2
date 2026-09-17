package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hardware_store";
    private static final String USER = "store_user";
    private static final String PASSWORD = "store_pw";

    // Abre uma conexao NOVA. Quem chamou e responsavel por fechar.
    public static Connection open() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Teste de ambiente: rode esta classe quando quiser saber se o
    // problema esta na sua maquina e nao no seu codigo.
    public static void main(String[] args) {
        try (Connection con = open()) {
            System.out.println("Conexao estabelecida.");
            System.out.println("Servidor : " + con.getMetaData().getDatabaseProductName());
            System.out.println("Banco    : " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("Falha na conexao: " + e.getMessage());
        }
    }
}
