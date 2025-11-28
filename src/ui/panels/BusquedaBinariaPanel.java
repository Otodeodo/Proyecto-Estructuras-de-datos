package ui.panels;

import algorithms.BinarySearch;
import algorithms.ShellSort;
import utils.ArrayUtils;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class BusquedaBinariaPanel extends JPanel {
    private final JTextField inputArray;
    private final JTextField inputTarget;
    private final JTextArea output;

    public BusquedaBinariaPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Buscar Ingrediente (Binaria)"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Encontrar un ingrediente en el inventario ordenado"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel l1 = new JLabel("Inventario (separados por comas):"); l1.setFont(Theme.PRIMARY); card.add(l1, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        inputArray = new JTextField(); 
        inputArray.setToolTipText("Ejemplo: 5, 8, 12, 15, 20");
        card.add(inputArray, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton ordenar = new JButton("Ordenar");
        Theme.styleButtonLight(ordenar);
        card.add(ordenar, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JLabel l2 = new JLabel("Buscar ID:"); l2.setFont(Theme.PRIMARY); card.add(l2, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        inputTarget = new JTextField(); 
        inputTarget.setToolTipText("Ingrese un número a buscar");
        card.add(inputTarget, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JButton buscar = new JButton("Buscar");
        Theme.styleButtonLight(buscar);
        card.add(buscar, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        JButton limpiar = new JButton("Limpiar Todo");
        Theme.styleButtonLight(limpiar);
        JPanel limpiarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        limpiarPanel.setOpaque(false);
        limpiarPanel.add(limpiar);
        card.add(limpiarPanel, gbc);

        // Vista visual del arreglo
        ui.components.ArrayView arrayView = new ui.components.ArrayView();
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
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
        sp.setBorder(Theme.titled("Resultado de búsqueda"));
        add(sp, BorderLayout.SOUTH);

        buscar.addActionListener(e -> onBuscar(arrayView));
        ordenar.addActionListener(e -> onOrdenar(arrayView));
        limpiar.addActionListener(e -> onLimpiar(arrayView));
    }

    private void onBuscar(ui.components.ArrayView arrayView) {
        try {
            int[] arr = ArrayUtils.parseIntArray(inputArray.getText());
            String targetTxt = inputTarget.getText().trim();
            if (targetTxt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un número en 'Buscar ID'", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int target = Integer.parseInt(targetTxt);
            // Ordenar automáticamente para asegurar búsqueda correcta
            int[] sorted = ShellSort.shellSort(arr);
            // Actualizar el campo con el arreglo ordenado para que el índice sea claro
            inputArray.setText(arrayToInput(sorted));
            int idx = BinarySearch.binarySearch(sorted, target);
            
            // Mostrar el arreglo y resaltar el índice encontrado
            arrayView.setArray(sorted);
            if (idx >= 0) {
                arrayView.highlightIndices(java.util.Collections.singleton(idx));
                output.setText("✓ Ingrediente " + target + " encontrado en posición: " + idx + "\n(El inventario se ordenó automáticamente)");
            } else {
                arrayView.clearHighlight();
                output.setText("✗ Ingrediente " + target + " no encontrado en el inventario\n(El inventario se ordenó automáticamente)");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido. Use números separados por comas.\nEjemplo: 5, 8, 12, 15, 20", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onOrdenar(ui.components.ArrayView arrayView) {
        try {
            int[] arr = ArrayUtils.parseIntArray(inputArray.getText());
            int[] sorted = ShellSort.shellSort(arr);
            inputArray.setText(arrayToInput(sorted));
            arrayView.setArray(sorted);
            output.setText("Inventario ordenado: " + ArrayUtils.toString(sorted));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido. Use números separados por comas.\nEjemplo: 5, 8, 12, 15, 20", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String arrayToInput(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private void onLimpiar(ui.components.ArrayView arrayView) { 
        inputArray.setText(""); 
        inputTarget.setText(""); 
        output.setText(""); 
        arrayView.clear();
    }
}