package ui.panels;

import structures.Pila;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.EmptyStackException;

public class PilasPanel extends JPanel {
    private final Pila<Integer> pila = new Pila<>();
    private final JTextField input;
    private final JTextArea output;

    public PilasPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Pila de Tortillas"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Apilar tortillas calientes para los tacos"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("ID tortilla (int):"); lbl.setFont(Theme.PRIMARY); card.add(lbl, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        input = new JTextField(12); card.add(input, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton push = new JButton("Poner (Push)");
        JButton pop = new JButton("Sacar (Pop)");
        JButton peek = new JButton("Ver Tope (Peek)");
        JButton limpiar = new JButton("Limpiar Canasta");
        Theme.styleButtonLight(push); Theme.styleButtonLight(pop); Theme.styleButtonLight(peek); Theme.styleButtonLight(limpiar);
        buttons.add(push); buttons.add(pop); buttons.add(peek); buttons.add(limpiar);
        card.add(buttons, gbc);

        // Vista visual de la pila
        ui.components.StackView stackView = new ui.components.StackView();
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        card.add(stackView, gbc);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(card, BorderLayout.NORTH);
        center.add(new JPanel(), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        Theme.styleTextArea(output);
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(Theme.titled("Estado de la pila de tortillas"));
        add(sp, BorderLayout.SOUTH);

        push.addActionListener(e -> onPush(stackView));
        pop.addActionListener(e -> onPop(stackView));
        peek.addActionListener(e -> onPeek(stackView));
        limpiar.addActionListener(e -> onLimpiar(stackView));
        refresh(stackView);
    }

    private void onPush(ui.components.StackView stackView) {
        try {
            int v = Integer.parseInt(input.getText().trim());
            pila.push(v);
            input.setText("");
            refresh(stackView);
            stackView.highlightTop();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onPop(ui.components.StackView stackView) {
        try {
            Integer v = pila.pop();
            output.append("\nSe sacó la tortilla: " + v);
            refresh(stackView);
        } catch (EmptyStackException ex) {
            JOptionPane.showMessageDialog(this, "La pila de tortillas está vacía", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onPeek(ui.components.StackView stackView) {
        try {
            Integer v = pila.peek();
            output.append("\nTope (Tortilla lista): " + v);
            stackView.highlightTop();
        } catch (EmptyStackException ex) {
            JOptionPane.showMessageDialog(this, "La pila de tortillas está vacía", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onLimpiar(ui.components.StackView stackView) {
        while (!pila.isEmpty()) pila.pop();
        refresh(stackView);
        stackView.clearHighlight();
        output.setText("Canasta limpia.");
    }

    private void refresh(ui.components.StackView stackView) {
        // output.setText(pila.toString() + "\nTamaño: " + pila.size()); // No sobreescribir log
        // Solo actualizar vista gráfica y quizás append estado
        stackView.setStack(pila.toList());
    }
}