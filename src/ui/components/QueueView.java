package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QueueView extends JPanel {
    private final List<String> items = new ArrayList<>(); // frente -> fin
    private int highlightIndex = -1; // índice relativo desde frente

    public QueueView() {
        setPreferredSize(new Dimension(600, 120));
        setOpaque(false);
    }

    public void setQueue(List<?> queue) {
        items.clear();
        for (Object o : queue) items.add(String.valueOf(o));
        repaint();
    }

    public void clear() {
        items.clear();
        highlightIndex = -1;
        repaint();
    }

    public void highlightFront() {
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
        int boxW = 80;
        int boxH = 50;
        int startX = padding;
        int startY = (getHeight() - boxH) / 2;

        FontMetrics fm = g2.getFontMetrics(Theme.PRIMARY);
        g2.setFont(Theme.PRIMARY);

        for (int i = 0; i < n; i++) {
            int x = startX + i * (boxW + gap);
            // Si hay muchos elementos, permitir scroll o simplemente dibujar hasta donde quepa
            if (x + boxW > getWidth()) break; 

            boolean hi = (i == highlightIndex);
            Color fillColor = hi ? new Color(255, 193, 7) : Theme.ACCENT;
            Color borderColor = hi ? new Color(255, 160, 0) : Theme.ACCENT;
            Color textColor = Color.WHITE;

            g2.setColor(fillColor);
            g2.fillRoundRect(x, startY, boxW, boxH, 10, 10);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(borderColor);
            g2.drawRoundRect(x, startY, boxW, boxH, 10, 10);

            String s = items.get(i);
            // Truncar si es muy largo
            if (fm.stringWidth(s) > boxW - 10) {
                s = "..."; 
            }
            int tx = x + (boxW - fm.stringWidth(s)) / 2;
            int ty = startY + (boxH + fm.getAscent()) / 2 - 6;
            g2.setColor(textColor);
            g2.drawString(s, tx, ty);
        }

        // Etiquetas Frente/Fin
        if (n > 0) {
            g2.setColor(new Color(90, 90, 90));
            g2.drawString("Frente", startX, startY - 6);
            int endX = startX + (n - 1) * (boxW + gap);
            if (endX + boxW <= getWidth()) {
                g2.drawString("Fin", endX, startY - 6);
            }
        }

        g2.dispose();
    }
}
