package Controlador;

import Modelo.ModeloReporte;
import Vistas.PanelReportesVentas;
import Vistas.VistaAdmin;
import Vistas.VistaReportes;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

public class ControladorReportes {

    private ModeloReporte modelo;
    private VistaReportes vista;

    public ControladorReportes(ModeloReporte modelo, VistaReportes vista) {
        this.modelo = modelo;
        this.vista = vista;
        configurarListeners();
    }
    
    PanelReportesVentas vistaRVentas = new PanelReportesVentas();
    
    

    private void configurarListeners() {
        vista.getBtnVolver().addActionListener(e -> volverMenuPrincipal());
        vista.getBtnVentas().addActionListener(e -> insertarPanelReporte(vistaRVentas));

    }

    private void volverMenuPrincipal() {
        VistaAdmin vistaAD = new VistaAdmin();
        vistaAD.setVisible(true);
        vista.dispose();
    }

    private void insertarPanelReporte(JPanel p) {
        p.setSize(924, 700);
        p.setLocation(0, 0);
        vista.pnlFondoReportes.removeAll();
        vista.pnlFondoReportes.add(p, BorderLayout.CENTER);
        vista.pnlFondoReportes.revalidate();
        vista.pnlFondoReportes.repaint();
    }
    
    
    
    
}
