/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;


import Conector.DBConnection;
import Interfaces.IReporteCompras;
import Modelo.ModeloReporteCompras.CompraFechaDTO;
import Modelo.ModeloReporteCompras.CompraProveedorDTO;
import Modelo.ModeloReporteCompras.ModeloItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteCompraImpl implements IReporteCompras{

    @Override
    public List<ModeloItem> listarProveedores() {
        String sql = """
            SELECT id_proveedor,
                   COALESCE(razon_social, nit) AS nombre
            FROM proveedores
            WHERE estado IS NULL OR UPPER(estado) <> 'INACTIVO'
            ORDER BY nombre
        """;
        List<ModeloItem> out = new ArrayList<>();
        DBConnection db = new DBConnection();
        try {
            db.conectar();
            try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new ModeloItem(rs.getLong(1), rs.getString(2)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.desconectar();
        }
        return out;
    }

    @Override
    public List<CompraFechaDTO> comprasPorRango(Date desde, Date hasta) {
        String sql = """
            SELECT c.id_compra,
                   c.fecha,
                   COALESCE(p.razon_social, p.nit) AS proveedor,
                   c.forma_pago,
                   c.total_bruto,
                   COALESCE(x.saldo_pendiente, 0) AS saldo
            FROM compras c
            JOIN proveedores p ON p.id_proveedor = c.id_proveedor
            LEFT JOIN cuenta_por_pagar x ON x.id_compra = c.id_compra
            WHERE c.fecha BETWEEN ? AND ?
            ORDER BY c.fecha, c.id_compra
        """;
        List<CompraFechaDTO> out = new ArrayList<>();
        DBConnection db = new DBConnection();
        try {
            db.conectar();
            try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
                ps.setDate(1, desde);
                ps.setDate(2, hasta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(new CompraFechaDTO(
                                rs.getLong(1),
                                rs.getDate(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getBigDecimal(5),
                                rs.getBigDecimal(6)
                        ));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.desconectar();
        }
        return out;
    }

    @Override
    public List<CompraProveedorDTO> comprasPorProveedor(long idProveedor, Date desde, Date hasta) {
        String sql = """
            SELECT c.id_compra,
                   c.fecha,
                   c.forma_pago,
                   c.total_bruto,
                   COALESCE(x.monto_total, c.total_bruto) AS monto_total,
                   COALESCE(x.saldo_pendiente, 0) AS saldo_pendiente
            FROM compras c
            LEFT JOIN cuenta_por_pagar x ON x.id_compra = c.id_compra
            WHERE c.id_proveedor = ?
              AND ( ? IS NULL OR c.fecha >= ? )
              AND ( ? IS NULL OR c.fecha <= ? )
            ORDER BY c.fecha, c.id_compra
        """;
        List<CompraProveedorDTO> out = new ArrayList<>();
        DBConnection db = new DBConnection();
        try {
            db.conectar();
            try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
                ps.setLong(1, idProveedor);

                if (desde == null) { ps.setNull(2, Types.DATE); ps.setNull(3, Types.DATE); }
                else { ps.setDate(2, desde); ps.setDate(3, desde); }

                if (hasta == null) { ps.setNull(4, Types.DATE); ps.setNull(5, Types.DATE); }
                else { ps.setDate(4, hasta); ps.setDate(5, hasta); }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        BigDecimal montoTotal = rs.getBigDecimal(5);
                        BigDecimal saldo = rs.getBigDecimal(6);
                        BigDecimal pagado = (montoTotal == null ? BigDecimal.ZERO : montoTotal)
                                .subtract(saldo == null ? BigDecimal.ZERO : saldo);

                        out.add(new CompraProveedorDTO(
                                rs.getLong(1),
                                rs.getDate(2),
                                rs.getString(3),
                                rs.getBigDecimal(4),
                                pagado,
                                saldo
                        ));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.desconectar();
        }
        return out;
    }
}
