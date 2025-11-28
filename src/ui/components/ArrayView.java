package ui.components;

import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class ArrayView extends JPanel {
    private int[] arr = new int[0];
    private int animIndex = -1;
    private Timer timer;
    private java.util.Set<Integer> highlight = new java.util.HashSet<>();

    public ArrayView() {
        setPreferredSize(new Dimension(800, 160));
        setBackground(new Color(250, 250, 250));
        setOpaque(true);
    }

    public void setArray(int[] a) {
        stopAnimation();
        arr = a == null ? new int[0] : a.clone();
        animIndex = arr.length - 1; // todo pintado
        highlight.clear();
        repaint();
    }

    public void clear() {
        stopAnimation();
        arr = new int[0];
        animIndex = -1;
        highlight.clear();
        repaint();
    }

    public void animateFill(int[] a, int delayMs) {
        stopAnimation();
        arr = a == null ? new int[0] : a.clone();
        animIndex = -1; // comienza vacío
        timer = new Timer(Math.max(50, delayMs), e -> {
            if (animIndex >= arr.length - 1) { stopAnimation(); return; }
            animIndex++;
            repaint();
        });
        timer.start();
    }

    public void highlightIndices(java.util.Collection<Integer> indices) {
        stopAnimation();
        highlight.clear();
        if (indices != null) highlight.addAll(indices);
        repaint();
    }

    public void clearHighlight() {
        highlight.clear();
        repaint();
    }

    private void stopAnimation() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int n = arr.length;
        if (n == 0) { g2.dispose(); return; }

        int padding = 16;
        int gap = 8;
        int boxW = Math.max(40, (getWidth() - 2*padding - gap*(n-1)) / Math.max(1, n));
        int boxH = 48;
        int y = getHeight()/2 - boxH/2;

        Font f = Theme.PRIMARY;
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics(f);

        for (int i = 0; i < n; i++) {
            int x = padding + i * (boxW + gap);
            boolean filled = i <= animIndex;
            boolean hi = highlight.contains(i);

            Color fillColor;
            Color borderColor;
            Color textColor;

            if (hi) {
                fillColor = new Color(255, 193, 7); // ámbar para resaltar
                borderColor = new Color(255, 160, 0);
                textColor = Color.WHITE;
            } else if (filled) {
                fillColor = Theme.ACCENT;
                borderColor = Theme.ACCENT;
                textColor = Color.WHITE;
            } else {
                fillColor = Color.WHITE;
                borderColor = Theme.ACCENT;
                textColor = Theme.TEXT;
            }

            g2.setColor(fillColor);
            g2.fillRoundRect(x, y, boxW, boxH, 10, 10);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(borderColor);
            g2.drawRoundRect(x, y, boxW, boxH, 10, 10);

            String s = String.valueOf(arr[i]);
            int tx = x + (boxW - fm.stringWidth(s)) / 2;
            int ty = y + (boxH + fm.getAscent()) / 2 - 6;
            g2.setColor(textColor);
            g2.drawString(s, tx, ty);
        }

        g2.dispose();
    }
}