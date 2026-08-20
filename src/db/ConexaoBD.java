package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por abrir a conexão com o banco de dados MySQL.
 *
 * IMPORTANTE:
 * - Ajuste USUARIO e SENHA conforme as credenciais que você recebeu
 *   (ex: usuário "aluno_cd" e a senha correspondente).
 * - Se o banco estiver em outro computador, troque "localhost" pelo
 *   IP ou nome do servidor.
 * - É necessário ter o driver MySQL Connector/J no classpath do projeto
 *   (veja o arquivo LEIA-ME.md para o passo a passo no Eclipse).
 */
public class ConexaoBD {

    // Endereço do banco. Troque "loja_hardware" se o banco tiver outro nome.
    private static final String URL =
            "jdbc:mysql://localhost:3306/loja_hardware?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // Credenciais de acesso ao MySQL
    private static final String USUARIO = "aluno_cd";
    private static final String SENHA = "aluno_pw";

    /**
     * Abre e retorna uma conexão com o banco.
     * Lança RuntimeException com a mensagem original do MySQL em caso de falha
     * (usuário/senha errados, banco inexistente, servidor fora do ar, etc).
     */
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Método só para testar a conexão rapidamente.
     * Clique com o botão direito nesta classe -> Run As -> Java Application.
     *
     * Se aparecer "Conectado com sucesso!" está tudo certo.
     * Se der erro, a mensagem já vai indicar o motivo (senha errada,
     * usuário sem permissão no banco, MySQL não está rodando, etc).
     */
    public static void main(String[] args) {
        try (Connection con = conectar()) {
            System.out.println("Conectado com sucesso!");
            System.out.println("URL: " + con.getMetaData().getURL());
            System.out.println("Usuário: " + con.getMetaData().getUserName());
        } catch (Exception e) {
            System.out.println("Falha na conexão:");
            e.printStackTrace();
        }
    }
}