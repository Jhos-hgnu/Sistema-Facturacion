package Implementacion;

import Conector.DBConnection;
import Interfaces.IAuditoria;
import Interfaces.IProducto;
import Modelo.ModeloAuditoria;
import Modelo.ModeloProducto;
import Modelo.ModeloVistaInicio;
import java.math.BigDecimal;

import java.sql.*;



public class ProductoImp implements IProducto {

    private final DBConnection conexion = new DBConnection();
    private final IAuditoria auditoriaDAO = new AuditoriaImpl();

  
    private ModeloProducto map(ResultSet rs) throws SQLException {
        ModeloProducto p = new ModeloProducto();
        p.setIdProducto(rs.getBigDecimal("ID_PRODUCTO"));
        p.setNombreProducto(rs.getString("NOMBRE_PRODUCTO"));
        p.setMarca(rs.getString("MARCA"));
        p.setDescripcion(rs.getString("DESCRIPCION"));
        p.setPrecioCosto(rs.getBigDecimal("PRECIO_COSTO"));
        p.setPrecioVenta(rs.getBigDecimal("PRECIO_VENTA"));
        p.setStock(rs.getInt("STOCK"));
        p.setIdCategoria(rs.getInt("ID_CATEGORIA"));
        p.setIdImpuesto(rs.getInt("ID_IMPUESTO"));
        return p;
    }

