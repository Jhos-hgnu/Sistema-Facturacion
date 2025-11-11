/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladores;
import Conector.DBConnection;
import Implementacion.AuditoriaImpl;
import Interfaces.IAuditoria;
import Modelo.ModeloAuditoria;
import Vistas.PanelAuditoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.swing.JOptionPane;
/**
 * @author jciri
 */


public class ControladorAuditoria implements ActionListener {

    private final PanelAuditoria vista;
    private final IAuditoria dao;
    private final DBConnection db;
    private final SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ControladorAuditoria(PanelAuditoria vista) {
        this.vista = vista;
        this.dao = new AuditoriaImpl();
        this.db  = new DBConnection();
        db.conectar();
        this.vista.setControlador(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if ("BUSCAR".equals(cmd)) onBuscar();
        else if (
        "Limpiar".equals(cmd)) limpiar();
      
    }

    private void onBuscar() {
        String idAudTxt = vista.getTxtIdAuditoria().getText().trim();
        String idRegTxt = vista.getTxtIdRegistro().getText().trim();

        if (idAudTxt.isEmpty() && idRegTxt.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese ID de Auditoría o ID de Registro para buscar.");
            return;
        }

        try {
            Connection cn = db.getConnection();
            Optional<ModeloAuditoria> op;

            if (!idAudTxt.isEmpty()) {
                long idAud = Long.parseLong(idAudTxt);
                op = dao.buscarPorIdAuditoria(cn, idAud);
            } else {
                long idReg = Long.parseLong(idRegTxt);
                op = dao.buscarUltimaPorIdRegistro(cn, idReg);
            }

            if (op.isPresent()) mostrar(op.get());
            else JOptionPane.showMessageDialog(vista, "No se encontraron resultados.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Los IDs deben ser numéricos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al buscar: " + ex.getMessage());
        }
    }

    private void mostrar(ModeloAuditoria a) {
        vista.getTxtIdAuditoria().setText(String.valueOf(a.getIdAuditoria()));
        vista.getTxtTipoCambio().setText(a.getTipoCambio());
        vista.getTxtIdRegistro().setText(String.valueOf(a.getIdRegistro()));
        vista.getTxtOperacion().setText(a.getOperacion());
        vista.getTxtIdUsuario().setText(String.valueOf(a.getIdUsuario()));
        vista.getTxtMotivo().setText(a.getMotivo());
        vista.getTxtFecha().setText(formato.format(a.getFecha() != null ? a.getFecha() : new Date()));
    }

    private void limpiar() {
        vista.getTxtIdAuditoria().setText("");
        vista.getTxtTipoCambio().setText("");
        vista.getTxtIdRegistro().setText("");
        vista.getTxtOperacion().setText("");
        vista.getTxtIdUsuario().setText("");
        vista.getTxtMotivo().setText("");
        vista.getTxtFecha().setText("");
    }
}
