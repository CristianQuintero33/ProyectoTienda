package gui.panels;

import exception.TiendaException;
import gui.EstiloTienda;
import model.Cliente;
import model.Cliente.TipoCliente;
import model.Cliente.TipoIdentificacion;
import service.IClienteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelClientes extends JPanel {

    private final IClienteService service;

    private JTextField txtCodigo, txtNombre, txtNumeroId, txtDireccion, txtTelefono;
    private JComboBox<TipoIdentificacion> cmbTipoId;
    private JComboBox<TipoCliente>        cmbTipoCliente;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private boolean modoEdicion = false;

    public PanelClientes(IClienteService service) {
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

    private JPanel construirFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstiloTienda.PANEL);
        p.setBorder(EstiloTienda.bordeTitulado("👤  Datos del Cliente"));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 8, 6, 8);
        gc.fill   = GridBagConstraints.HORIZONTAL;

        txtCodigo     = EstiloTienda.campo(10);
        txtNombre     = EstiloTienda.campo(24);
        cmbTipoId     = new JComboBox<>(TipoIdentificacion.values());
        cmbTipoId.setBackground(EstiloTienda.PANEL_CLARO);
        cmbTipoId.setForeground(EstiloTienda.TEXTO);
        cmbTipoId.setFont(EstiloTienda.FUENTE_NORMAL);
        txtNumeroId   = EstiloTienda.campo(14);
        txtDireccion  = EstiloTienda.campo(20);
        txtTelefono   = EstiloTienda.campo(12);
        cmbTipoCliente = new JComboBox<>(TipoCliente.values());
        cmbTipoCliente.setBackground(EstiloTienda.PANEL_CLARO);
        cmbTipoCliente.setForeground(EstiloTienda.TEXTO);
        cmbTipoCliente.setFont(EstiloTienda.FUENTE_NORMAL);

        gc.gridx=0; gc.gridy=0; p.add(EstiloTienda.label("Código:"), gc);
        gc.gridx=1;             p.add(txtCodigo, gc);
        gc.gridx=2;             p.add(EstiloTienda.label("Nombre completo:"), gc);
        gc.gridx=3; gc.weightx=1; p.add(txtNombre, gc); gc.weightx=0;

        gc.gridx=0; gc.gridy=1; p.add(EstiloTienda.label("Tipo ID:"), gc);
        gc.gridx=1;             p.add(cmbTipoId, gc);
        gc.gridx=2;             p.add(EstiloTienda.label("N° Identificación:"), gc);
        gc.gridx=3;             p.add(txtNumeroId, gc);

        gc.gridx=0; gc.gridy=2; p.add(EstiloTienda.label("Dirección:"), gc);
        gc.gridx=1; gc.gridwidth=2; p.add(txtDireccion, gc); gc.gridwidth=1;
        gc.gridx=3;             p.add(EstiloTienda.label("Teléfono:"), gc);
        gc.gridx=4;             p.add(txtTelefono, gc);
        gc.gridx=5;             p.add(EstiloTienda.label("Tipo cliente:"), gc);
        gc.gridx=6;             p.add(cmbTipoCliente, gc);

        return p;
    }

    private JScrollPane construirTablaPanel() {
        String[] cols = {"Código","Nombre","Tipo ID","N° ID","Dirección","Teléfono","Tipo","Estado"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = EstiloTienda.tabla(cols);
        tabla.setModel(modeloTabla);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarFilaSeleccionada();
        });

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBackground(EstiloTienda.PANEL);
        sp.getViewport().setBackground(EstiloTienda.PANEL);
        sp.setBorder(EstiloTienda.bordeTitulado("📋  Listado de Clientes"));
        return sp;
    }

    private JPanel construirBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setBackground(EstiloTienda.FONDO);

        JButton btnGuardar   = EstiloTienda.botonPrimario("💾  Guardar");
        JButton btnEditar    = EstiloTienda.botonSecundario("✏  Editar");
        JButton btnInactivar = EstiloTienda.botonPeligro("🚫  Inactivar");
        JButton btnLimpiar   = EstiloTienda.botonSecundario("🔄  Limpiar");

        btnGuardar.addActionListener(e   -> guardar());
        btnEditar.addActionListener(e    -> { modoEdicion = true; txtCodigo.setEditable(false); });
        btnInactivar.addActionListener(e -> inactivar());
        btnLimpiar.addActionListener(e   -> limpiarFormulario());

        p.add(btnGuardar); p.add(btnEditar); p.add(btnInactivar); p.add(btnLimpiar);
        return p;
    }

    private void guardar() {
        try {
            Cliente c = leerFormulario();
            if (modoEdicion) { service.modificarCliente(c); EstiloTienda.mostrarExito(this, "Cliente actualizado."); }
            else              { service.registrarCliente(c); EstiloTienda.mostrarExito(this, "Cliente registrado."); }
            limpiarFormulario(); cargarTabla();
        } catch (TiendaException ex) { EstiloTienda.mostrarError(this, ex); }
    }

    private void inactivar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        String codigo = (String) modeloTabla.getValueAt(fila, 0);
        int ok = JOptionPane.showConfirmDialog(this, "¿Inactivar cliente " + codigo + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try { service.inactivarCliente(codigo); cargarTabla(); }
            catch (TiendaException ex) { EstiloTienda.mostrarError(this, ex); }
        }
    }

    private Cliente leerFormulario() {
        return new Cliente(
            txtCodigo.getText().trim(),
            txtNombre.getText().trim(),
            (TipoIdentificacion) cmbTipoId.getSelectedItem(),
            txtNumeroId.getText().trim(),
            txtDireccion.getText().trim(),
            txtTelefono.getText().trim(),
            (TipoCliente) cmbTipoCliente.getSelectedItem()
        );
    }

    private void cargarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtCodigo.setText((String) modeloTabla.getValueAt(fila, 0));
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        cmbTipoId.setSelectedItem(TipoIdentificacion.valueOf((String) modeloTabla.getValueAt(fila, 2)));
        txtNumeroId.setText((String) modeloTabla.getValueAt(fila, 3));
        txtDireccion.setText((String) modeloTabla.getValueAt(fila, 4));
        txtTelefono.setText((String) modeloTabla.getValueAt(fila, 5));
        cmbTipoCliente.setSelectedItem(TipoCliente.valueOf((String) modeloTabla.getValueAt(fila, 6)));
        modoEdicion = false;
    }

    private void limpiarFormulario() {
        txtCodigo.setText(""); txtNombre.setText(""); txtNumeroId.setText("");
        txtDireccion.setText(""); txtTelefono.setText("");
        cmbTipoId.setSelectedIndex(0); cmbTipoCliente.setSelectedIndex(0);
        txtCodigo.setEditable(true); modoEdicion = false; tabla.clearSelection();
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        for (Cliente c : service.listarActivos()) {
            modeloTabla.addRow(new Object[]{
                c.getCodigo(), c.getNombreCompleto(),
                c.getTipoIdentificacion().name(), c.getNumeroIdentificacion(),
                c.getDireccion(), c.getTelefono(),
                c.getTipoCliente().name(),
                c.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }
}
