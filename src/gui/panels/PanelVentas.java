package gui.panels;

import exception.TiendaException;
import gui.EstiloTienda;
import model.Cliente;
import model.Producto;
import model.Venta;
import model.Venta.FormaPago;
import service.IClienteService;
import service.IProductoService;
import service.IVentaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class PanelVentas extends JPanel {

    private final IVentaService    ventaService;
    private final IClienteService  clienteService;
    private final IProductoService productoService;

    // Carrito de la venta actual
    private final Map<String, Integer> carrito = new LinkedHashMap<>();

    // Componentes formulario
    private JComboBox<String>   cmbCliente;
    private JComboBox<String>   cmbProducto;
    private JTextField          txtCantidad;
    private JComboBox<FormaPago> cmbFormaPago;
    private JCheckBox           chkIva;

    // Tabla carrito
    private DefaultTableModel modeloCarrito;

    // Tabla historial
    private DefaultTableModel modeloHistorial;

    // Labels resumen
    private JLabel lblSubtotal, lblIva, lblTotal;

    public PanelVentas(IVentaService ventaService,
                       IClienteService clienteService,
                       IProductoService productoService) {
        this.ventaService    = ventaService;
        this.clienteService  = clienteService;
        this.productoService = productoService;
        construirUI();
        cargarHistorial();
    }

    private void construirUI() {
        setBackground(EstiloTienda.FONDO);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                construirPanelSuperior(), construirPanelHistorial());
        split.setBackground(EstiloTienda.FONDO);
        split.setDividerLocation(380);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    // ── Panel superior: formulario + carrito ──────────────────────
    private JPanel construirPanelSuperior() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(EstiloTienda.FONDO);
        p.add(construirFormularioVenta(), BorderLayout.NORTH);
        p.add(construirCarrito(), BorderLayout.CENTER);
        p.add(construirResumenYBoton(), BorderLayout.EAST);
        return p;
    }

    private JPanel construirFormularioVenta() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstiloTienda.PANEL);
        p.setBorder(EstiloTienda.bordeTitulado("🛒  Nueva Venta"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        // Poblar combos
        List<String> codigosClientes = new ArrayList<>();
        clienteService.listarActivos().forEach(c -> codigosClientes.add(c.getCodigo() + " - " + c.getNombreCompleto()));
        cmbCliente = new JComboBox<>(codigosClientes.toArray(new String[0]));
        cmbCliente.setBackground(EstiloTienda.PANEL_CLARO);
        cmbCliente.setForeground(EstiloTienda.TEXTO);
        cmbCliente.setFont(EstiloTienda.FUENTE_NORMAL);

        List<String> codigosProductos = new ArrayList<>();
        productoService.listarActivos().forEach(prod -> codigosProductos.add(prod.getCodigo() + " - " + prod.getNombre()));
        cmbProducto = new JComboBox<>(codigosProductos.toArray(new String[0]));
        cmbProducto.setBackground(EstiloTienda.PANEL_CLARO);
        cmbProducto.setForeground(EstiloTienda.TEXTO);
        cmbProducto.setFont(EstiloTienda.FUENTE_NORMAL);

        txtCantidad  = EstiloTienda.campo(6);
        txtCantidad.setText("1");
        cmbFormaPago = new JComboBox<>(FormaPago.values());
        cmbFormaPago.setBackground(EstiloTienda.PANEL_CLARO);
        cmbFormaPago.setForeground(EstiloTienda.TEXTO);
        cmbFormaPago.setFont(EstiloTienda.FUENTE_NORMAL);
        chkIva = new JCheckBox("Aplica IVA (19%)");
        chkIva.setBackground(EstiloTienda.PANEL);
        chkIva.setForeground(EstiloTienda.TEXTO);
        chkIva.setFont(EstiloTienda.FUENTE_NORMAL);

        JButton btnAgregarItem = EstiloTienda.botonSecundario("➕  Agregar");
        btnAgregarItem.addActionListener(e -> agregarAlCarrito());

        gc.gridx=0; gc.gridy=0; p.add(EstiloTienda.label("Cliente:"), gc);
        gc.gridx=1; gc.weightx=1; p.add(cmbCliente, gc); gc.weightx=0;
        gc.gridx=2; p.add(EstiloTienda.label("Forma de pago:"), gc);
        gc.gridx=3; p.add(cmbFormaPago, gc);
        gc.gridx=4; p.add(chkIva, gc);

        gc.gridx=0; gc.gridy=1; p.add(EstiloTienda.label("Producto:"), gc);
        gc.gridx=1; gc.weightx=1; p.add(cmbProducto, gc); gc.weightx=0;
        gc.gridx=2; p.add(EstiloTienda.label("Cantidad:"), gc);
        gc.gridx=3; p.add(txtCantidad, gc);
        gc.gridx=4; p.add(btnAgregarItem, gc);

        return p;
    }

    private JScrollPane construirCarrito() {
        String[] cols = {"Producto","Cantidad","Precio Unit.","Subtotal"};
        modeloCarrito = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaCarrito = EstiloTienda.tabla(cols);
        tablaCarrito.setModel(modeloCarrito);

        JScrollPane sp = new JScrollPane(tablaCarrito);
        sp.setBorder(EstiloTienda.bordeTitulado("🛒  Items de la venta"));
        sp.getViewport().setBackground(EstiloTienda.PANEL);
        return sp;
    }

    private JPanel construirResumenYBoton() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstiloTienda.PANEL);
        p.setBorder(EstiloTienda.bordeTitulado("💰  Resumen"));
        p.setPreferredSize(new Dimension(200, 0));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 10, 8, 10);
        gc.fill   = GridBagConstraints.HORIZONTAL;
        gc.gridx  = 0; gc.weightx = 1;

        lblSubtotal = EstiloTienda.label("Subtotal:  $0");
        lblIva      = EstiloTienda.label("IVA:       $0");
        lblTotal    = EstiloTienda.labelSubtitulo("TOTAL:     $0");
        lblTotal.setForeground(EstiloTienda.ACENTO);

        JButton btnRegistrar = EstiloTienda.botonPrimario("✅ Registrar Venta");
        JButton btnLimpiar   = EstiloTienda.botonSecundario("🗑 Limpiar");

        btnRegistrar.addActionListener(e -> registrarVenta());
        btnLimpiar.addActionListener(e   -> limpiarCarrito());

        gc.gridy=0; p.add(lblSubtotal, gc);
        gc.gridy=1; p.add(lblIva, gc);
        gc.gridy=2; p.add(new JSeparator(), gc);
        gc.gridy=3; p.add(lblTotal, gc);
        gc.gridy=4; p.add(btnRegistrar, gc);
        gc.gridy=5; p.add(btnLimpiar, gc);

        return p;
    }

    private JScrollPane construirPanelHistorial() {
        String[] cols = {"Factura","Fecha","Cliente","Items","Subtotal","IVA","Total","Pago","Estado"};
        modeloHistorial = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaH = EstiloTienda.tabla(cols);
        tablaH.setModel(modeloHistorial);

        JScrollPane sp = new JScrollPane(tablaH);
        sp.setBorder(EstiloTienda.bordeTitulado("📋  Historial de Ventas"));
        sp.getViewport().setBackground(EstiloTienda.PANEL);
        return sp;
    }

    // ── Lógica ─────────────────────────────────────────────────────
    private void agregarAlCarrito() {
        try {
            if (cmbProducto.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No hay productos disponibles.");
                return;
            }
            String seleccion = (String) cmbProducto.getSelectedItem();
            String codigo    = seleccion.split(" - ")[0].trim();
            int cantidad     = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) throw new NumberFormatException();

            Producto p = productoService.buscarProducto(codigo);
            carrito.merge(codigo, cantidad, Integer::sum);
            actualizarTablaCarrito();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "La cantidad debe ser un número entero mayor que 0.",
                "⚠ Valor inválido", JOptionPane.WARNING_MESSAGE);
        } catch (TiendaException e) {
            EstiloTienda.mostrarError(this, e);
        }
    }

    private void actualizarTablaCarrito() {
        modeloCarrito.setRowCount(0);
        double subtotal = 0;
        for (Map.Entry<String, Integer> entry : carrito.entrySet()) {
            try {
                Producto p = productoService.buscarProducto(entry.getKey());
                double sub = p.getPrecioVenta() * entry.getValue();
                subtotal += sub;
                modeloCarrito.addRow(new Object[]{
                    p.getNombre(), entry.getValue(),
                    String.format("$%,.0f", p.getPrecioVenta()),
                    String.format("$%,.0f", sub)
                });
            } catch (TiendaException ignore) {}
        }
        double iva   = chkIva.isSelected() ? subtotal * 0.19 : 0;
        double total = subtotal + iva;
        lblSubtotal.setText(String.format("Subtotal: $%,.0f", subtotal));
        lblIva.setText(String.format("IVA:      $%,.0f", iva));
        lblTotal.setText(String.format("TOTAL:  $%,.0f", total));
    }

    private void registrarVenta() {
        if (carrito.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto al carrito.");
            return;
        }
        if (cmbCliente.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay clientes registrados.");
            return;
        }
        try {
            String selCliente = (String) cmbCliente.getSelectedItem();
            String codigoCliente = selCliente.split(" - ")[0].trim();
            FormaPago fp = (FormaPago) cmbFormaPago.getSelectedItem();

            Venta v = ventaService.registrarVenta(codigoCliente, carrito, fp, chkIva.isSelected());
            EstiloTienda.mostrarExito(this,
                "Venta registrada: " + v.getNumeroFactura() +
                "\nTotal: $" + String.format("%,.0f", v.getTotal()));
            limpiarCarrito();
            cargarHistorial();
        } catch (TiendaException e) {
            EstiloTienda.mostrarError(this, e);
        }
    }

    private void limpiarCarrito() {
        carrito.clear();
        modeloCarrito.setRowCount(0);
        lblSubtotal.setText("Subtotal: $0");
        lblIva.setText("IVA:      $0");
        lblTotal.setText("TOTAL:  $0");
    }

    public void cargarHistorial() {
        modeloHistorial.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Venta v : ventaService.listarVentas()) {
            modeloHistorial.addRow(new Object[]{
                v.getNumeroFactura(),
                v.getFechaHora().format(fmt),
                v.getCliente().getNombreCompleto(),
                v.getDetalles().size(),
                String.format("$%,.0f", v.getSubtotal()),
                String.format("$%,.0f", v.getIVA()),
                String.format("$%,.0f", v.getTotal()),
                v.getFormaPago().name(),
                v.getEstado().name()
            });
        }
    }

    public void refrescarCombos() {
        cmbCliente.removeAllItems();
        clienteService.listarActivos().forEach(c ->
            cmbCliente.addItem(c.getCodigo() + " - " + c.getNombreCompleto()));

        cmbProducto.removeAllItems();
        productoService.listarActivos().forEach(p ->
            cmbProducto.addItem(p.getCodigo() + " - " + p.getNombre()));
    }
}
