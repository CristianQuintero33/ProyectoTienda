import gui.VentanaPrincipal;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Intentar look and feel del sistema
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        UIManager.put("TextField.background", new Color(0x1a, 0x3d, 0x5c));
        UIManager.put("TextField.foreground", new Color(0xa8, 0xdf, 0xf0));
        UIManager.put("TextField.caretForeground", new Color(0x4d, 0xc3, 0xe8));
        UIManager.put("ComboBox.background", new Color(0x1a, 0x3d, 0x5c));
        UIManager.put("ComboBox.foreground", new Color(0xa8, 0xdf, 0xf0));
    }
    catch (Exception ignore) {}

    SwingUtilities.invokeLater(() -> {
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
    });
}
}
