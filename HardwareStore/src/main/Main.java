package main;

import java.awt.EventQueue;

import visao.LoginWindow;

/**
 * Ponto de entrada. Execute SEMPRE esta classe.
 *
 * O aplicativo comeca na tela de login. Apos um login com sucesso, a
 * propria LoginWindow abre a ProductWindow.
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginWindow window = new LoginWindow();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
