package model;

public class Proveedor {

    private String  codigo;
    private String  razonSocial;
    private String  nit;
    private String  direccion;
    private String  telefono;
    private String  correo;
    private boolean activo;

    public Proveedor() { this.activo = true; }

    public Proveedor(String codigo, String razonSocial, String nit,
                     String direccion, String telefono, String correo) {
        this.codigo      = codigo;
        this.razonSocial = razonSocial;
        this.nit         = nit;
        this.direccion   = direccion;
        this.telefono    = telefono;
        this.correo      = correo;
        this.activo      = true;
    }

    public String  getCodigo()      { return codigo; }
    public String  getRazonSocial() { return razonSocial; }
    public String  getNit()         { return nit; }
    public String  getDireccion()   { return direccion; }
    public String  getTelefono()    { return telefono; }
    public String  getCorreo()      { return correo; }
    public boolean isActivo()       { return activo; }

    public void setCodigo(String v)      { this.codigo = v; }
    public void setRazonSocial(String v) { this.razonSocial = v; }
    public void setNit(String v)         { this.nit = v; }
    public void setDireccion(String v)   { this.direccion = v; }
    public void setTelefono(String v)    { this.telefono = v; }
    public void setCorreo(String v)      { this.correo = v; }
    public void setActivo(boolean v)     { this.activo = v; }

    @Override public String toString() { return "[" + nit + "] " + razonSocial; }
}
