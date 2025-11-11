/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;


public class ModeloAuditoria {

    private long idAuditoria;    
    private String tipoCambio;    
    private long idRegistro;      
    private String operacion;     
    private long idUsuario;       
    private String motivo;        
    private Date fecha;           

    public ModeloAuditoria() {}

    public ModeloAuditoria(long idAuditoria, String tipoCambio, long idRegistro,
                           String operacion, long idUsuario, String motivo, Date fecha) {
        this.idAuditoria = idAuditoria;
        this.tipoCambio = tipoCambio;
        this.idRegistro = idRegistro;
        this.operacion = operacion;
        this.idUsuario = idUsuario;
        this.motivo = motivo;
        this.fecha = fecha;
    }

   
    public long getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(long idAuditoria) { this.idAuditoria = idAuditoria; }

    public String getTipoCambio() { return tipoCambio; }
    public void setTipoCambio(String tipoCambio) { this.tipoCambio = tipoCambio; }

    public long getIdRegistro() { return idRegistro; }
    public void setIdRegistro(long idRegistro) { this.idRegistro = idRegistro; }

    public String getOperacion() { return operacion; }
    public void setOperacion(String operacion) { this.operacion = operacion; }

    public long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(long idUsuario) { this.idUsuario = idUsuario; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return "ModeloAuditoria{" +
                "idAuditoria=" + idAuditoria +
                ", tipoCambio='" + tipoCambio + '\'' +
                ", idRegistro=" + idRegistro +
                ", operacion='" + operacion + '\'' +
                ", idUsuario=" + idUsuario +
                ", motivo='" + motivo + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
