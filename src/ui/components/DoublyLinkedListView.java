package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedListView extends JPanel {
    private final List<String> items = new ArrayList<>();
    private int highlightIndex = -1;

    public DoublyLinkedListView() {
        setPreferredSize(new Dimension(600, 120));
        setOpaque(false);
    }

    public void setList(List<?> list) {
        items.clear();
        for (Object o : list)
            items.add(String.valueOf(o));
        repaint();
    }

    public void clear() {
        items.clear();
        highlightIndex = -1;
        repaint();
    }

    public void highlightIndex(int index) {
        highlightIndex = index;
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
        int gap = 40; // More gap for arrows
        int boxW = 60;
        int boxH = 40;
        int startX = padding;
        int startY = (getHeight() - boxH) / 2;

        FontMetrics fm = g2.getFontMetrics(Theme.PRIMARY);
        g2.setFont(Theme.PRIMARY);

        for (int i = 0; i < n; i++) {
            int x = startX + i * (boxW + gap);
            if (x + boxW > getWidth())
                break;

            // Draw Arrows (before drawing the node, so it's behind or between)
            if (i > 0) {
                int prevX = startX + (i - 1) * (boxW + gap) + boxW;
                int arrowY = startY + boxH / 2;
                int currX = x;

                g2.setColor(Theme.ACCENT);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(prevX, arrowY, currX, arrowY);

                // Arrowheads
                drawArrowHead(g2, currX, arrowY, prevX, arrowY); // <
                drawArrowHead(g2, prevX, arrowY, currX, arrowY); // >
            }

            boolean hi = (i == highlightIndex);
            Color fillColor = hi ? new Color(255, 193, 7) : Theme.ACCENT;
            Color borderColor = hi ? new Color(255, 160, 0) : Theme.ACCENT;
            Color textColor = Color.WHITE;

            // Draw Node
            g2.setColor(fillColor);
            g2.fillRoundRect(x, startY, boxW, boxH, 8, 8);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(borderColor);
            g2.drawRoundRect(x, startY, boxW, boxH, 8, 8);

            String s = items.get(i);
            if (fm.stringWidth(s) > boxW - 6)
                s = "..";
            int tx = x + (boxW - fm.stringWidth(s)) / 2;
            int ty = startY + (boxH + fm.getAscent()) / 2 - 4;
            g2.setColor(textColor);
            g2.drawString(s, tx, ty);
        }

        // Draw null at the end
        if (n > 0) {
            int lastX = startX + (n - 1) * (boxW + gap) + boxW;
            if (lastX + 30 <= getWidth()) {
                int arrowY = startY + boxH / 2;
                g2.setColor(Theme.ACCENT);
                g2.drawLine(lastX, arrowY, lastX + 20, arrowY);
                g2.drawString("null", lastX + 25, arrowY + 5);
            }
        }

        g2.dispose();
    }

    private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2) {
        // Draw arrow head at (x2, y2) pointing from (x1, y1)
        double phi = Math.toRadians(30);
        int barb = 8;
        double dy = y2 - y1;
        double dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        double x, y, rho = theta + phi;
        for (int j = 0; j < 2; j++) {
            x = x2 - barb * Math.cos(rho);
            y = y2 - barb * Math.sin(rho);
            g2.drawLine(x2, y2, (int) x, (int) y);
            rho = theta - phi;
        }
    }
}
