package ui.components;

import structures.Grafo;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GraphView extends JPanel {
    private final Grafo grafo;
    private java.util.List<String> traversalOrder = java.util.Collections.emptyList();
    private java.util.List<Grafo.Edge> highlightedEdges = java.util.Collections.emptyList();

    public GraphView(Grafo grafo) {
        this.grafo = grafo;
        setPreferredSize(new Dimension(700, 320));
        setBackground(Theme.CARD_BG);
    }

    public void setTraversalOrder(java.util.List<String> order) {
        this.traversalOrder = order == null ? java.util.Collections.emptyList() : new java.util.ArrayList<>(order);
        this.highlightedEdges = java.util.Collections.emptyList(); // Clear edge highlight when showing path
        repaint();
    }

    public void setHighlightedEdges(java.util.List<Grafo.Edge> edges) {
        this.highlightedEdges = edges == null ? java.util.Collections.emptyList() : new java.util.ArrayList<>(edges);
        this.traversalOrder = java.util.Collections.emptyList(); // Clear path traversal when showing edges
        repaint();
    }

    public void clearTraversal() {
        this.traversalOrder = java.util.Collections.emptyList();
        this.highlightedEdges = java.util.Collections.emptyList();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<String, java.util.List<String>> adj = grafo.getAdyacencia();
        Set<String> vertices = new LinkedHashSet<>(adj.keySet());
        for (java.util.List<String> list : adj.values())
            vertices.addAll(list);

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
                        g2.fillRoundRect(mx - tagW / 2, my - tagH / 2, tagW, tagH, 8, 8);
                        g2.setColor(Color.WHITE);
                        g2.drawString(ps, mx - fm.stringWidth(ps) / 2, my - tagH / 2 + fm.getAscent());
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
                    // Calcular puntos ajustados para que la flecha no quede dentro del nodo
                    double angle = Math.atan2(b.y - a.y, b.x - a.x);
                    int startX = (int) (a.x + nodeR * Math.cos(angle));
                    int startY = (int) (a.y + nodeR * Math.sin(angle));
                    int endX = (int) (b.x - nodeR * Math.cos(angle));
                    int endY = (int) (b.y - nodeR * Math.sin(angle));

                    drawArrowLine(g2, startX, startY, endX, endY, 10, 8);
                }
            }
        }

        // Resaltar aristas del MST (o cualquier conjunto de aristas)
        if (!highlightedEdges.isEmpty()) {
            g2.setStroke(new BasicStroke(3.5f));
            g2.setColor(new Color(76, 175, 80)); // Green for MST
            for (Grafo.Edge edge : highlightedEdges) {
                Point a = pos.get(edge.src);
                Point b = pos.get(edge.dest);
                if (a != null && b != null) {
                    g2.drawLine(a.x, a.y, b.x, b.y);
                }
            }
        }

        // Nodes
        java.util.Map<String, Integer> orderIndex = new java.util.HashMap<>();
        for (int i = 0; i < traversalOrder.size(); i++)
            orderIndex.put(traversalOrder.get(i), i);

        // Para MST, resaltamos todos los nodos involucrados
        Set<String> mstNodes = new HashSet<>();
        for (Grafo.Edge e : highlightedEdges) {
            mstNodes.add(e.src);
            mstNodes.add(e.dest);
        }

        for (String v : vs) {
            Point p = pos.get(v);
            int x = p.x - nodeR;
            int y = p.y - nodeR;
            boolean inTraversal = orderIndex.containsKey(v);
            boolean inMST = mstNodes.contains(v);

            Color nodeColor = Color.WHITE;
            Color borderColor = Theme.ACCENT;

            if (inTraversal) {
                nodeColor = new Color(255, 193, 7);
                borderColor = new Color(255, 160, 0);
            } else if (inMST) {
                nodeColor = new Color(129, 199, 132);
                borderColor = new Color(76, 175, 80);
            }

            g2.setColor(nodeColor);
            g2.fillOval(x, y, nodeR * 2, nodeR * 2);
            g2.setColor(borderColor);
            g2.drawOval(x, y, nodeR * 2, nodeR * 2);
            g2.setColor(Theme.TEXT);
            g2.setFont(Theme.PRIMARY);
            FontMetrics fm = g2.getFontMetrics();
            int tx = p.x - fm.stringWidth(v) / 2;
            int ty = p.y + fm.getAscent() / 2 - 2;
            g2.drawString(v, tx, ty);

            // Orden pequeño arriba del nodo (solo para traversal)
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

    /**
     * Dibuja una línea con punta de flecha.
     * 
     * @param g  Graphics2D context
     * @param x1 Start X
     * @param y1 Start Y
     * @param x2 End X
     * @param y2 End Y
     * @param d  Ancho de la punta de flecha
     * @param h  Altura de la punta de flecha
     */
    private void drawArrowLine(Graphics2D g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = { x2, (int) xm, (int) xn };
        int[] ypoints = { y2, (int) ym, (int) yn };

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }
}