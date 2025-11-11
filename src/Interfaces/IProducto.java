package Interfaces;

import Modelo.ModeloProducto;
import java.math.BigDecimal;


/**
 *
 * @author jhosu
 */
public interface IProducto {
  
    boolean registrarProducto(ModeloProducto p);
    ModeloProducto buscarPorId (BigDecimal  codigo);
    ModeloProducto buscarPorNombre(String nombre);
    boolean actualizarProducto(ModeloProducto p);
    boolean eliminarProducto(BigDecimal idProducto);

    
    boolean registrarProducto(ModeloProducto p, long idUsuario);
    boolean actualizarProducto(ModeloProducto p, long idUsuario);
    boolean eliminarProducto(BigDecimal idProducto, long idUsuario);

/*hola*/
}

