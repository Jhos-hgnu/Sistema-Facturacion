/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;


import java.sql.Date;
import Modelo.ModeloMovInventario;
import Modelo.ModeloReporteCompras.ModeloItem;

import java.util.List;

/**
 *
 * @author anyi4
 */
// Interfaces/IInventarioMov.java



public interface IInventarioMov {
    List<ModeloItem> listarProductos(String filtroNombreOCodigo);
    List<ModeloItem> listarUsuarios(String filtroNombre);

    List<ModeloMovInventario> buscarMovimientos(
        Long idProducto,
        String codigoBarra,
        Long idUsuario,
        Date desde,           
        Date hasta,            
        String tipo,
        boolean soloObs
    );
}

    
