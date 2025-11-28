package ui.panels;

import algorithms.ShellSort;
import utils.ArrayUtils;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class ShellSortPanel extends JPanel {
    private final JTextField input;
    private final JTextArea output;

    public ShellSortPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Ordenar Pedidos (Shell Sort)"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Organizar pedidos por prioridad/número"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("Pedidos (IDs enteros separados por comas):"); lbl.setFont(Theme.PRIMARY); card.add(lbl, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        input = new JTextField(); card.add(input, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton ordenar = new JButton("Ordenar Pedidos");
        JButton limpiar = new JButton("Limpiar");
        Theme.styleButtonLight(ordenar); Theme.styleButtonLight(limpiar);
        buttons.add(ordenar); buttons.add(limpiar);
        card.add(buttons, gbc);

        // Vista visual del arreglo
        ui.components.ArrayView arrayView = new ui.components.ArrayView();
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        card.add(arrayView, gbc);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(card, BorderLayout.NORTH);
        center.add(new JPanel(), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        Theme.styleTextArea(output);
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(Theme.titled("Pedidos ordenados"));
        add(sp, BorderLayout.SOUTH);

        ordenar.addActionListener(e -> onOrdenar(arrayView));
        limpiar.addActionListener(e -> onLimpiar(arrayView));
    }

    private void onOrdenar(ui.components.ArrayView arrayView) {
        try {
            int[] arr = ArrayUtils.parseIntArray(input.getText());
            int[] sorted = ShellSort.shellSort(arr);
            arrayView.setArray(sorted);
            output.setText("Pedidos ordenados: " + ArrayUtils.toString(sorted));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLimpiar(ui.components.ArrayView arrayView) { 
        input.setText(""); 
        output.setText(""); 
        arrayView.clear();
    }
}