/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Implementacion;

import Conector.DBConnection;
import Interfaces.IInventarioMov;
import Modelo.ModeloMovInventario;

import Modelo.ModeloReporteCompras.ModeloItem;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author anyi4
 */



public class InventarioMovImpl implements IInventarioMov {

    public List<ModeloItem> listarProductos() { return listarProductos(null); }
    public List<ModeloItem> listarUsuarios()  { return listarUsuarios(null);  }

    private static final String SQL_LISTAR_PROD = 
        "SELECT p.id_producto, " +
        "       p.nombre_producto AS nombre " +
        "FROM   productos p " +
        "WHERE  ( ? IS NULL " +
        "         OR LOWER(p.nombre_producto) LIKE '%' || LOWER(?) || '%' ) " +
        "ORDER BY p.nombre_producto";

    private static final String SQL_LISTAR_USU =
        "SELECT u.id_usuario, " +
        "       TRIM(NVL(u.primer_nombre,'') || ' ' || NVL(u.primer_apellido,'')) AS nombre " +
        "FROM   Usuarios u " +
        "WHERE  ( ? IS NULL " +
        "         OR LOWER(u.primer_nombre)  LIKE '%' || LOWER(?) || '%' " +
        "         OR LOWER(u.primer_apellido) LIKE '%' || LOWER(?) || '%' ) " +
        "ORDER BY nombre";

    // NOTA: codigoBarra ya no existe; se ignora en el WHERE
    private static final String SQL_BUSCAR_MOV =
     
"SELECT " +
"  i.id_inventario,        " + 
"  i.fecha,                " + 
"  p.nombre_producto,      " + 
"  i.movimiento,           " + 
"  NVL(i.motivo,'') AS motivo, " + 
"  i.cantidad,             " + // 6
"  i.stock_anterior,       " + // 7
"  i.stock_actual,         " + // 8
"  TRIM(NVL(u.primer_nombre,'') || ' ' || NVL(u.primer_apellido,'')) AS usuario, " + // 9
"  p.id_producto           " + // 10
"FROM inventario i " +
"JOIN productos p ON p.id_producto = i.id_producto " +
"LEFT JOIN Usuarios u ON u.id_usuario = i.id_usuario " +
"WHERE ( ? IS NULL OR i.id_producto = ? ) " +            // 1-2  idProducto
"  AND ( ? IS NULL OR i.id_usuario  = ? ) " +            // 3-4  idUsuario
"  AND ( ? IS NULL OR i.fecha >= ? ) " +                 // 5-6  desde
"  AND ( ? IS NULL OR i.fecha <  ? + 1 ) " +             // 7-8  hasta (inclusive)
"  AND ( ? = 'TODOS' OR UPPER(i.movimiento) = UPPER(?) ) " + // 9-10 tipo
"  AND ( ? = 0 OR (i.motivo IS NOT NULL AND TRIM(i.motivo) <> '') ) " + // 11 soloObs
"ORDER BY i.fecha DESC, i.id_inventario DESC";

    

    @Override
    public List<ModeloItem> listarProductos(String filtroNombre) {
        List<ModeloItem> out = new ArrayList<>();
        DBConnection db = new DBConnection();
        try {
            db.conectar();
            try (PreparedStatement ps = db.getConnection().prepareStatement(SQL_LISTAR_PROD)) {
                if (filtroNombre == null || filtroNombre.isBlank()) {
                    ps.setNull(1, Types.VARCHAR);
                    ps.setNull(2, Types.VARCHAR);
                } else {
                    ps.setString(1, filtroNombre);
                    ps.setString(2, filtroNombre);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(new ModeloItem(rs.getLong(1), rs.getString(2)));
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
    public List<ModeloItem> listarUsuarios(String filtroNombre) {
        List<ModeloItem> out = new ArrayList<>();
        DBConnection db = new DBConnection();
        try {
            db.conectar();
            try (PreparedStatement ps = db.getConnection().prepareStatement(SQL_LISTAR_USU)) {
                if (filtroNombre == null || filtroNombre.isBlank()) {
                    ps.setNull(1, Types.VARCHAR);
                    ps.setNull(2, Types.VARCHAR);
                    ps.setNull(3, Types.VARCHAR);
                } else {
                    ps.setString(1, filtroNombre);
                    ps.setString(2, filtroNombre);
                    ps.setString(3, filtroNombre);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(new ModeloItem(rs.getLong(1), rs.getString(2)));
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
    public List<ModeloMovInventario> buscarMovimientos(
            Long idProducto, String codigoBarra, Long idUsuario,
            java.sql.Date desde, java.sql.Date hasta, String tipo, boolean soloObs) {

        // codigoBarra se ignora porque no existe en la BD actual
        List<ModeloMovInventario> out = new ArrayList<>();
        DBConnection db = new DBConnection();

        try {
            db.conectar();
            try (PreparedStatement ps = db.getConnection().prepareStatement(SQL_BUSCAR_MOV)) {

                // 1-2: idProducto
                if (idProducto == null) { 
                    ps.setNull(1, Types.NUMERIC); 
                    ps.setNull(2, Types.NUMERIC); 
                } else { 
                    ps.setLong(1, idProducto); 
                    ps.setLong(2, idProducto); 
                }

                // 3-4: idUsuario
                if (idUsuario == null) { 
                    ps.setNull(3, Types.NUMERIC); 
                    ps.setNull(4, Types.NUMERIC); 
                } else { 
                    ps.setLong(3, idUsuario); 
                    ps.setLong(4, idUsuario); 
                }

                // 5-6: desde (inclusive)
                if (desde == null) {
                    ps.setNull(5, Types.DATE);
                    ps.setNull(6, Types.DATE);
                } else {
                    ps.setDate(5, desde);
                    ps.setDate(6, desde);
                }

                // 7-8: hasta (inclusive día completo)
                if (hasta == null) {
                    ps.setNull(7, Types.DATE);
                    ps.setNull(8, Types.DATE);
                } else {
                    ps.setDate(7, hasta);
                    ps.setDate(8, hasta);
                }

                // 9-10: tipo (TODOS para no filtrar)
                String t = (tipo == null || tipo.isBlank()) ? "TODOS" : tipo;
                ps.setString(9, t);
                ps.setString(10, t);

                // 11: soloObs (1 = sólo con motivo; 0 = todos)
                ps.setInt(11, soloObs ? 1 : 0);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(new ModeloMovInventario(
                            rs.getLong(1),       // id_inventario
                            rs.getTimestamp(2),  // fecha
                            rs.getString(3),     // nombre_producto
                            rs.getString(4),     // movimiento
                            rs.getString(5),     // motivo
                            rs.getBigDecimal(6), // cantidad
                            rs.getBigDecimal(7), // stock_anterior
                            rs.getBigDecimal(8), // stock_actual
                            rs.getString(9),     // usuario (nombre)
                            rs.getString(10)     // id_producto como String
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