    public boolean existeProducto(BigDecimal idProducto) {
        String sql = "SELECT 1 FROM productos WHERE id_producto = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setBigDecimal(1, idProducto);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("existeProducto: " + e.getMessage());
            return false;
        } finally {
            conexion.desconectar();
        }
    }

   

    @Override
    public boolean registrarProducto(ModeloProducto p, long idUsuario) {
        String ins = "INSERT INTO productos " +
                "(id_producto, nombre_producto, marca, descripcion, precio_costo, precio_venta, stock, id_categoria, id_impuesto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            conexion.conectar();
            cn = conexion.getConnection();
            cn.setAutoCommit(false);

            ps = cn.prepareStatement(ins);
            ps.setBigDecimal(1, p.getIdProducto());
            ps.setString(2, p.getNombreProducto());
            ps.setString(3, p.getMarca());
            ps.setString(4, p.getDescripcion());
            ps.setBigDecimal(5, p.getPrecioCosto());
            ps.setBigDecimal(6, p.getPrecioVenta());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getIdCategoria());
            ps.setInt(9, p.getIdImpuesto());
            int rows = ps.executeUpdate();

            if (rows == 1) {
                ModeloAuditoria a = new ModeloAuditoria();
                a.setTipoCambio("PRODUCTO");
                a.setIdRegistro(p.getIdProducto().longValue());
                a.setOperacion("INSERT");
                a.setIdUsuario(idUsuario);
                a.setMotivo("Alta: " + p.getNombreProducto() + " | Marca=" + p.getMarca()
                        + " | PV=" + p.getPrecioVenta() + " | Stock=" + p.getStock());
                auditoriaDAO.agregar(cn, a);
            }

            cn.commit();
            return rows == 1;
        } catch (Exception e) {
            try { if (cn != null) cn.rollback(); } catch (Exception ignore) {}
            System.err.println("registrarProducto: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception ignore) {}
            try { if (cn != null) cn.setAutoCommit(true); } catch (Exception ignore) {}
            conexion.desconectar();
        }
    }

    @Override
    public boolean actualizarProducto(ModeloProducto p, long idUsuario) {
        String sel = "SELECT nombre_producto, marca, descripcion, precio_costo, precio_venta, stock, id_categoria, id_impuesto " +
                     "FROM productos WHERE id_producto=?";
        String up  = "UPDATE productos SET nombre_producto=?, marca=?, descripcion=?, precio_costo=?, " +
                     "precio_venta=?, stock=?, id_categoria=?, id_impuesto=? WHERE id_producto=?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            conexion.conectar();
            cn = conexion.getConnection();
            cn.setAutoCommit(false);

            String oNom=null,oMar=null,oDesc=null;
            BigDecimal oPC=null,oPV=null;
            Integer oStk=null,oCat=null,oImp=null;

            try (PreparedStatement q = cn.prepareStatement(sel)) {
                q.setBigDecimal(1, p.getIdProducto());
                try (ResultSet rs = q.executeQuery()) {
                    if (!rs.next()) throw new SQLException("Producto no encontrado.");
                    oNom = rs.getString(1);
                    oMar = rs.getString(2);
                    oDesc= rs.getString(3);
                    oPC  = rs.getBigDecimal(4);
                    oPV  = rs.getBigDecimal(5);
                    oStk = rs.getInt(6);
                    oCat = rs.getInt(7);
                    oImp = rs.getInt(8);
                }
            }

            // 2) actualizar
            ps = cn.prepareStatement(up);
            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getDescripcion());
            ps.setBigDecimal(4, p.getPrecioCosto());
            ps.setBigDecimal(5, p.getPrecioVenta());
            ps.setInt(6, p.getStock());
            ps.setInt(7, p.getIdCategoria());
            ps.setInt(8, p.getIdImpuesto());
            ps.setBigDecimal(9, p.getIdProducto());
            int rows = ps.executeUpdate();

            if (rows == 1) {
                StringBuilder motivo = new StringBuilder("Edición: ");
                addDiff(motivo, "Nombre", oNom, p.getNombreProducto());
                addDiff(motivo, "Marca",  oMar, p.getMarca());
                addDiff(motivo, "Desc",   oDesc, p.getDescripcion());
                addDiff(motivo, "PC",     oPC, p.getPrecioCosto());
                addDiff(motivo, "PV",     oPV, p.getPrecioVenta());
                addDiff(motivo, "Stock",  oStk, p.getStock());
                addDiff(motivo, "Cat",    oCat, p.getIdCategoria());
                addDiff(motivo, "Imp",    oImp, p.getIdImpuesto());

                ModeloAuditoria a = new ModeloAuditoria();
                a.setTipoCambio("PRODUCTO");
                a.setIdRegistro(p.getIdProducto().longValue());
                a.setOperacion("UPDATE");
                a.setIdUsuario(idUsuario);
                a.setMotivo(motivo.toString());
                auditoriaDAO.agregar(cn, a);
            }

            cn.commit();
            return rows == 1;
        } catch (Exception e) {
            try { if (cn != null) cn.rollback(); } catch (Exception ignore) {}
            System.err.println("actualizarProducto: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception ignore) {}
            try { if (cn != null) cn.setAutoCommit(true); } catch (Exception ignore) {}
            conexion.desconectar();
        }
    }

    @Override
    public boolean eliminarProducto(BigDecimal idProducto, long idUsuario) {
        String del = "DELETE FROM productos WHERE id_producto=?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            conexion.conectar();
            cn = conexion.getConnection();
            cn.setAutoCommit(false);

            ps = cn.prepareStatement(del);
            ps.setBigDecimal(1, idProducto);
            int rows = ps.executeUpdate();

            if (rows == 1) {
                ModeloAuditoria a = new ModeloAuditoria();
                a.setTipoCambio("PRODUCTO");
                a.setIdRegistro(idProducto.longValue());
                a.setOperacion("DELETE");
                a.setIdUsuario(idUsuario);
                a.setMotivo("Eliminación de producto");
                auditoriaDAO.agregar(cn, a);
            }

            cn.commit();
            return rows == 1;
        } catch (Exception e) {
            try { if (cn != null) cn.rollback(); } catch (Exception ignore) {}
            System.err.println("eliminarProducto: " + e.getMessage());
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception ignore) {}
            try { if (cn != null) cn.setAutoCommit(true); } catch (Exception ignore) {}
            conexion.desconectar();
        }
    }

    @Override
    public boolean registrarProducto(ModeloProducto p) {
        long uid = ModeloVistaInicio.getIdUsuarioEncontrado(); 
        return registrarProducto(p, uid);
    }

    @Override
    public boolean actualizarProducto(ModeloProducto p) {
        long uid = ModeloVistaInicio.getIdUsuarioEncontrado();
        return actualizarProducto(p, uid);
    }

    @Override
    public boolean eliminarProducto(BigDecimal idProducto) {
        long uid = ModeloVistaInicio.getIdUsuarioEncontrado();
        return eliminarProducto(idProducto, uid);
    }

    @Override
    public ModeloProducto buscarPorId(BigDecimal codigo) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setBigDecimal(1, codigo);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            System.err.println("buscarPorId: " + e.getMessage());
            return null;
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public ModeloProducto buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM productos WHERE UPPER(nombre_producto) LIKE UPPER(?) FETCH FIRST 1 ROWS ONLY";
        try {
            conexion.conectar();
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ps.setString(1, "%" + nombre.trim() + "%");
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            System.err.println("buscarPorNombre: " + e.getMessage());
            return null;
        } finally {
            conexion.desconectar();
        }
    }

    
    private static void addDiff(StringBuilder sb, String campo, Object oldV, Object newV) {
        String o = String.valueOf(oldV), n = String.valueOf(newV);
        if ((oldV == null && newV != null) || (oldV != null && !o.equals(n))) {
            sb.append(campo).append(": ").append(o).append("→").append(n).append("; ");
        }
    }
}
