/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Modelo.ModeloRegistroUsuario;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author jhosu
 */
public interface IRegistroUsuario {
    boolean guardarUsuario(ModeloRegistroUsuario modelo);
    javax.swing.DefaultComboBoxModel mostrarTiposUsuarios();
    boolean elimiarUsuario(String nombreU);
    ModeloRegistroUsuario validarUsuario(String nombreU, String contraU);

    // 👉 NUEVO:
    ModeloRegistroUsuario buscarUsuarioPorIdONombre(String idUsuario, String usuario);
}

