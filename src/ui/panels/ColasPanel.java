package ui.panels;

import structures.Cola;
import ui.components.CardPanel;
import ui.theme.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.NoSuchElementException;

public class ColasPanel extends JPanel {
    private final Cola<Integer> cola = new Cola<>();
    private final JTextField input;
    private final JTextArea output;

    public ColasPanel() {
        Theme.applyDefaults(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.titleLabel("Fila de Clientes"), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Atención de clientes en orden de llegada"), BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        CardPanel card = new CardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel("ID Cliente (Ticket):"); lbl.setFont(Theme.PRIMARY); card.add(lbl, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        input = new JTextField(12); card.add(input, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton enq = new JButton("Formar (Enqueue)");
        JButton deq = new JButton("Atender (Dequeue)");
        JButton front = new JButton("Ver Siguiente (Front)");
        JButton limpiar = new JButton("Cerrar Fila");
        Theme.styleButtonLight(enq); Theme.styleButtonLight(deq); Theme.styleButtonLight(front); Theme.styleButtonLight(limpiar);
        buttons.add(enq); buttons.add(deq); buttons.add(front); buttons.add(limpiar);
        card.add(buttons, gbc);

        // Vista visual de la cola
        ui.components.QueueView queueView = new ui.components.QueueView();
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        card.add(queueView, gbc);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(card, BorderLayout.NORTH);
        center.add(new JPanel(), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        output = new JTextArea(10, 50);
        output.setEditable(false);
        Theme.styleTextArea(output);
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(Theme.titled("Estado de la fila de clientes"));
        add(sp, BorderLayout.SOUTH);

        enq.addActionListener(e -> onEnqueue(queueView));
        deq.addActionListener(e -> onDequeue(queueView));
        front.addActionListener(e -> onFront(queueView));
        limpiar.addActionListener(e -> onLimpiar(queueView));
        refresh(queueView);
    }

    private void onEnqueue(ui.components.QueueView queueView) {
        try {
            int v = Integer.parseInt(input.getText().trim());
            cola.enqueue(v);
            input.setText("");
            refresh(queueView);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un entero válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDequeue(ui.components.QueueView queueView) {
        try {
            Integer v = cola.dequeue();
            output.append("\nAtendido cliente con ticket: " + v);
            refresh(queueView);
        } catch (NoSuchElementException ex) {
            JOptionPane.showMessageDialog(this, "No hay clientes en la fila", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onFront(ui.components.QueueView queueView) {
        try {
            Integer v = cola.front();
            output.append("\nSiguiente cliente: " + v);
            queueView.highlightFront();
        } catch (NoSuchElementException ex) {
            JOptionPane.showMessageDialog(this, "No hay clientes en la fila", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onLimpiar(ui.components.QueueView queueView) {
        while (!cola.isEmpty()) cola.dequeue();
        refresh(queueView);
        queueView.clearHighlight();
        output.setText("Fila cerrada. Todos los clientes se han ido.");
    }

    private void refresh(ui.components.QueueView queueView) {
        queueView.setQueue(cola.toList());
    }
}