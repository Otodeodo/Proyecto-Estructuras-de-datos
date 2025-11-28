package ui.panels;

import utils.ArrayUtils;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class ArreglosPanel extends JPanel {
    private final JTextField input;
    private final JTextArea output;
    private int[] currentArr = new int[0];

    public ArreglosPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Menú del Día (Arreglos)"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Gestión de platillos e ingredientes"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Tarjeta de formulario
        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("IDs de platillos (enteros, separados por comas):");
        lbl.setFont(Theme.PRIMARY);
        card.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        input = new JTextField();
        card.add(input, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton cargar = new JButton("Servir Menú");
        JButton limpiar = new JButton("Limpiar Mesa");
        Theme.styleButtonLight(cargar); Theme.styleButtonLight(limpiar);
        buttons.add(cargar); buttons.add(limpiar);
        card.add(buttons, gbc);

        // Vista del arreglo y opciones
        ui.components.ArrayView arrayView = new ui.components.ArrayView();
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(card, BorderLayout.NORTH);
        center.add(arrayView, BorderLayout.CENTER);

        // Opciones del arreglo (arriba, debajo de los botones de Cargar/Limpiar)
        JPanel opts = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JTextField buscarTxt = new JTextField(6);
        JLabel buscarLbl = new JLabel("Buscar Platillo (ID):"); buscarLbl.setFont(Theme.PRIMARY);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnAsc = new JButton("Ordenar (Asc)");
        JButton btnDesc = new JButton("Ordenar (Desc)");
        JButton btnInvertir = new JButton("Invertir Orden");
        Theme.styleButtonLight(btnBuscar); Theme.styleButtonLight(btnAsc); Theme.styleButtonLight(btnDesc);
        Theme.styleButtonLight(btnInvertir);
        opts.add(buscarLbl); opts.add(buscarTxt); opts.add(btnBuscar);
        opts.add(btnAsc); opts.add(btnDesc); opts.add(btnInvertir);

        // Colocar el bloque de opciones en la tarjeta, arriba del ArrayView
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        card.add(opts, gbc);
        // Ya no se añade al sur del centro
        add(center, BorderLayout.CENTER);

        // Resultado
        output = new JTextArea(10, 50);
        output.setEditable(false);
        Theme.styleTextArea(output);
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(Theme.titled("Estado del Menú"));
        add(sp, BorderLayout.SOUTH);

        cargar.addActionListener(e -> onCargar(arrayView));
        limpiar.addActionListener(e -> onLimpiar(arrayView));
        btnBuscar.addActionListener(e -> onBuscar(arrayView, buscarTxt));
        btnAsc.addActionListener(e -> onOrdenar(arrayView, true));
        btnDesc.addActionListener(e -> onOrdenar(arrayView, false));
        btnInvertir.addActionListener(e -> onInvertir(arrayView));
    }

    private void onCargar(ui.components.ArrayView arrayView) {
        try {
            int[] arr = ArrayUtils.parseIntArray(input.getText());
            currentArr = arr;
            arrayView.animateFill(currentArr, 300);
            output.setText("Menú servido: " + ArrayUtils.toString(arr));

            // Validación: números repetidos
            java.util.List<Integer> dupIdx = getDuplicateIndices(arr);
            if (!dupIdx.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "El menú contiene platillos repetidos: " + getDuplicateMessage(arr),
                        "Error en la cocina",
                        JOptionPane.ERROR_MESSAGE
                );
                // Resaltar todas las posiciones repetidas
                arrayView.highlightIndices(dupIdx);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLimpiar(ui.components.ArrayView arrayView) {
        input.setText("");
        output.setText("");
        currentArr = new int[0];
        arrayView.clear();
    }

    private void onBuscar(ui.components.ArrayView arrayView, JTextField buscarTxt) {
        try {
            int value = Integer.parseInt(buscarTxt.getText().trim());
            java.util.List<Integer> matches = new java.util.ArrayList<>();
            for (int i = 0; i < currentArr.length; i++) {
                if (currentArr[i] == value) { matches.add(i); }
            }
            if (matches.isEmpty()) {
                output.setText("Platillo no encontrado: " + value);
                arrayView.clearHighlight();
                JOptionPane.showMessageDialog(this, "El platillo " + value + " no está en el menú", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (matches.size() > 1) {
                output.setText("El platillo " + value + " está repetido en índices " + matches);
                arrayView.highlightIndices(matches);
                JOptionPane.showMessageDialog(this, "El platillo " + value + " está repetido en el menú", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                output.setText("Platillo encontrado " + value + " en índice " + matches.get(0));
                arrayView.highlightIndices(matches);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onOrdenar(ui.components.ArrayView arrayView, boolean asc) {
        if (currentArr.length == 0) { output.setText("Primero sirva el menú"); return; }
        int[] copy = currentArr.clone();
        java.util.Arrays.sort(copy);
        if (!asc) {
            for (int i = 0, j = copy.length - 1; i < j; i++, j--) {
                int tmp = copy[i]; copy[i] = copy[j]; copy[j] = tmp;
            }
        }
        currentArr = copy;
        output.setText("Menú ordenado " + (asc ? "asc" : "desc") + ": " + ArrayUtils.toString(currentArr));
        arrayView.clearHighlight();
        arrayView.animateFill(currentArr, 250);
    }

    private void onInvertir(ui.components.ArrayView arrayView) {
        if (currentArr.length == 0) { output.setText("Primero sirva el menú"); return; }
        for (int i = 0, j = currentArr.length - 1; i < j; i++, j--) {
            int tmp = currentArr[i]; currentArr[i] = currentArr[j]; currentArr[j] = tmp;
        }
        output.setText("Orden invertido: " + ArrayUtils.toString(currentArr));
        arrayView.clearHighlight();
        arrayView.animateFill(currentArr, 250);
    }

    // Métodos removidos: Min/Max y Suma/Promedio según solicitud del usuario

    // Helpers de validación de repetidos
    private java.util.List<Integer> getDuplicateIndices(int[] arr) {
        java.util.Map<Integer, java.util.List<Integer>> map = new java.util.HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            map.computeIfAbsent(arr[i], k -> new java.util.ArrayList<>()).add(i);
        }
        java.util.List<Integer> dup = new java.util.ArrayList<>();
        for (java.util.Map.Entry<Integer, java.util.List<Integer>> e : map.entrySet()) {
            if (e.getValue().size() > 1) dup.addAll(e.getValue());
        }
        return dup;
    }

    private String getDuplicateMessage(int[] arr) {
        java.util.Map<Integer, Integer> count = new java.util.HashMap<>();
        for (int v : arr) { count.put(v, count.getOrDefault(v, 0) + 1); }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (java.util.Map.Entry<Integer, Integer> e : count.entrySet()) {
            if (e.getValue() > 1) {
                if (!first) sb.append(", ");
                sb.append(e.getKey()).append(" (").append(e.getValue()).append(")");
                first = false;
            }
        }
        return sb.toString();
    }
}