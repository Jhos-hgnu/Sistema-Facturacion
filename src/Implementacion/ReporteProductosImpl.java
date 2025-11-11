/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
   package Implementacion;

import Interfaces.IReporteProductos;
import Modelo.ModeloReporteProductos.ProdMasVendidoDTO;
import Modelo.ModeloReporteProductos.StockBajoDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anyi4
 */
public class ReporteProductosImpl  implements IReporteProductos{


    private final Connection cn;

    public  ReporteProductosImpl (Connection cn) { this.cn = cn; }

    @Override
    public List<ProdMasVendidoDTO> listarMasVendidos(
            Date d1, Date d2, Integer idCat, int top, boolean porMonto) throws Exception {

        String orderBy = porMonto ? " total_monto DESC " : " total_cant DESC ";

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( ");
        sql.append("  SELECT p.id_producto, p.nombre_producto, p.marca, ");
        sql.append("         SUM(dv.cantidad) AS total_cant, ");
        sql.append("         SUM((dv.precio_venta - NVL(dv.descuento,0)) * dv.cantidad) AS total_monto ");
        sql.append("  FROM detalle_venta dv ");
        sql.append("  JOIN venta v     ON v.id_venta = dv.id_venta ");
        sql.append("  JOIN productos p ON p.id_producto = dv.id_producto ");
        sql.append("  WHERE 1=1 ");
        if (d1 != null) sql.append(" AND v.fecha >= ? ");
        if (d2 != null) sql.append(" AND v.fecha <  ? + 1 ");
        if (idCat != null) sql.append(" AND p.id_categoria = ? ");
        sql.append("  GROUP BY p.id_producto, p.nombre_producto, p.marca ");
        sql.append("  ORDER BY ").append(orderBy);
        sql.append(") WHERE ROWNUM <= ?");

        try (PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            int i=1;
            if (d1 != null) ps.setDate(i++, d1);
            if (d2 != null) ps.setDate(i++, d2);
            if (idCat != null) ps.setInt(i++, idCat);
            ps.setInt(i++, top);

            List<ProdMasVendidoDTO> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new ProdMasVendidoDTO(
                            rs.getLong("id_producto"),
                            rs.getString("nombre_producto"),
                            rs.getString("marca"),
                            rs.getBigDecimal("total_cant"),
                            rs.getBigDecimal("total_monto")));
                }
            }
            return out;
        }
    }

    @Override
    public List<StockBajoDTO> listarStockBajo(int minStock, Integer idCat) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id_producto, p.nombre_producto, p.marca, p.stock ");
        sql.append("FROM productos p WHERE p.stock <= ? ");
        if (idCat != null) sql.append(" AND p.id_categoria = ? ");
        sql.append("ORDER BY p.stock ASC, p.nombre_producto");

        try (PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            int i=1;
            ps.setInt(i++, minStock);
            if (idCat != null) ps.setInt(i++, idCat);

            List<StockBajoDTO> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new StockBajoDTO(
                            rs.getLong("id_producto"),
                            rs.getString("nombre_producto"),
                            rs.getString("marca"),
                            rs.getInt("stock")));
                }
            }
            return out;
        }
    }


}
