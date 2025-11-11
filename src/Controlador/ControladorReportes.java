package Controlador;

import Modelo.ModeloReporte;
import Vistas.PanelReportesVentas;
import Vistas.PanelFinanciero;            // <-- importa el panel financiero
// import Vistas.PanelReportesCompras;
// import Vistas.PanelReportesInventario;
import Vistas.VistaAdmin;
import Vistas.VistaReportes;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class ControladorReportes {

    private final ModeloReporte modelo;
    private final VistaReportes vista;

    // Instancias de panels a reutilizar (evita recrear cada clic)
    private final PanelReportesVentas vistaRVentas = new PanelReportesVentas();
    private final PanelFinanciero vistaRFinanciero = new PanelFinanciero(); // <-- ¡AQUÍ!

    // private final PanelReportesCompras vistaRCompras = new PanelReportesCompras();
    // private final PanelReportesInventario vistaRInventario = new PanelReportesInventario();

    public ControladorReportes(ModeloReporte modelo, VistaReportes vista) {
        this.modelo = modelo;
        this.vista = vista;
        configurarListeners();
    }

    private void configurarListeners() {
        vista.getBtnVolver().addActionListener(e -> volverMenuPrincipal());
        vista.getBtnVentas().addActionListener(e -> insertarPanelReporte(vistaRVentas));

        // === Botón FINANCIERO ===
        vista.getBtnFinanciero().addActionListener(e -> {
            insertarPanelReporte(vistaRFinanciero);
            // Si luego expones un método refresh en PanelFinanciero:
            // vistaRFinanciero.refrescar();
        });

        // Si ya tienes estos botones y panels, descomenta:
        // vista.getBtnCompras().addActionListener(e -> insertarPanelReporte(vistaRCompras));
        // vista.getBtnInventario().addActionListener(e -> insertarPanelReporte(vistaRInventario));
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
