package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StackView extends JPanel {
    private final List<String> items = new ArrayList<>(); // tope -> base
    private int highlightIndex = -1; // índice relativo desde tope

    public StackView() {
        setPreferredSize(new Dimension(600, 240));
        setOpaque(false);
    }

    public void setStack(List<?> stack) {
        items.clear();
        for (Object o : stack) items.add(String.valueOf(o));
        repaint();
    }

    public void clear() {
        items.clear();
        highlightIndex = -1;
        repaint();
    }

    public void highlightTop() {
        highlightIndex = items.isEmpty() ? -1 : 0;
        repaint();
    }

    public void clearHighlight() {
        highlightIndex = -1;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int n = items.size();
        int padding = 16;
        int gap = 12;
        int boxW = Math.max(90, getWidth() - padding * 2);
        int boxH = 40;
        int startY = padding;

        FontMetrics fm = g2.getFontMetrics(Theme.PRIMARY);
        g2.setFont(Theme.PRIMARY);

        for (int i = 0; i < n; i++) {
            int y = startY + i * (boxH + gap);
            boolean hi = (i == highlightIndex);
            Color fillColor = hi ? new Color(255, 193, 7) : Theme.ACCENT;
            Color borderColor = hi ? new Color(255, 160, 0) : Theme.ACCENT;
            Color textColor = Color.WHITE;

            g2.setColor(fillColor);
            g2.fillRoundRect(padding, y, boxW, boxH, 10, 10);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(borderColor);
            g2.drawRoundRect(padding, y, boxW, boxH, 10, 10);

            String s = items.get(i);
            int tx = padding + (boxW - fm.stringWidth(s)) / 2;
            int ty = y + (boxH + fm.getAscent()) / 2 - 6;
            g2.setColor(textColor);
            g2.drawString(s, tx, ty);
        }

        // Etiquetas Top/Base
        if (n > 0) {
            g2.setColor(new Color(90, 90, 90));
            g2.drawString("Top", padding, startY - 4);
            int baseY = startY + (n - 1) * (boxH + gap) + boxH + 14;
            g2.drawString("Base", padding, baseY);
        }

        g2.dispose();
    }
}