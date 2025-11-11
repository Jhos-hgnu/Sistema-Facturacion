
import Controlador.*;
import Vistas.*;
import Vistas.VistaInicio;
import controladores.ControladorAuditoria;
import javax.swing.JFrame;

public class proyecto_farmacia {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        // TODO code application logic here

       /* VistaInicio vistainicio = new VistaInicio();
        vistainicio.setVisible(true);
        */
   JFrame frame = new JFrame("Auditoría");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(900, 700);
frame.setLocationRelativeTo(null);
PanelReporteCompras panel = new PanelReporteCompras();
   
frame.setContentPane(panel);
frame.setVisible(true);

    }
   
    
}
