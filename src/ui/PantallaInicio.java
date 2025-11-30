package ui;

import java.awt.*;
import javax.swing.*;
import ui.theme.Theme;

public class PantallaInicio extends JFrame {

    public PantallaInicio() {
        super("Taquería - Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        Theme.applyDefaults(getContentPane());
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel principal con fondo
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Título
        JLabel titulo = new JLabel("Taquería \"El Programador Hambriento\"");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setForeground(Theme.ACCENT);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Sistema de Gestión de Datos");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        subtitulo.setForeground(Theme.TEXT);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(subtitulo);
        mainPanel.add(Box.createVerticalStrut(50));

        // Panel de botones en grid
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setMaximumSize(new Dimension(800, 350));

        // Crear botones del menú
        addMenuButton(gridPanel, "Menú (Arreglos)", "Gestión de arreglos");
        addMenuButton(gridPanel, "Pila de Tortillas", "Estructura LIFO");
        addMenuButton(gridPanel, "Fila de Clientes", "Estructura FIFO");
        addMenuButton(gridPanel, "Fila de Órdenes", "Lista doblemente ligada");
        addMenuButton(gridPanel, "Ordenamiento Inserción", "Insertion Sort");
        addMenuButton(gridPanel, "Buscar Ingrediente", "Búsqueda binaria");
        addMenuButton(gridPanel, "Árbol de Recetas", "Árbol binario");
        addMenuButton(gridPanel, "Mapa del Local", "Grafos y rutas");

        // Botón de salir
        JButton exitBtn = createStyledButton("Salir", "Cerrar aplicación");
        exitBtn.setBackground(new Color(255, 200, 200)); // Fondo rojo claro
        exitBtn.setForeground(Color.BLACK);
        exitBtn.addActionListener(e -> System.exit(0));
        gridPanel.add(exitBtn);

        mainPanel.add(gridPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Pie de página
        JLabel footer = new JLabel("Seleccione una opción para comenzar");
        footer.setFont(Theme.PRIMARY);
        footer.setForeground(new Color(120, 120, 120));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(footer);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addMenuButton(JPanel panel, String text, String description) {
        JButton btn = createStyledButton(text, description);
        btn.addActionListener(e -> {
            // Abrir MenuPrincipal y cerrar esta ventana
            SwingUtilities.invokeLater(() -> {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
                // Navegar al panel correspondiente basado en el texto
                String panelName = getPanelNameFromButton(text);
                if (panelName != null) {
                    menu.showPanel(panelName);
                }
                dispose();
            });
        });
        panel.add(btn);
    }

    private JButton createStyledButton(String text, String description) {
        JButton btn = new JButton(
                "<html><center><b>" + text + "</b><br><small>" + description + "</small></center></html>");
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setBackground(new Color(255, 245, 230)); // Fondo beige claro
        btn.setForeground(Color.BLACK); // Texto negro
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT, 3),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(description);
        btn.setPreferredSize(new Dimension(200, 80));

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 235, 205)); // Más oscuro al pasar el mouse
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(255, 245, 230));
            }
        });

        return btn;
    }

    private String getPanelNameFromButton(String buttonText) {
        if (buttonText.contains("Menú"))
            return "Arreglos";
        if (buttonText.contains("Pila"))
            return "Pilas";
        if (buttonText.contains("Fila de Clientes"))
            return "Colas";
        if (buttonText.contains("Fila de Órdenes"))
            return "ListaDoblementeLigada";
        if (buttonText.contains("Ordenamiento"))
            return "InsertionSort";
        if (buttonText.contains("Buscar"))
            return "BusquedaBinaria";
        if (buttonText.contains("Árbol"))
            return "ArbolBinario";
        if (buttonText.contains("Mapa"))
            return "Grafo";
        return null;
    }
}
