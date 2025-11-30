import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.PantallaInicio;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            PantallaInicio inicio = new PantallaInicio();
            inicio.setVisible(true);
        });
    }
}