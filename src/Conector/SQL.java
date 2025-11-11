package Conector;

/**
 *
 * @author jhosu
 */
public class SQL {

    // Si necesitas prefijar esquema: "C##CTM3." por ejemplo.
    private static final String SCHEMA_PREFIX = ""; // ej. "C##CTM3."

    // Consulta para login: compara usuario (case-insensitive), contraseña y activo=1
    private static final String CONSULTA_USUARIO
            = "SELECT "
            + "  ID_USUARIO, "
            + "  PRIMER_NOMBRE AS USUARIO, "
            + // ← lo que tu código lee
            "  CONTRASENA     AS CONTRASENIA, "
            + // ← alias con 'I'
            "  TIPO_USUARIO, "
            + "  CASE WHEN ESTADO = 'ACTIVO' THEN 1 ELSE 0 END AS ACTIVO "
            + // ← numérico 1/0
            "FROM " + SCHEMA_PREFIX + "USUARIOS "
            + "WHERE UPPER(PRIMER_NOMBRE) = UPPER(?) "
            + "  AND CONTRASENA = ? "
            + "  AND ESTADO = 'ACTIVO'";

    public String getCONSULTA_USUARIO() {
        return CONSULTA_USUARIO;
    }

    // (Opcional) pequeño smoke test manual de conexión
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        try {
            db.conectar();
            System.out.println("Conexión OK");
        } finally {
            db.desconectar();
        }
    }
    
    
    //Consultas para los reportes xd
    private static String CONSULTA_REPORTE_DIA = "SELECT " +
                     "    TO_CHAR(v.fecha, 'HH24:MI') as hora, " +
                     "    'F' || TO_CHAR(v.id_venta) as factura, " +
                     "    c.primer_nombre || ' ' || c.primer_apellido as cliente, " +
                     "    (SELECT LISTAGG(p.nombre_producto || ' (' || dv.cantidad || ')' , ', ') " +
                     "     WITHIN GROUP (ORDER BY p.nombre_producto) " +
                     "     FROM detalle_venta dv " +
                     "     JOIN productos p ON dv.id_producto = p.id_producto " +
                     "     WHERE dv.id_venta = v.id_venta) as productos, " +
                     "    v.total, " +
                     "    u.primer_nombre || ' ' || u.primer_apellido as vendedor " +
                     "FROM venta v " +
                     "JOIN clientes c ON v.nit = c.nit " +
                     "JOIN usuarios u ON v.id_usuario = u.id_usuario " +
                     "WHERE TRUNC(v.fecha) = TRUNC(SYSDATE) " +
                     "ORDER BY v.fecha ASC";

    public static String getCONSULTA_REPORTE_DIA() {
        return CONSULTA_REPORTE_DIA;
    }
 
    
    
    
    
    
    
}