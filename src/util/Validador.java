package util;

import exception.CampoObligatorioException;
import java.util.regex.Pattern;

/**
 * Métodos de validación reutilizables — Semana 4.
 */
public class Validador {

    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^[0-9]{7,15}$");

    private static final Pattern PATRON_NIT =
            Pattern.compile("^[0-9]{8,12}(-[0-9])?$");

    public static void requerirTexto(String valor, String campo)
            throws CampoObligatorioException {
        if (valor == null || valor.trim().isEmpty())
            throw new CampoObligatorioException(campo);
    }

    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && PATRON_TELEFONO.matcher(telefono.trim()).matches();
    }

    public static boolean esNitValido(String nit) {
        return nit != null && PATRON_NIT.matcher(nit.trim()).matches();
    }

    public static boolean esNumeroPositivo(double valor) {
        return valor > 0;
    }

    public static boolean esEnteroPositivo(int valor) {
        return valor >= 0;
    }
}
