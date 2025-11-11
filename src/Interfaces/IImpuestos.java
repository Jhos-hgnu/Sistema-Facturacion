/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;
import Modelo.ModeloImpuesto;
import java.sql.Connection;
import java.util.Optional;
/**
 *
 * @author anyi4
 */



public interface IImpuestos{
    long crear(Connection cn, String nombre, java.math.BigDecimal tasa, long idUsuario) throws Exception;
    void actualizar(Connection cn, long id, String nombre, java.math.BigDecimal tasa, long idUsuario) throws Exception;
    boolean borrar(Connection cn, long id, long idUsuario) throws Exception;

    Optional<ModeloImpuesto> buscarPorId(Connection cn, long id) throws Exception;
    Optional<ModeloImpuesto> buscarPorNombreExacto(Connection cn, String nombre) throws Exception;
    boolean estaEnUsoPorProductos(Connection cn, long id) throws Exception;
     boolean existeNombre(Connection cn, String nombre) throws Exception;
}
