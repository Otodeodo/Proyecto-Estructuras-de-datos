package ui.panels;

import structures.Grafo;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class GrafoPanel extends JPanel {
    private final Grafo grafo = new Grafo();
    private final JTextField vertexInput;
    private final JTextField fromInput;
    private final JTextField toInput;
    private final JTextField weightInput;
    private final JCheckBox bidirectional;
    private final JTextField startInput;
    private final JTextField targetInput;
    private final JTextArea output;

    public GrafoPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Mapa del Local"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Conexiones entre mesas y cocina"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel vLbl = new JLabel("Nueva mesa/zona:"); vLbl.setFont(Theme.PRIMARY); card.add(vLbl, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        vertexInput = new JTextField(10); card.add(vertexInput, gbc);
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton addVertex = new JButton("Agregar Mesa"); Theme.styleButtonLight(addVertex); card.add(addVertex, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        // Fila de arista organizada en FlowLayout
        JLabel eLbl = new JLabel("Conexión:"); eLbl.setFont(Theme.PRIMARY);
        JPanel edgeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        edgeRow.setOpaque(false);
        JLabel deLbl = new JLabel("de:"); deLbl.setFont(Theme.PRIMARY);
        fromInput = new JTextField(8);
        JLabel aLbl = new JLabel("a:"); aLbl.setFont(Theme.PRIMARY);
        toInput = new JTextField(8);
        JLabel wLbl = new JLabel("distancia (m):"); wLbl.setFont(Theme.PRIMARY);
        weightInput = new JTextField(6);
        bidirectional = new JCheckBox("Bidireccional", true);
        JButton addEdge = new JButton("Conectar Mesas"); Theme.styleButtonLight(addEdge);
        edgeRow.add(eLbl); edgeRow.add(deLbl); edgeRow.add(fromInput);
        edgeRow.add(aLbl); edgeRow.add(toInput);
        edgeRow.add(wLbl); edgeRow.add(weightInput);
        edgeRow.add(bidirectional); edgeRow.add(addEdge);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 6; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        card.add(edgeRow, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        // Fila de recorridos organizada en FlowLayout
        JPanel pathsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pathsRow.setOpaque(false);
        JLabel sLbl = new JLabel("Mesa Origen:"); sLbl.setFont(Theme.PRIMARY);
        startInput = new JTextField(10);
        JLabel tLbl = new JLabel("Mesa Destino:"); tLbl.setFont(Theme.PRIMARY);
        targetInput = new JTextField(10);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton bfs = new JButton("Ruta (BFS)");
        JButton dfs = new JButton("Ruta (DFS)");
        JButton shortest = new JButton("Camino Corto (Dijkstra)");
        JButton limpiar = new JButton("Limpiar Mapa");
        Theme.styleButtonLight(bfs); Theme.styleButtonLight(dfs); Theme.styleButtonLight(shortest); Theme.styleButtonLight(limpiar);
        buttons.add(bfs); buttons.add(dfs); buttons.add(shortest); buttons.add(limpiar);
        pathsRow.add(sLbl); pathsRow.add(startInput); pathsRow.add(tLbl); pathsRow.add(targetInput); pathsRow.add(buttons);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 6; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        card.add(pathsRow, gbc);

        center.add(card, BorderLayout.NORTH);

        ui.components.GraphView graphView = new ui.components.GraphView(grafo);
        center.add(graphView, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        output = new JTextArea(12, 50);
        output.setEditable(false);
        Theme.styleTextArea(output);
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(Theme.titled("Conexiones y rutas"));
        add(sp, BorderLayout.SOUTH);

        addVertex.addActionListener(e -> { onAddVertex(); graphView.repaint(); });
        addEdge.addActionListener(e -> { onAddEdge(); graphView.repaint(); });
        bfs.addActionListener(e -> onBfs(graphView));
        dfs.addActionListener(e -> onDfs(graphView));
        shortest.addActionListener(e -> onShortest(graphView));
        limpiar.addActionListener(e -> { onLimpiar(); graphView.clearTraversal(); graphView.repaint(); });
    }

    private void onAddVertex() {
        String v = vertexInput.getText().trim();
        if (v.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de la mesa/zona", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        grafo.addVertex(v);
        vertexInput.setText("");
        refreshAdj();
    }

    private void onAddEdge() {
        String f = fromInput.getText().trim();
        String t = toInput.getText().trim();
        if (f.isEmpty() || t.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos de la conexión", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int peso = 1;
        String wStr = weightInput.getText().trim();
        if (!wStr.isEmpty()) {
            try { peso = Integer.parseInt(wStr); } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La distancia debe ser entera", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (peso < 0) {
                JOptionPane.showMessageDialog(this, "La distancia no puede ser negativa", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        grafo.addEdge(f, t, peso, bidirectional.isSelected());
        fromInput.setText(""); toInput.setText(""); weightInput.setText("");
        refreshAdj();
    }

    private void onBfs(ui.components.GraphView graphView) {
        String s = startInput.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese mesa inicial", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!containsVertex(s)) {
            JOptionPane.showMessageDialog(this, "La mesa/zona '" + s + "' no existe en el mapa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> orden = grafo.bfs(s);
        output.append("\nBFS: " + orden);
        graphView.setTraversalOrder(orden);
    }

    private void onDfs(ui.components.GraphView graphView) {
        String s = startInput.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese mesa inicial", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!containsVertex(s)) {
            JOptionPane.showMessageDialog(this, "La mesa/zona '" + s + "' no existe en el mapa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> orden = grafo.dfs(s);
        output.append("\nDFS: " + orden);
        graphView.setTraversalOrder(orden);
    }

    private void onShortest(ui.components.GraphView graphView) {
        String s = startInput.getText().trim();
        String t = targetInput.getText().trim();
        if (s.isEmpty() || t.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese origen y destino", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!containsVertex(s) || !containsVertex(t)) {
            JOptionPane.showMessageDialog(this, "Origen o destino no existen en el mapa", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        structures.Grafo.PathResult pr = grafo.shortestPath(s, t);
        if (pr.path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay camino entre '" + s + "' y '" + t + "'", "Sin ruta", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        output.append("\nRuta mínima " + s + " -> " + t + ": " + pr.path + " (distancia=" + pr.cost + "m)");
        graphView.setTraversalOrder(pr.path);
    }

    private void onLimpiar() {
        grafo.clear();
        output.setText("");
        vertexInput.setText("");
        fromInput.setText("");
        toInput.setText("");
        weightInput.setText("");
        startInput.setText("");
    }

    private void refreshAdj() {
        StringBuilder sb = new StringBuilder();
        sb.append("Conexiones (con distancias):\n");
        for (Map.Entry<String, java.util.Map<String, Integer>> e : grafo.getAdyacenciaConPesos().entrySet()) {
            sb.append(e.getKey()).append(" -> ");
            java.util.List<String> items = new java.util.ArrayList<>();
            for (Map.Entry<String, Integer> w : e.getValue().entrySet()) {
                items.add(w.getKey() + "(" + w.getValue() + "m)");
            }
            sb.append(items).append("\n");
        }
        output.setText(sb.toString());
    }

    private boolean containsVertex(String v) {
        java.util.Map<String, java.util.List<String>> adj = grafo.getAdyacencia();
        if (adj.containsKey(v)) return true;
        for (java.util.List<String> list : adj.values()) {
            for (String t : list) if (t.equals(v)) return true;
        }
        return false;
    }
}