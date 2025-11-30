package ui.panels;

import structures.ListaDoblementeLigada;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class ListaDoblementeLigadaPanel extends JPanel {
    private final ListaDoblementeLigada<Integer> lista = new ListaDoblementeLigada<>();
    private final JTextField input;
    private final JTextArea output;

    public ListaDoblementeLigadaPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Fila de Órdenes"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Gestión de órdenes pendientes (doble enlace)"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("Número de Orden:");
        lbl.setFont(Theme.PRIMARY);
        card.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        input = new JTextField(12);
        input.setToolTipText("Ingrese número(s) de orden separados por comas (ej: 101, 102, 103)");
        card.add(input, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton insert = new JButton("Agregar Orden");
        JButton delete = new JButton("Completar Orden");
        JButton contains = new JButton("Buscar Orden");
        JButton limpiar = new JButton("Limpiar Fila");
        Theme.styleButtonLight(insert);
        Theme.styleButtonLight(delete);
        Theme.styleButtonLight(contains);
        Theme.styleButtonLight(limpiar);
        buttons.add(insert);
        buttons.add(delete);
        buttons.add(contains);
        buttons.add(limpiar);
        card.add(buttons, gbc);

        // Vista visual de la lista
        ui.components.DoublyLinkedListView listView = new ui.components.DoublyLinkedListView();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        card.add(listView, gbc);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(card, BorderLayout.NORTH);
        center.add(new JPanel(), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        Theme.styleTextArea(output);
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(Theme.titled("Estado de la lista"));
        add(sp, BorderLayout.SOUTH);

        insert.addActionListener(e -> onInsert(listView));
        delete.addActionListener(e -> onDelete(listView));
        contains.addActionListener(e -> onContains(listView));
        limpiar.addActionListener(e -> onLimpiar(listView));
        refresh(listView);
    }

    private void onInsert(ui.components.DoublyLinkedListView listView) {
        try {
            String text = input.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese al menos un número de orden", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Soportar inserción masiva por comas
            String[] parts = text.split(",");
            for (String part : parts) {
                lista.insert(Integer.parseInt(part.trim()));
            }
            input.setText("");
            refresh(listView);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese números de orden válidos separados por comas", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete(ui.components.DoublyLinkedListView listView) {
        try {
            int v = Integer.parseInt(input.getText().trim());
            boolean ok = lista.delete(v);
            JOptionPane.showMessageDialog(this, ok ? "Orden #" + v + " completada" : "Orden #" + v + " no encontrada");
            input.setText("");
            refresh(listView);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de orden válido", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onContains(ui.components.DoublyLinkedListView listView) {
        try {
            int v = Integer.parseInt(input.getText().trim());
            int index = lista.indexOf(v);
            if (index != -1) {
                listView.highlightIndex(index);
                output.setText("Orden #" + v + " encontrada en posición " + index + " de la fila");
                // JOptionPane.showMessageDialog(this, "El elemento existe en el índice " +
                // index);
            } else {
                listView.clearHighlight();
                output.setText("Orden #" + v + " no encontrada en la fila");
                JOptionPane.showMessageDialog(this, "La orden #" + v + " no existe en la fila", "No encontrada",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de orden válido", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLimpiar(ui.components.DoublyLinkedListView listView) {
        while (!lista.isEmpty()) {
            java.util.List<Integer> l = lista.toList();
            if (l.isEmpty())
                break;
            lista.delete(l.get(0));
        }
        refresh(listView);
        output.setText("Fila de órdenes limpia.");
    }

    private void refresh(ui.components.DoublyLinkedListView listView) {
        output.setText("Fila de Órdenes: " + lista.toString() + "\nTotal de órdenes: " + lista.size());
        listView.setList(lista.toList());
    }
}
