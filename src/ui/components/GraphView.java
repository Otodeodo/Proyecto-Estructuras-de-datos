package ui.components;

import structures.Grafo;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GraphView extends JPanel {
    private final Grafo grafo;
    private java.util.List<String> traversalOrder = java.util.Collections.emptyList();

    public GraphView(Grafo grafo) {
        this.grafo = grafo;
        setPreferredSize(new Dimension(700, 320));
        setBackground(Theme.CARD_BG);
    }

    public void setTraversalOrder(java.util.List<String> order) {
        this.traversalOrder = order == null ? java.util.Collections.emptyList() : new java.util.ArrayList<>(order);
        repaint();
    }

    public void clearTraversal() {
        this.traversalOrder = java.util.Collections.emptyList();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<String, java.util.List<String>> adj = grafo.getAdyacencia();
        Set<String> vertices = new LinkedHashSet<>(adj.keySet());
        for (java.util.List<String> list : adj.values()) vertices.addAll(list);

        if (vertices.isEmpty()) {
            g2.setColor(Theme.TEXT);
            g2.setFont(Theme.PRIMARY);
            g2.drawString("Grafo vacío", 16, 24);
            return;
        }

        int w = getWidth();
        int h = getHeight();
        int r = Math.min(w, h) / 2 - 40;
        int cx = w / 2;
        int cy = h / 2;
        int nodeR = 16;

        java.util.List<String> vs = new ArrayList<>(vertices);
        Map<String, Point> pos = new HashMap<>();
        for (int i = 0; i < vs.size(); i++) {
            double angle = 2 * Math.PI * i / vs.size();
            int x = (int) (cx + r * Math.cos(angle));
            int y = (int) (cy + r * Math.sin(angle));
            pos.put(vs.get(i), new Point(x, y));
        }

        // Edges
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(144, 202, 249));
        for (Map.Entry<String, java.util.List<String>> e : adj.entrySet()) {
            Point a = pos.get(e.getKey());
            for (String t : e.getValue()) {
                Point b = pos.get(t);
                if (a != null && b != null) {
                    g2.drawLine(a.x, a.y, b.x, b.y);
                    // Dibujar el peso si existe
                    Integer peso = grafo.getPeso(e.getKey(), t);
                    if (peso != null) {
                        int mx = (a.x + b.x) / 2;
                        int my = (a.y + b.y) / 2;
                        g2.setColor(new Color(33, 150, 243));
                        g2.setFont(Theme.PRIMARY);
                        String ps = String.valueOf(peso);
                        FontMetrics fm = g2.getFontMetrics();
                        int tagW = fm.stringWidth(ps) + 8;
                        int tagH = fm.getHeight();
                        g2.setColor(new Color(33, 150, 243));
                        g2.fillRoundRect(mx - tagW/2, my - tagH/2, tagW, tagH, 8, 8);
                        g2.setColor(Color.WHITE);
                        g2.drawString(ps, mx - fm.stringWidth(ps)/2, my - tagH/2 + fm.getAscent());
                        g2.setColor(new Color(144, 202, 249));
                    }
                }
            }
        }

        // Resaltar aristas del recorrido/camino provisto
        if (traversalOrder.size() > 1) {
            g2.setStroke(new BasicStroke(3.5f));
            g2.setColor(new Color(255, 160, 0));
            for (int i = 0; i < traversalOrder.size() - 1; i++) {
                String aId = traversalOrder.get(i);
                String bId = traversalOrder.get(i + 1);
                Point a = pos.get(aId);
                Point b = pos.get(bId);
                if (a != null && b != null) {
                    g2.drawLine(a.x, a.y, b.x, b.y);
                }
            }
        }

        // Nodes
        java.util.Map<String, Integer> orderIndex = new java.util.HashMap<>();
        for (int i = 0; i < traversalOrder.size(); i++) orderIndex.put(traversalOrder.get(i), i);
        for (String v : vs) {
            Point p = pos.get(v);
            int x = p.x - nodeR;
            int y = p.y - nodeR;
            boolean inTraversal = orderIndex.containsKey(v);
            g2.setColor(inTraversal ? new Color(255, 193, 7) : Color.WHITE);
            g2.fillOval(x, y, nodeR * 2, nodeR * 2);
            g2.setColor(inTraversal ? new Color(255, 160, 0) : Theme.ACCENT);
            g2.drawOval(x, y, nodeR * 2, nodeR * 2);
            g2.setColor(Theme.TEXT);
            g2.setFont(Theme.PRIMARY);
            FontMetrics fm = g2.getFontMetrics();
            int tx = p.x - fm.stringWidth(v) / 2;
            int ty = p.y + fm.getAscent() / 2 - 2;
            g2.drawString(v, tx, ty);

            // Orden pequeño arriba del nodo
            if (inTraversal) {
                String idx = String.valueOf(orderIndex.get(v));
                int tagW = fm.stringWidth(idx) + 8;
                int tagH = fm.getHeight();
                int tagX = x + nodeR * 2 - tagW;
                int tagY = y - tagH - 2;
                g2.setColor(new Color(33, 150, 243));
                g2.fillRoundRect(tagX, tagY, tagW, tagH, 8, 8);
                g2.setColor(Color.WHITE);
                g2.drawString(idx, tagX + 4, tagY + fm.getAscent());
            }
        }
    }
}