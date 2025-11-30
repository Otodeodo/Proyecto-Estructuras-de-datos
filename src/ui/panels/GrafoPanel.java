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
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // --- Sección: Agregar Mesa ---
        JPanel vertexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        vertexPanel.setOpaque(false);
        JLabel vLbl = new JLabel("Nueva mesa/zona:");
        vLbl.setFont(Theme.PRIMARY);
        vertexPanel.add(vLbl);
        vertexInput = new JTextField(12);
        vertexPanel.add(vertexInput);
        JButton addVertex = new JButton("Agregar Mesa");
        Theme.styleButtonLight(addVertex);
        vertexPanel.add(addVertex);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(vertexPanel, gbc);

        // --- Sección: Conexiones ---
        gbc.gridy++;
        JPanel edgePanel = new JPanel(new GridBagLayout());
        edgePanel.setOpaque(false);
        edgePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.ACCENT), "Conexión",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                Theme.PRIMARY, Theme.ACCENT));

        GridBagConstraints egbc = new GridBagConstraints();
        egbc.insets = new Insets(4, 8, 4, 8);
        egbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Inputs
        egbc.gridx = 0;
        egbc.gridy = 0;
        edgePanel.add(new JLabel("De:"), egbc);
        egbc.gridx = 1;
        fromInput = new JTextField(8);
        edgePanel.add(fromInput, egbc);

        egbc.gridx = 2;
        edgePanel.add(new JLabel("A:"), egbc);
        egbc.gridx = 3;
        toInput = new JTextField(8);
        edgePanel.add(toInput, egbc);

        egbc.gridx = 4;
        edgePanel.add(new JLabel("Distancia (m):"), egbc);
        egbc.gridx = 5;
        weightInput = new JTextField(6);
        edgePanel.add(weightInput, egbc);

        // Fila 2: Botones
        egbc.gridx = 0;
        egbc.gridy = 1;
        egbc.gridwidth = 6;
        JPanel edgeButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        edgeButtons.setOpaque(false);
        bidirectional = new JCheckBox("Bidireccional", true);
        bidirectional.setOpaque(false);
        bidirectional.setFont(Theme.PRIMARY);
        edgeButtons.add(bidirectional);
        edgeButtons.add(Box.createHorizontalStrut(15));
        JButton addEdge = new JButton("Conectar Mesas");
        Theme.styleButtonLight(addEdge);
        edgeButtons.add(addEdge);
        edgePanel.add(edgeButtons, egbc);

        card.add(edgePanel, gbc);

        // --- Sección: Rutas y Algoritmos ---
        gbc.gridy++;
        JPanel pathsPanel = new JPanel(new GridBagLayout());
        pathsPanel.setOpaque(false);
        pathsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.ACCENT),
                "Rutas y Algoritmos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, Theme.PRIMARY, Theme.ACCENT));

        GridBagConstraints pgbc = new GridBagConstraints();
        pgbc.insets = new Insets(4, 8, 4, 8);
        pgbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Inputs
        pgbc.gridx = 0;
        pgbc.gridy = 0;
        pathsPanel.add(new JLabel("Origen:"), pgbc);
        pgbc.gridx = 1;
        startInput = new JTextField(8);
        pathsPanel.add(startInput, pgbc);

        pgbc.gridx = 2;
        pathsPanel.add(new JLabel("Destino:"), pgbc);
        pgbc.gridx = 3;
        targetInput = new JTextField(8);
        pathsPanel.add(targetInput, pgbc);

        // Fila 2: Botones de Algoritmos
        pgbc.gridx = 0;
        pgbc.gridy = 1;
        pgbc.gridwidth = 4;
        JPanel algoButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        algoButtons.setOpaque(false);

        JButton bfs = new JButton("BFS (Anchura)");
        JButton dfs = new JButton("DFS (Profundidad)");
        JButton shortest = new JButton("MST (Kruskal)");
        JButton limpiar = new JButton("Limpiar");

        Theme.styleButtonLight(bfs);
        Theme.styleButtonLight(dfs);
        Theme.styleButtonLight(shortest);
        Theme.styleButtonLight(limpiar);

        algoButtons.add(bfs);
        algoButtons.add(dfs);
        algoButtons.add(shortest);
        algoButtons.add(Box.createHorizontalStrut(10));
        algoButtons.add(limpiar);

        pathsPanel.add(algoButtons, pgbc);

        card.add(pathsPanel, gbc);

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

        addVertex.addActionListener(e -> {
            onAddVertex();
            graphView.repaint();
        });
        addEdge.addActionListener(e -> {
            onAddEdge();
            graphView.repaint();
        });
        bfs.addActionListener(e -> onBfs(graphView));
        dfs.addActionListener(e -> onDfs(graphView));
        shortest.addActionListener(e -> onKruskal(graphView));
        limpiar.addActionListener(e -> {
            onLimpiar();
            graphView.clearTraversal();
            graphView.repaint();
        });
    }

    private void onAddVertex() {
        String v = vertexInput.getText().trim();
        if (v.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de la mesa/zona", "Error",
                    JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Complete los campos de la conexión", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int peso = 1;
        String wStr = weightInput.getText().trim();
        if (!wStr.isEmpty()) {
            try {
                peso = Integer.parseInt(wStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La distancia debe ser entera", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al procesar la distancia: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (peso < 0) {
                JOptionPane.showMessageDialog(this, "La distancia no puede ser negativa", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        grafo.addEdge(f, t, peso, bidirectional.isSelected());
        fromInput.setText("");
        toInput.setText("");
        weightInput.setText("");
        refreshAdj();
    }

    private void onBfs(ui.components.GraphView graphView) {
        String s = startInput.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese mesa inicial", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!containsVertex(s)) {
            JOptionPane.showMessageDialog(this, "La mesa/zona '" + s + "' no existe en el mapa", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> orden = grafo.bfs(s);
        output.append("\nRecorrido en Anchura: " + orden);
        graphView.setTraversalOrder(orden);
    }

    private void onDfs(ui.components.GraphView graphView) {
        String s = startInput.getText().trim();
        if (s.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese mesa inicial", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!containsVertex(s)) {
            JOptionPane.showMessageDialog(this, "La mesa/zona '" + s + "' no existe en el mapa", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> orden = grafo.dfs(s);
        output.append("\nRecorrido en Profundidad: " + orden);
        graphView.setTraversalOrder(orden);
    }

    private void onKruskal(ui.components.GraphView graphView) {
        structures.Grafo.MSTResult result = grafo.kruskalMST();
        if (result.edges.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El grafo no tiene aristas para formar un MST", "MST Vacío",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nÁrbol de Expansión Mínima (Kruskal):\n");
        for (structures.Grafo.Edge edge : result.edges) {
            sb.append(edge.toString()).append(", ");
        }
        sb.append("\nCosto Total: ").append(result.totalWeight).append("m");
        output.append(sb.toString());

        // Visualizar el MST resaltando las aristas
        graphView.setHighlightedEdges(result.edges);
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
        if (adj.containsKey(v))
            return true;
        for (java.util.List<String> list : adj.values()) {
            for (String t : list)
                if (t.equals(v))
                    return true;
        }
        return false;
    }
}