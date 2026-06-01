package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class EstiloTienda {

    // ── Colores ──────────────────────────────────────────────────
                                              public static final Color FONDO        = new Color(15, 20, 35);
    public static final Color PANEL        = new Color(20, 28, 48);
    public static final Color PANEL_CLARO  = new Color(28, 40, 65);
    public static final Color BORDE        = new Color(50, 70, 110);
    public static final Color ACENTO       = new Color(50, 185, 255);   
    public static final Color ACENTO2      = new Color(0, 230, 160);   
    public static final Color ROJO         = new Color(220, 60, 60);
    public static final Color TEXTO        = new Color(210, 225, 255);
    public static final Color TEXTO_SUAVE  = new Color(120, 140, 180);

    // ── Fuentes ──────────────────────────────────────────────────
    public static final Font FUENTE_TITULO  = new Font("Monospaced", Font.BOLD,  18);
    public static final Font FUENTE_SUBTIT  = new Font("Monospaced", Font.BOLD,  13);
    public static final Font FUENTE_NORMAL  = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font FUENTE_PEQUENA = new Font("Monospaced", Font.PLAIN, 11);
	private static final Color PANEL_OSCURO = null;

    // ── Componentes helpers ───────────────────────────────────────
    public static JLabel labelTitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_TITULO);
        l.setForeground(ACENTO);
        return l;
    }

    public static JLabel labelSubtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_SUBTIT);
        l.setForeground(TEXTO);
        return l;
    }

    public static JLabel label(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_NORMAL);
        l.setForeground(TEXTO);
        return l;
    }

    public static JTextField campo(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setBackground(new Color(0x1a, 0x3d, 0x5c));
        tf.setForeground(TEXTO);
        tf.setCaretColor(ACENTO);
        tf.setFont(FUENTE_NORMAL);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x2a, 0x60, 0x80)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return tf;
    
    }

    public static JComboBox<?> combo(Object[] opciones) {
        JComboBox<Object> cb = new JComboBox<>(opciones);
        cb.setBackground(new Color(0x1a, 0x3d, 0x5c));
        cb.setForeground(TEXTO);
        cb.setFont(FUENTE_NORMAL);
        return cb;
    
    }

    public static JButton botonPrimario(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(ACENTO);
        b.setForeground(Color.BLACK);
        b.setFont(FUENTE_SUBTIT);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  
        return b;
    }

    public static JButton botonSecundario(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(PANEL_CLARO);
        b.setForeground(TEXTO);
        b.setFont(FUENTE_NORMAL);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton botonPeligro(String texto) {
        JButton b = botonSecundario(texto);
        b.setForeground(ROJO);
        return b;
    }

    public static JTable tabla(String[] cabeceras) {
        JTable t = new JTable();
        t.setBackground(PANEL);
        t.setForeground(TEXTO);
        t.setFont(FUENTE_NORMAL);
        t.setGridColor(BORDE);
        t.setRowHeight(26);
        t.setSelectionBackground(new Color(255, 185, 0, 60));
        t.setSelectionForeground(TEXTO);
        t.getTableHeader().setBackground(PANEL_OSCURO);
        t.getTableHeader().setForeground(ACENTO);
        t.getTableHeader().setFont(FUENTE_SUBTIT);
        return t;
    }

    public static Border bordeTitulado(String titulo) {
        javax.swing.border.TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE), titulo);
        tb.setTitleFont(FUENTE_SUBTIT);
        tb.setTitleColor(ACENTO);
        return tb;
    }

    public static void aplicarFondo(JPanel p) {
        p.setBackground(FONDO);
    }

    public static void mostrarError(Component parent, Exception e) {
        JOptionPane.showMessageDialog(parent,
                e.getMessage(), "⚠ Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarExito(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent,
                msg, "✅ Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
