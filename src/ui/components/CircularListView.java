package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircularListView extends JPanel {
    private final List<String> items = new ArrayList<>();

    public CircularListView() {
        setPreferredSize(new Dimension(600, 240));
        setOpaque(false);
    }

    public void setList(List<?> list) {
        items.clear();
        for (Object o : list) items.add(String.valueOf(o));
        repaint();
    }

    public void clear() {
        items.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int n = items.size();
        if (n == 0) {
            g2.setFont(Theme.TITLE);
            g2.setColor(Theme.ACCENT);
            String msg = "Rueda vacía";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            g2.dispose();
            return;
        }

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 3;
        int boxSize = 50;

        g2.setFont(Theme.PRIMARY);
        FontMetrics fm = g2.getFontMetrics();

        // Dibujar conexiones primero
        g2.setColor(Theme.ACCENT);
        g2.setStroke(new BasicStroke(2f));
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            double nextAngle = 2 * Math.PI * ((i + 1) % n) / n - Math.PI / 2;

            int x1 = (int) (centerX + radius * Math.cos(angle));
            int y1 = (int) (centerY + radius * Math.sin(angle));
            int x2 = (int) (centerX + radius * Math.cos(nextAngle));
            int y2 = (int) (centerY + radius * Math.sin(nextAngle));

            g2.drawLine(x1, y1, x2, y2);
        }

        // Dibujar nodos
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            int x = (int) (centerX + radius * Math.cos(angle)) - boxSize / 2;
            int y = (int) (centerY + radius * Math.sin(angle)) - boxSize / 2;

            g2.setColor(new Color(255, 250, 225)); // Fondo claro
            g2.fillOval(x, y, boxSize, boxSize);
            g2.setColor(Theme.ACCENT);
            g2.drawOval(x, y, boxSize, boxSize);

            String s = items.get(i);
            if (fm.stringWidth(s) > boxSize - 10) s = "...";
            int tx = x + (boxSize - fm.stringWidth(s)) / 2;
            int ty = y + (boxSize + fm.getAscent()) / 2 - 4;
            g2.drawString(s, tx, ty);
        }

        g2.dispose();
    }
}
