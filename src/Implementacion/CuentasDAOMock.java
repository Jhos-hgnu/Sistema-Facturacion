// Implementacion/CuentasDAOMock.java
package Implementacion;

import Interfaces.CuentasDAO;

import java.math.BigDecimal;
import java.util.*;

public class CuentasDAOMock implements CuentasDAO {

    private Date d(int y, int m, int dia) {
        Calendar c = Calendar.getInstance();
        c.set(y, m - 1, dia, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    @Override
    public List<CxcRow> listarCXC(int limit) {
        List<CxcRow> out = new ArrayList<>();
        // genera 20 registros
        for (int i = 1; i <= 20; i++) {
            String estado = (i % 3 == 1) ? "PENDIENTE" : (i % 3 == 2) ? "PAGADA" : "PARCIAL";
            BigDecimal monto = new BigDecimal(500 + (i * 50)).setScale(2);
            BigDecimal saldo = switch (estado) {
                case "PAGADA" -> BigDecimal.ZERO.setScale(2);
                case "PARCIAL" -> monto.divide(new BigDecimal("2"));
                default -> monto;
            };
            out.add(new CxcRow(
                (long) i,                // id_cuenta_cobro
                100L + i,                // id_venta
                d(2025, 10 + (i % 2), 5 + (i % 20)),  // emision
                d(2025, 11, 10 + (i % 15)),          // vence
                monto, saldo, estado
            ));
        }
        if (limit > 0 && limit < out.size()) return out.subList(0, limit);
        return out;
    }

    @Override
    public List<CxpRow> listarCXP(int limit) {
        List<CxpRow> out = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String estado = (i % 3 == 0) ? "PAGADA" : (i % 2 == 0) ? "PARCIAL" : "PENDIENTE";
            BigDecimal total = new BigDecimal(700 + (i * 60)).setScale(2);
            BigDecimal saldo = switch (estado) {
                case "PAGADA" -> BigDecimal.ZERO.setScale(2);
                case "PARCIAL" -> total.divide(new BigDecimal("2"));
                default -> total;
            };
            out.add(new CxpRow(
                (long) i,                // id_cuenta_pago
                200L + i,                // id_compra
                d(2025, 9 + (i % 3), 7 + (i % 20)),   // emision
                d(2025, 12, 12 + (i % 10)),          // vencimiento
                total, saldo, estado
            ));
        }
        if (limit > 0 && limit < out.size()) return out.subList(0, limit);
        return out;
    }
}
