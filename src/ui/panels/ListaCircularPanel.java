package ui.panels;

import structures.ListaCircular;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class ListaCircularPanel extends JPanel {
    private final ListaCircular<Integer> lista = new ListaCircular<>();
    private final JTextField input;
    private final JTextArea output;

    public ListaCircularPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Rueda de Salsas"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Selección rotativa de salsas"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("ID Salsa (int):"); lbl.setFont(Theme.PRIMARY); card.add(lbl, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        input = new JTextField(12); card.add(input, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton insert = new JButton("Agregar Salsa");
        JButton delete = new JButton("Quitar Salsa");
        JButton contains = new JButton("Verificar Salsa");
        JButton limpiar = new JButton("Limpiar Rueda");
        Theme.styleButtonLight(insert); Theme.styleButtonLight(delete); Theme.styleButtonLight(contains); Theme.styleButtonLight(limpiar);
        buttons.add(insert); buttons.add(delete); buttons.add(contains); buttons.add(limpiar);
        card.add(buttons, gbc);

        // Vista visual de la lista circular
        ui.components.CircularListView listView = new ui.components.CircularListView();
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
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
        sp.setBorder(Theme.titled("Estado de la rueda de salsas"));
        add(sp, BorderLayout.SOUTH);

        insert.addActionListener(e -> onInsert(listView));
        delete.addActionListener(e -> onDelete(listView));
        contains.addActionListener(e -> onContains());
        limpiar.addActionListener(e -> onLimpiar(listView));
        refresh(listView);
    }

    private void onInsert(ui.components.CircularListView listView) {
        try {
            int v = Integer.parseInt(input.getText().trim());
            lista.insert(v);
            input.setText("");
            refresh(listView);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete(ui.components.CircularListView listView) {
        try {
            int v = Integer.parseInt(input.getText().trim());
            boolean ok = lista.delete(v);
            JOptionPane.showMessageDialog(this, ok ? "Salsa eliminada" : "Salsa no encontrada");
            input.setText("");
            refresh(listView);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onContains() {
        try {
            int v = Integer.parseInt(input.getText().trim());
            boolean ok = lista.contains(v);
            JOptionPane.showMessageDialog(this, ok ? "Sí tenemos esa salsa" : "No tenemos esa salsa");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLimpiar(ui.components.CircularListView listView) {
        while (!lista.isEmpty()) {
            java.util.List<Integer> l = lista.toList();
            if (l.isEmpty()) break;
            lista.delete(l.get(0));
        }
        refresh(listView);
        output.setText("Rueda de salsas limpia.");
    }

    private void refresh(ui.components.CircularListView listView) {
        listView.setList(lista.toList());
        // output.setText(lista.toString() + "\nTamaño: " + lista.size());
    }
}