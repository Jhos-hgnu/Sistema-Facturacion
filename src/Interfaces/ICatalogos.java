/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;
import java.math.BigDecimal;
/**
 *
 * @author anyi4
 */



public interface ICatalogos {
    Integer getCategoriaIdByNombre(String nombre);
    Integer getImpuestoIdByNombre(String nombre);
    Integer getImpuestoIdByTasa(BigDecimal tasa);
}
