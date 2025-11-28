import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MenuPrincipal mp = new MenuPrincipal();
            mp.setVisible(true);
        });
    }
}