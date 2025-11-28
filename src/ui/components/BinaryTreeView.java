package ui.components;

import structures.ArbolBinario;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BinaryTreeView extends JPanel {
    private final ArbolBinario tree;
    private java.util.Set<Integer> highlight = new java.util.HashSet<>();
    private javax.swing.Timer animTimer;
    private java.util.List<Integer> animSeq = java.util.Collections.emptyList();
    private int animIndex = -1;

    public BinaryTreeView(ArbolBinario tree) {
        this.tree = tree;
        setPreferredSize(new Dimension(700, 300));
        setBackground(Theme.CARD_BG);
    }

    public void highlightNodes(java.util.Collection<Integer> nodes) {
        highlight.clear();
        if (nodes != null) highlight.addAll(nodes);
        repaint();
    }

    public void clearHighlight() {
        stopAnimation();
        highlight.clear();
        repaint();
    }

    public void stopAnimation() {
        if (animTimer != null) {
            animTimer.stop();
            animTimer = null;
        }
        animSeq = java.util.Collections.emptyList();
        animIndex = -1;
    }

    public void animatePath(java.util.List<Integer> seq, int delayMs) {
        stopAnimation();
        if (seq == null || seq.isEmpty()) return;
        animSeq = new java.util.ArrayList<>(seq);
        animIndex = 0;
        highlight.clear();
        animTimer = new javax.swing.Timer(Math.max(50, delayMs), e -> {
            if (animIndex >= animSeq.size()) {
                stopAnimation();
                return;
            }
            highlight.add(animSeq.get(animIndex));
            animIndex++;
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        java.util.List<Integer> inorder = tree.inOrder();
        java.util.List<ArbolBinario.NodeData> nodes = tree.nodesWithDepth();
        java.util.List<ArbolBinario.Edge> edges = tree.edges();

        if (nodes.isEmpty()) {
            g2.setColor(Theme.TEXT);
            g2.setFont(Theme.PRIMARY);
            g2.drawString("Árbol vacío", 16, 24);
            return;
        }

        Map<Integer, Integer> xOrder = new HashMap<>();
        for (int i = 0; i < inorder.size(); i++) xOrder.put(inorder.get(i), i);

        int w = getWidth();
        int h = getHeight();
        int marginX = 40;
        int marginY = 30;
        int levels = nodes.stream().mapToInt(n -> n.depth).max().orElse(0) + 1;
        int levelGap = Math.max(60, (h - 2 * marginY) / Math.max(levels, 1));
        int nodeR = 16;

        Map<Integer, Point> pos = new HashMap<>();
        for (ArbolBinario.NodeData nd : nodes) {
            int idx = xOrder.getOrDefault(nd.value, 0);
            double step = (double) (w - 2 * marginX) / Math.max(inorder.size() - 1, 1);
            int x = (int) (marginX + idx * step);
            int y = marginY + nd.depth * levelGap;
            pos.put(nd.value, new Point(x, y));
        }

        // Draw edges
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(144, 202, 249));
        for (ArbolBinario.Edge e : edges) {
            Point a = pos.get(e.from);
            Point b = pos.get(e.to);
            if (a != null && b != null) {
                g2.drawLine(a.x, a.y, b.x, b.y);
            }
        }

        // Draw nodes
        for (ArbolBinario.NodeData nd : nodes) {
            Point p = pos.get(nd.value);
            int x = p.x - nodeR;
            int y = p.y - nodeR;
            boolean isHi = highlight.contains(nd.value);
            g2.setColor(isHi ? Theme.ACCENT : Color.WHITE);
            g2.fillOval(x, y, nodeR * 2, nodeR * 2);
            g2.setColor(Theme.ACCENT);
            g2.drawOval(x, y, nodeR * 2, nodeR * 2);
            g2.setColor(isHi ? Color.WHITE : Theme.TEXT);
            String s = String.valueOf(nd.value);
            FontMetrics fm = g2.getFontMetrics(Theme.PRIMARY);
            g2.setFont(Theme.PRIMARY);
            int tx = p.x - fm.stringWidth(s) / 2;
            int ty = p.y + fm.getAscent() / 2 - 2;
            g2.drawString(s, tx, ty);
        }
    }
}