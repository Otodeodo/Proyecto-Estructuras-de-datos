package ui.panels;

import algorithms.InsertionSort;
import utils.ArrayUtils;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class InsertionSortPanel extends JPanel {
    private final JTextField input;
    private final JTextArea output;

    public InsertionSortPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Organizar Tickets de Pedidos"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Ordenar tickets por número de llegada"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("Números de Ticket (separados por comas):");
        lbl.setFont(Theme.PRIMARY);
        card.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        input = new JTextField();
        input.setToolTipText("Ejemplo: 105, 102, 108, 101, 103");
        card.add(input, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton ordenar = new JButton("Organizar Tickets");
        JButton limpiar = new JButton("Limpiar");
        Theme.styleButtonLight(ordenar);
        Theme.styleButtonLight(limpiar);
        buttons.add(ordenar);
        buttons.add(limpiar);
        card.add(buttons, gbc);

        // Vista visual del arreglo
        ui.components.ArrayView arrayView = new ui.components.ArrayView();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
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
        sp.setBorder(Theme.titled("Tickets organizados"));
        add(sp, BorderLayout.SOUTH);

        ordenar.addActionListener(e -> onOrdenar(arrayView));
        limpiar.addActionListener(e -> onLimpiar(arrayView));
    }

    private void onOrdenar(ui.components.ArrayView arrayView) {
        try {
            int[] arr = ArrayUtils.parseIntArray(input.getText());
            int[] sorted = InsertionSort.insertionSort(arr);
            arrayView.setArray(sorted);
            output.setText("Tickets organizados por orden de llegada: " + ArrayUtils.toString(sorted));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato inválido. Ingrese números de ticket separados por comas.\nEjemplo: 105, 102, 108, 101",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al ordenar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLimpiar(ui.components.ArrayView arrayView) {
        input.setText("");
        output.setText("");
        arrayView.clear();
    }
}
