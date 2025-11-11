// Interfaces/CuentasDAO.java
// Interfaces/CuentasDAO.java
package Interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CuentasDAO {
    record CxcRow(Long id, Long idVenta, Date emision, Date vence,
                  BigDecimal monto, BigDecimal saldo, String estado) {}
    record CxpRow(Long id, Long idCompra, Date emision, Date vencimiento,
                  BigDecimal montoTotal, BigDecimal saldoPend, String estado) {}

    List<CxcRow> listarCXC(int limit); // 0 = sin límite
    List<CxpRow> listarCXP(int limit);
}
