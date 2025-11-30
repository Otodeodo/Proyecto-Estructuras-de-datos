package ui.panels;

import structures.ArbolBinario;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ArbolBinarioPanel extends JPanel {
    private final ArbolBinario arbol = new ArbolBinario();
    private final JTextField input;
    private final JTextArea output;

    public ArbolBinarioPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Árbol de Recetas"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Organización jerárquica de recetas"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Contenido central: formulario arriba y vista del árbol abajo
        JPanel center = new JPanel(new BorderLayout());

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("ID Receta (int):");
        lbl.setFont(Theme.PRIMARY);
        card.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        input = new JTextField(10);
        card.add(input, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton insert = new JButton("Insertar Receta");
        JButton search = new JButton("Buscar Receta");
        JButton delete = new JButton("Eliminar Receta");
        JButton inOrder = new JButton("In-Orden");
        JButton preOrder = new JButton("Pre-Orden");
        JButton postOrder = new JButton("Post-Orden");
        JButton limpiar = new JButton("Limpiar Árbol");
        Theme.styleButtonLight(insert);
        Theme.styleButtonLight(search);
        Theme.styleButtonLight(delete);
        Theme.styleButtonLight(inOrder);
        Theme.styleButtonLight(preOrder);
        Theme.styleButtonLight(postOrder);
        Theme.styleButtonLight(limpiar);
        buttons.add(insert);
        buttons.add(search);
        buttons.add(delete);
        buttons.add(inOrder);
        buttons.add(preOrder);
        buttons.add(postOrder);
        buttons.add(limpiar);
        card.add(buttons, gbc);

        center.add(card, BorderLayout.NORTH);

        ui.components.BinaryTreeView treeView = new ui.components.BinaryTreeView(arbol);
        center.add(treeView, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        Theme.styleTextArea(output);
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(Theme.titled("Estado del Árbol de Recetas"));
        add(sp, BorderLayout.SOUTH);

        insert.addActionListener(e -> {
            onInsert();
            treeView.clearHighlight();
            treeView.repaint();
        });
        search.addActionListener(e -> onSearch(treeView));
        delete.addActionListener(e -> {
            onDelete();
            treeView.clearHighlight();
            treeView.repaint();
        });
        inOrder.addActionListener(e -> {
            onTraverse("in", treeView);
        });
        preOrder.addActionListener(e -> {
            onTraverse("pre", treeView);
        });
        postOrder.addActionListener(e -> {
            onTraverse("post", treeView);
        });
        limpiar.addActionListener(e -> {
            onLimpiar();
            treeView.clearHighlight();
            treeView.repaint();
        });
    }

    private void onInsert() {
        try {
            int v = Integer.parseInt(input.getText().trim());
            arbol.insert(v);
            input.setText("");
            output.setText("Receta insertada: " + v);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido para insertar", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch(ui.components.BinaryTreeView treeView) {
        try {
            int v = Integer.parseInt(input.getText().trim());
            List<Integer> path = arbol.pathTo(v);
            boolean ok = !path.isEmpty() && path.get(path.size() - 1) == v;
            output.setText((ok ? "Receta encontrada" : "Receta no encontrada") + ": ruta " + path);
            // Animar la ruta de búsqueda para que se note el recorrido
            treeView.animatePath(path, 450);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido para buscar", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        try {
            int v = Integer.parseInt(input.getText().trim());
            arbol.delete(v);
            output.setText("Receta eliminada: " + v);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido para eliminar", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onTraverse(String type, ui.components.BinaryTreeView treeView) {
        List<Integer> res;
        String label;
        switch (type) {
            case "in":
                res = arbol.inOrder();
                label = "In-Orden (Ingredientes)";
                break;
            case "pre":
                res = arbol.preOrder();
                label = "Pre-Orden (Preparación)";
                break;
            default:
                res = arbol.postOrder();
                label = "Post-Orden (Limpieza)";
                break;
        }
        output.setText(label + ": " + res);
        // Animar el recorrido para que se note paso a paso
        treeView.animatePath(res, 350);
    }

    private void onLimpiar() {
        arbol.clear();
        output.setText("Árbol de recetas reiniciado");
    }
}