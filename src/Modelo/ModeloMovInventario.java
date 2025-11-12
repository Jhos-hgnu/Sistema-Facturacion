/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author anyi4
 */
public record ModeloMovInventario(
        long idMov,
        Timestamp fechaHora,
        String tipo,
        String producto,
        String codigoBarras,
        BigDecimal cantidad,
        BigDecimal stockAntes,
        BigDecimal stockDespues,
        String usuario,
        String motivo) {

}
