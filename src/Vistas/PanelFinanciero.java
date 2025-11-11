// Vistas/PanelFinanciero.java
package Vistas;

import javax.swing.*;
import java.awt.*;

public class PanelFinanciero extends JPanel {
    private final VistaCuentasBD vista = new VistaCuentasBD(); // usa Oracle

    public PanelFinanciero() {
        setLayout(new BorderLayout());
        add(vista.getContentPane(), BorderLayout.CENTER);
    }

    // Si luego quieres refrescar manualmente, expón un método en VistaCuentasBD y llámalo aquí.
    public void refrescar() { /* opcional */ }
}
