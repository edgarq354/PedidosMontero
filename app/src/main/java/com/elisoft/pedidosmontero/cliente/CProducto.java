package com.elisoft.pedidosmontero.cliente;

public class CProducto {
    private int Id_producto;
    private String Nombre;
    private String Precio_unit;
    private String Direccion_imagen;
    private int id_empresa;

    CProducto(){
        setId_producto(0);
        setNombre("");
        setPrecio_unit("0");
        setDireccion_imagen("");
        setId_empresa(0);
    }
    public CProducto(int Id_producto,
                     String Nombre,
                     String Precio_unit,
                     String Direccion_imagen,
                     int id_empresa){
        this.setId_producto(Id_producto);
        this.setNombre(Nombre);
        this.setPrecio_unit(Precio_unit);
        this.setDireccion_imagen(Direccion_imagen);
        this.setId_empresa(id_empresa);
    }

    public int getId_producto() {
        return Id_producto;
    }

    public void setId_producto(int id_producto) {
        Id_producto = id_producto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPrecio_unit() {
        return Precio_unit;
    }

    public void setPrecio_unit(String precio_unit) {
        Precio_unit = precio_unit;
    }

    public String getDireccion_imagen() {
        return Direccion_imagen;
    }

    public void setDireccion_imagen(String direccion_imagen) {
        Direccion_imagen = direccion_imagen;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }
}
