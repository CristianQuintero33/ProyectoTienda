package gui;

import dao.impl.*;
import gui.panels.*;
import service.IClienteService;
import service.IProductoService;
import service.IVentaService;
import service.impl.*;

import javax.swing.*;
import java.awt.*;


public class VentanaPrincipal extends JFrame {

    private PanelProductos panelProductos;
    private PanelClientes  panelClientes;
    private PanelVentas    panelVentas;

    public VentanaPrincipal() {
        // ── Construir capas (inyección de dependencias) ─────────────
        var productoDAO = new ProductoDAOMemoria();
        var clienteDAO  = new ClienteDAOMemoria();
        var ventaDAO    = new VentaDAOMemoria();

        IProductoService productoService = new ProductoServiceImpl(productoDAO);
        IClienteService  clienteService  = new ClienteServiceImpl(clienteDAO);
        IVentaService    ventaService    = new VentaServiceImpl(ventaDAO, productoService, clienteService);

        // Datos de prueba
        cargarDatosDemostracion(productoService, clienteService);

        // ── Construir paneles ────────────────────────────────────────
        panelProductos = new PanelProductos(productoService);
        panelClientes  = new PanelClientes(clienteService);
        panelVentas    = new PanelVentas(ventaService, clienteService, productoService);

        // ── Ventana ──────────────────────────────────────────────────
        setTitle("🏪  Sistema de Gestión — Tienda Minorista");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(EstiloTienda.FONDO);
        setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(EstiloTienda.PANEL);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, EstiloTienda.ACENTO));
        JLabel titulo = new JLabel("  🏪  SISTEMA DE GESTION - TIENDA MINORISTA");
        titulo.setFont(EstiloTienda.FUENTE_TITULO);
        titulo.setForeground(EstiloTienda.ACENTO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(titulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        
        
        // ── Pestañas ──────────────────────────────────────────────────        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(EstiloTienda.FONDO);
        tabs.setForeground(EstiloTienda.TEXTO);
        tabs.setFont(EstiloTienda.FUENTE_SUBTIT);

        tabs.addTab("📦  Productos",  panelProductos);
        tabs.addTab("👤  Clientes",   panelClientes);
        tabs.addTab("🛒  Ventas",     panelVentas);

        // Al entrar a Ventas, refrescar los combos con datos actuales
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 2) panelVentas.refrescarCombos();
        
        });

        add(tabs, BorderLayout.CENTER);
    }

    /** Datos de prueba para demostrar el sistema. */
    private void cargarDatosDemostracion(IProductoService ps, IClienteService cs) {
        try {
            ps.registrarProducto(new model.Producto(
                "P001","Arroz 1kg", model.Producto.Categoria.VIVERES,
                2500, 3200, 80, 10, 200));
            ps.registrarProducto(new model.Producto(
                "P002","Aceite 1L", model.Producto.Categoria.VIVERES,
                8000, 10500, 45, 8, 100));
            ps.registrarProducto(new model.Producto(
                "P003","Jabón de manos", model.Producto.Categoria.ASEO,
                3000, 4200, 60, 15, 150));
            ps.registrarProducto(new model.Producto(
                "P004","Cuaderno 100 hojas", model.Producto.Categoria.PAPELERIA,
                4500, 6000, 5, 10, 80));

            cs.registrarCliente(new model.Cliente(
                "C001","Juan Pérez",
                model.Cliente.TipoIdentificacion.CC, "10234567",
                "Cra 5 #12-34","3001234567",
                model.Cliente.TipoCliente.MINORISTA));
            cs.registrarCliente(new model.Cliente(
                "C002","Distribuidora Gran Vía S.A.S",
                model.Cliente.TipoIdentificacion.NIT,"900123456-1",
                "Av Principal #1-00","6012345678",
                model.Cliente.TipoCliente.MAYORISTA));
        } catch (Exception ignore) {}
    }
}
