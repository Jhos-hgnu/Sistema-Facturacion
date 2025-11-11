/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import Modelo.ModeloCategoria;
import java.util.List;

/**
 *
 * @author anyi4
 */

public interface ICategoria {
    boolean registrarCategoria(ModeloCategoria categoria);
    ModeloCategoria buscarCategoria(int idCategoria);
    boolean actualizarCategoria(ModeloCategoria categoria);
    boolean borrarCategoria(int idCategoria);
    List<ModeloCategoria> listarCategorias();
   ModeloCategoria buscarPorNombre(String nombre);


}

