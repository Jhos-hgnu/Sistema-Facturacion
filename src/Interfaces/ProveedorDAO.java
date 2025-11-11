/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

/**
 *
 * @author Madelin
 */



import Modelo.ModeloProveedor;
import java.util.Optional;

public interface ProveedorDAO {
    int crear(ModeloProveedor p) throws Exception;
    Optional<ModeloProveedor> buscarPorId(int id) throws Exception;
    Optional<ModeloProveedor> buscarPorNit(String nit) throws Exception;
    int actualizar(ModeloProveedor p) throws Exception;
    int eliminarFisico(int id) throws Exception;   // DELETE
    int desactivar(int id) throws Exception;       // UPDATE estado='INACTIVO'
}


