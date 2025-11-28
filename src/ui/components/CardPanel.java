package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {
    public CardPanel() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setLayout(new GridBagLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // sombra suave
        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(6, 6, w - 12, h - 12, 16, 16);
        // tarjeta
        g2.setColor(Theme.CARD_BG);
        g2.fillRoundRect(0, 0, w - 12, h - 12, 16, 16);
        g2.setColor(Theme.CARD_BORDER);
        g2.drawRoundRect(0, 0, w - 12, h - 12, 16, 16);
        g2.dispose();
        super.paintComponent(g);
    }
}