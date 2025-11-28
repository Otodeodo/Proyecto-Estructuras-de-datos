package ui.theme;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Theme {
    // Paleta "Taquería"
    // Fondo: Maíz / Tortilla suave
    public static final Color BG = new Color(255, 250, 225); 
    // Tarjetas: Blanco hueso
    public static final Color CARD_BG = new Color(255, 255, 245);
    // Bordes: Café quemado
    public static final Color CARD_BORDER = new Color(100, 50, 20);
    // Acento: Rojo Salsa / Tomate
    public static final Color ACCENT = new Color(210, 40, 40);
    // Acento Secundario: Verde Cilantro / Limón
    public static final Color ACCENT_SECONDARY = new Color(50, 160, 60);
    // Texto: Carbón / Negro suave
    public static final Color TEXT = new Color(40, 30, 20);
    
    public static final Font PRIMARY = new Font("Segoe UI", Font.PLAIN, 14);
    // Fuente título un poco más grande y "bold"
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font SUBTITLE = new Font("Segoe UI", Font.ITALIC, 14);
    public static final Font MONO = new Font("Consolas", Font.PLAIN, 13);

    public static void applyDefaults(Container root) {
        root.setBackground(BG);
    }

    public static void styleButton(AbstractButton button) {
        button.setFont(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 30, 30), 2),
                BorderFactory.createEmptyBorder(6, 12, 6, 12))
        );
    }

    // Variante clara: fondo claro y texto oscuro
    public static void styleButtonLight(AbstractButton button) {
        button.setFont(PRIMARY);
        button.setForeground(TEXT);
        button.setBackground(new Color(255, 240, 200)); // Un tono más "masa"
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12))
        );
    }

    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(TITLE);
        l.setForeground(new Color(180, 40, 20)); // Rojo oscuro para títulos
        return l;
    }

    public static JLabel subtitleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(SUBTITLE);
        l.setForeground(new Color(100, 80, 60));
        return l;
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(MONO);
        area.setForeground(TEXT);
        area.setBackground(new Color(255, 252, 240));
        area.setBorder(BorderFactory.createLineBorder(CARD_BORDER));
    }

    public static TitledBorder titled(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 2), 
                title
        );
        tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        tb.setTitleColor(new Color(120, 60, 20));
        return tb;
    }
}