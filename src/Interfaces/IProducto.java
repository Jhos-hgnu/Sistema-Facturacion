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
}

