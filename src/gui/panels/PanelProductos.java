package gui.panels;

import exception.TiendaException;
import gui.EstiloTienda;
import model.Producto;
import model.Producto.Categoria;
import service.IProductoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de Productos — CRUD completo.
 */
public class PanelProductos extends JPanel {

    private final IProductoService service;

    // Formulario
    private JTextField txtCodigo, txtNombre, txtPrecioCompra, txtPrecioVenta;
    private JTextField txtStockActual, txtStockMinimo, txtStockMaximo;
    private JComboBox<Categoria> cmbCategoria;

    // Tabla
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    // Estado
    private boolean modoEdicion = false;

    public PanelProductos(IProductoService service) {
        this.service = service;
        construirUI();
        cargarTabla();
    }

    private void construirUI() {
        setBackground(EstiloTienda.FONDO);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(construirFormulario(), BorderLayout.NORTH);
        add(construirTablaPanel(), BorderLayout.CENTER);
        add(construirBotones(), BorderLayout.SOUTH);
    }

    // ── Formulario ────────────────────────────────────────────────
    private JPanel construirFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstiloTienda.PANEL);
        p.setBorder(EstiloTienda.bordeTitulado("📦  Datos del Producto"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        txtCodigo       = EstiloTienda.campo(10);
        txtNombre       = EstiloTienda.campo(20);
        cmbCategoria    = new JComboBox<>(Categoria.values());
        cmbCategoria.setBackground(EstiloTienda.PANEL_CLARO);
        cmbCategoria.setForeground(EstiloTienda.TEXTO);
        cmbCategoria.setFont(EstiloTienda.FUENTE_NORMAL);
        txtPrecioCompra = EstiloTienda.campo(10);
        txtPrecioVenta  = EstiloTienda.campo(10);
        txtStockActual  = EstiloTienda.campo(8);
        txtStockMinimo  = EstiloTienda.campo(8);
        txtStockMaximo  = EstiloTienda.campo(8);

        // Fila 0
        gc.gridx=0; gc.gridy=0; p.add(EstiloTienda.label("Código:"), gc);
        gc.gridx=1;             p.add(txtCodigo, gc);
        gc.gridx=2;             p.add(EstiloTienda.label("Nombre:"), gc);
        gc.gridx=3; gc.weightx=1; p.add(txtNombre, gc); gc.weightx=0;
        gc.gridx=4;             p.add(EstiloTienda.label("Categoría:"), gc);
        gc.gridx=5;             p.add(cmbCategoria, gc);

        // Fila 1
        gc.gridx=0; gc.gridy=1; p.add(EstiloTienda.label("P. Compra ($):"), gc);
        gc.gridx=1;             p.add(txtPrecioCompra, gc);
        gc.gridx=2;             p.add(EstiloTienda.label("P. Venta ($):"), gc);
        gc.gridx=3;             p.add(txtPrecioVenta, gc);

        // Fila 2
        gc.gridx=0; gc.gridy=2; p.add(EstiloTienda.label("Stock actual:"), gc);
        gc.gridx=1;             p.add(txtStockActual, gc);
        gc.gridx=2;             p.add(EstiloTienda.label("Stock mínimo:"), gc);
        gc.gridx=3;             p.add(txtStockMinimo, gc);
        gc.gridx=4;             p.add(EstiloTienda.label("Stock máximo:"), gc);
        gc.gridx=5;             p.add(txtStockMaximo, gc);

        return p;
    }

    // ── Tabla ──────────────────────────────────────────────────────
    private JScrollPane construirTablaPanel() {
        String[] cols = {"Código","Nombre","Categoría","P.Compra","P.Venta",
                         "Stock","Mín","Máx","Estado","Alerta"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = EstiloTienda.tabla(cols);
        tabla.setModel(modeloTabla);

        // Al seleccionar fila → cargar formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFilaSeleccionada();
        });

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBackground(EstiloTienda.PANEL);
        sp.getViewport().setBackground(EstiloTienda.PANEL);
        sp.setBorder(EstiloTienda.bordeTitulado("📋  Inventario de Productos"));
        return sp;
    }

    // ── Botones ────────────────────────────────────────────────────
    private JPanel construirBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setBackground(EstiloTienda.FONDO);

        JButton btnGuardar   = EstiloTienda.botonPrimario("💾  Guardar");
        JButton btnEditar    = EstiloTienda.botonSecundario("✏  Editar");
        JButton btnInactivar = EstiloTienda.botonPeligro("🚫  Inactivar");
        JButton btnLimpiar   = EstiloTienda.botonSecundario("🔄  Limpiar");
        JButton btnBajoStock = EstiloTienda.botonSecundario("⚠  Bajo Stock");

        btnGuardar.addActionListener(e   -> guardar());
        btnEditar.addActionListener(e    -> activarEdicion());
        btnInactivar.addActionListener(e -> inactivar());
        btnLimpiar.addActionListener(e   -> limpiarFormulario());
        btnBajoStock.addActionListener(e -> mostrarBajoStock());

        p.add(btnGuardar); p.add(btnEditar); p.add(btnInactivar);
        p.add(btnLimpiar); p.add(btnBajoStock);
        return p;
    }

    // ── Lógica ────────────────────────────────────────────────────
    private void guardar() {
        try {
            Producto p = leerFormulario();
            if (modoEdicion) {
                service.modificarProducto(p);
                EstiloTienda.mostrarExito(this, "Producto actualizado correctamente.");
            } else {
                service.registrarProducto(p);
                EstiloTienda.mostrarExito(this, "Producto registrado correctamente.");
            }
            limpiarFormulario();
            cargarTabla();
        } catch (TiendaException ex) {
            EstiloTienda.mostrarError(this, ex);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Verifique que los campos numéricos contengan solo números.",
                "⚠ Formato inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void activarEdicion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un producto de la tabla para editar.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtCodigo.setEditable(false); // no se puede cambiar el código al editar
        modoEdicion = true;
    }

    private void inactivar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        int ok = JOptionPane.showConfirmDialog(this,
                "¿Inactivar el producto " + codigo + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try {
                service.inactivarProducto(codigo);
                cargarTabla();
            } catch (TiendaException ex) { EstiloTienda.mostrarError(this, ex); }
        }
    }

    private void mostrarBajoStock() {
        List<Producto> bajos = service.listarBajoStock();
        if (bajos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "✅  Todos los productos tienen stock suficiente.",
                "Stock OK", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Productos con stock bajo:\n\n");
            for (Producto prod : bajos)
                sb.append("  • ").append(prod.getNombre())
                  .append(" — Stock: ").append(prod.getStockActual())
                  .append(" / Mínimo: ").append(prod.getStockMinimo()).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString(),
                "⚠  Alerta de Stock", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Producto leerFormulario() {
        return new Producto(
            txtCodigo.getText().trim(),
            txtNombre.getText().trim(),
            (Categoria) cmbCategoria.getSelectedItem(),
            Double.parseDouble(txtPrecioCompra.getText().trim()),
            Double.parseDouble(txtPrecioVenta.getText().trim()),
            Integer.parseInt(txtStockActual.getText().trim()),
            Integer.parseInt(txtStockMinimo.getText().trim()),
            Integer.parseInt(txtStockMaximo.getText().trim())
        );
    }

    private void cargarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtCodigo.setText((String) modeloTabla.getValueAt(fila, 0));
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        String cat = (String) modeloTabla.getValueAt(fila, 2);
        cmbCategoria.setSelectedItem(Categoria.valueOf(cat));
        txtPrecioCompra.setText(modeloTabla.getValueAt(fila, 3).toString().replace("$","").replace(",","").trim());
        txtPrecioVenta.setText(modeloTabla.getValueAt(fila, 4).toString().replace("$","").replace(",","").trim());
        txtStockActual.setText(modeloTabla.getValueAt(fila, 5).toString());
        txtStockMinimo.setText(modeloTabla.getValueAt(fila, 6).toString());
        txtStockMaximo.setText(modeloTabla.getValueAt(fila, 7).toString());
        modoEdicion = false;
    }

    private void limpiarFormulario() {
        txtCodigo.setText(""); txtNombre.setText("");
        txtPrecioCompra.setText(""); txtPrecioVenta.setText("");
        txtStockActual.setText(""); txtStockMinimo.setText(""); txtStockMaximo.setText("");
        cmbCategoria.setSelectedIndex(0);
        txtCodigo.setEditable(true);
        modoEdicion = false;
        tabla.clearSelection();
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : service.listarActivos()) {
            modeloTabla.addRow(new Object[]{
                p.getCodigo(), p.getNombre(), p.getCategoria().name(),
                String.format("$%,.0f", p.getPrecioCompra()),
                String.format("$%,.0f", p.getPrecioVenta()),
                p.getStockActual(), p.getStockMinimo(), p.getStockMaximo(),
                p.isActivo() ? "Activo" : "Inactivo",
                p.isBajoStock() ? "⚠ BAJO" : "✓ OK"
            });
        }
    }
}
