package ui;

import java.awt.*;
import javax.swing.*;

import ui.panels.ArreglosPanel;
import ui.panels.PilasPanel;
import ui.panels.ColasPanel;
import ui.panels.ListaCircularPanel;
import ui.panels.ShellSortPanel;
import ui.panels.BusquedaBinariaPanel;
import ui.panels.ArbolBinarioPanel;
import ui.panels.GrafoPanel;
import ui.theme.Theme;

public class MenuPrincipal extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public MenuPrincipal() {
        super("Taquería - Estructuras de Datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        Theme.applyDefaults(getContentPane());
        initUI();
    }

    private void initUI() {
        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 10, 20));
        header.add(Theme.titleLabel("Taquería \"El Programador Hambriento\""), BorderLayout.WEST);
        header.add(Theme.subtitleLabel("Donde los datos se cocinan con sabor"), BorderLayout.SOUTH);

        // Toolbar con botones de navegación estilizados
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        toolBar.setBackground(new Color(255, 245, 230)); // Fondo ligero para la barra
        
        addNavButton(toolBar, "Menú (Arreglos)", "Arreglos");
        addNavButton(toolBar, "Pila de Tortillas", "Pilas");
        addNavButton(toolBar, "Fila de Clientes", "Colas");
        addNavButton(toolBar, "Rueda de Salsas", "ListaCircular");
        addNavButton(toolBar, "Ordenar Pedidos", "ShellSort");
        addNavButton(toolBar, "Buscar Ingrediente", "BusquedaBinaria");
        addNavButton(toolBar, "Árbol de Recetas", "ArbolBinario");
        addNavButton(toolBar, "Mapa del Local", "Grafo");

        // Contenido
        getContentPane().setLayout(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(header, BorderLayout.NORTH);
        JScrollPane navScroller = new JScrollPane(
                toolBar,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        navScroller.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        navScroller.getViewport().setOpaque(false);
        navScroller.setOpaque(false);
        top.add(navScroller, BorderLayout.SOUTH);
        
        getContentPane().add(top, BorderLayout.NORTH);
        
        JScrollPane sp = new JScrollPane(cardPanel);
        sp.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        sp.getViewport().setBackground(Theme.BG);
        getContentPane().add(sp, BorderLayout.CENTER);

        // Barra de estado
        JLabel status = new JLabel("Sistema listo para tomar órdenes...");
        status.setFont(Theme.MONO);
        status.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        getContentPane().add(status, BorderLayout.SOUTH);

        // Registrar paneles
        cardPanel.setOpaque(false);
        cardPanel.add(new ArreglosPanel(), "Arreglos");
        cardPanel.add(new PilasPanel(), "Pilas");
        cardPanel.add(new ColasPanel(), "Colas");
        cardPanel.add(new ListaCircularPanel(), "ListaCircular");
        cardPanel.add(new ShellSortPanel(), "ShellSort");
        cardPanel.add(new BusquedaBinariaPanel(), "BusquedaBinaria");
        cardPanel.add(new ArbolBinarioPanel(), "ArbolBinario");
        cardPanel.add(new GrafoPanel(), "Grafo");

        showPanel("Arreglos");
    }

    private void addNavButton(JToolBar bar, String title, String panelName) {
        JButton btn = new JButton(title);
        Theme.styleButtonLight(btn);
        btn.addActionListener(e -> showPanel(panelName));
        bar.add(btn);
        bar.addSeparator(new Dimension(8, 0));
    }

    private void showPanel(String name) {
        cardLayout.show(cardPanel, name);
    }
}