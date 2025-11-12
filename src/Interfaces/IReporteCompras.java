/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;


import Modelo.ModeloReporteCompras;
import java.sql.Date;


import java.util.List;

/**
 *
 * @author anyi4
 */
public interface IReporteCompras {

    List<ModeloReporteCompras.ModeloItem> listarProveedores();

    List<ModeloReporteCompras.CompraFechaDTO> comprasPorRango(Date desde, Date hasta);

    List<ModeloReporteCompras.CompraProveedorDTO> comprasPorProveedor(
            long idProveedor, Date desde, Date hasta);
}


