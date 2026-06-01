package model;

public class Cliente {

    public enum TipoIdentificacion { CC, NIT, CE, PA }
    public enum TipoCliente        { MINORISTA, MAYORISTA }

    private String            codigo;
    private String            nombreCompleto;
    private TipoIdentificacion tipoIdentificacion;
    private String            numeroIdentificacion;
    private String            direccion;
    private String            telefono;
    private TipoCliente       tipoCliente;
    private boolean           activo;

    public Cliente() { this.activo = true; }

    public Cliente(String codigo, String nombreCompleto,
                   TipoIdentificacion tipoId, String numeroId,
                   String direccion, String telefono,
                   TipoCliente tipoCliente) {
        this.codigo               = codigo;
        this.nombreCompleto       = nombreCompleto;
        this.tipoIdentificacion   = tipoId;
        this.numeroIdentificacion = numeroId;
        this.direccion            = direccion;
        this.telefono             = telefono;
        this.tipoCliente          = tipoCliente;
        this.activo               = true;
    }

    public String             getCodigo()               { return codigo; }
    public String             getNombreCompleto()        { return nombreCompleto; }
    public TipoIdentificacion getTipoIdentificacion()   { return tipoIdentificacion; }
    public String             getNumeroIdentificacion() { return numeroIdentificacion; }
    public String             getDireccion()            { return direccion; }
    public String             getTelefono()             { return telefono; }
    public TipoCliente        getTipoCliente()          { return tipoCliente; }
    public boolean            isActivo()                { return activo; }

    public void setCodigo(String v)                          { this.codigo = v; }
    public void setNombreCompleto(String v)                  { this.nombreCompleto = v; }
    public void setTipoIdentificacion(TipoIdentificacion v)  { this.tipoIdentificacion = v; }
    public void setNumeroIdentificacion(String v)            { this.numeroIdentificacion = v; }
    public void setDireccion(String v)                       { this.direccion = v; }
    public void setTelefono(String v)                        { this.telefono = v; }
    public void setTipoCliente(TipoCliente v)                { this.tipoCliente = v; }
    public void setActivo(boolean v)                         { this.activo = v; }

    @Override public String toString() { return "[" + codigo + "] " + nombreCompleto; }
}
