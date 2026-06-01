package util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Sistema de logs del sistema — Semana 4.
 * Escribe en consola y en archivo TXT (logs del sistema según caso de estudio).
 */
public class LoggerTienda {

    private static final Logger LOGGER =
            Logger.getLogger("TiendaMinorista");

    static {
        LOGGER.setUseParentHandlers(false);
        // Handler consola
        ConsoleHandler consola = new ConsoleHandler();
        consola.setFormatter(new SimpleFormatter());
        consola.setLevel(Level.ALL);
        LOGGER.addHandler(consola);
        // Handler archivo TXT
        try {
            FileHandler archivo = new FileHandler("tienda_log.txt", true);
            archivo.setFormatter(new SimpleFormatter());
            archivo.setLevel(Level.ALL);
            LOGGER.addHandler(archivo);
        } catch (IOException e) {
            LOGGER.warning("No se pudo crear el archivo de log: " + e.getMessage());
        }
        LOGGER.setLevel(Level.ALL);
    }

    public static void info(String msg)        { LOGGER.info(msg); }
    public static void advertencia(String msg) { LOGGER.warning(msg); }
    public static void error(String msg, Exception e) { LOGGER.log(Level.SEVERE, msg, e); }
}
