/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;


import Modelo.ModeloReporteProductos.ProdMasVendidoDTO;
import Modelo.ModeloReporteProductos.StockBajoDTO;


import java.sql.Date;
import java.util.List;

/**
 *
 * @author anyi4
 */
public interface IReporteProductos {
List<ProdMasVendidoDTO> listarMasVendidos(
            Date desde, Date hasta, Integer idCategoria,
            int topN, boolean ordenarPorMonto) throws Exception;

    List<StockBajoDTO> listarStockBajo(int minStock, Integer idCategoria) throws Exception;
}
