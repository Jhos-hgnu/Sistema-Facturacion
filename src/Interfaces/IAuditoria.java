/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import java.sql.Connection;
import Modelo.ModeloAuditoria;
import java.util.Optional;


/**
 *
 * @author anyi4
 */
public interface IAuditoria {
   

   boolean agregar(Connection cn, ModeloAuditoria auditoria);
   Optional<ModeloAuditoria> buscarPorIdAuditoria(Connection cn, long idAuditoria);
    Optional<ModeloAuditoria> buscarUltimaPorIdRegistro(Connection cn, long idRegistro);
  
}
