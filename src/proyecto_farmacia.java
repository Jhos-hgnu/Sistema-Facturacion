
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
       
       /*VistaInicio vistainicio = new VistaInicio();
        vistainicio.setVisible(true);*/
           JFrame frame = new JFrame("Auditoría");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(900, 700);
frame.setLocationRelativeTo(null);
//
PanelMovimientosInventario panel = new PanelMovimientosInventario();
//new controladores.Con(panel); // ← crea y conecta 1 sola vez
//
frame.setContentPane(panel);
frame.setVisible(true);
   
    }
   
   
    
}
