/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas;

/**
 *
 * @author cindy
 */
import Conector.*;
import Interfaces.IRegistroCliente;
import controladores.ClienteController;
import controladores.ControladorClientes;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

public class PanelClientes extends javax.swing.JPanel {

            public ClienteController controlador;

    public PanelClientes() {
        initComponents();



     this.controlador = new ClienteController(this);

        // Crear las dependencias necesarias
        DBConnection conexion = new DBConnection();
        SQL sql = new SQL();

    }
    
    
    

    //esta parte solo funciona para correr este jframe
    //public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
/*        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                javax.swing.JFrame frame = new javax.swing.JFrame("Prueba - Cuentas por Pagar");
                frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new PanelClientes());
                frame.pack(); // ajusta tamaño al contenido
                frame.setSize(850, 700); // o fija el tamaño manualmente
                frame.setLocationRelativeTo(null); // centra la ventana
                frame.setVisible(true);
            }
        });
    }*/

//creacion get y set
    public JButton getBtnactualizar() {
        return btnactualizar;
    }

    public void setBtnactualizar(JButton btnactualizar) {
        this.btnactualizar = btnactualizar;
    }

    public JButton getBtneliminar() {
        return btneliminar;
    }

    public void setBtneliminar(JButton btneliminar) {
        this.btneliminar = btneliminar;
    }

    public JButton getBtningresar1() {
        return btningresar1;
    }

    public void setBtningresar1(JButton btningresar1) {
        this.btningresar1 = btningresar1;
    }

    public JTable getTblclientes() {
        return tblclientes;
    }

    public void setTblclientes(JTable tblclientes) {
        this.tblclientes = tblclientes;
    }

    public JTextField getTxtApellidoCasado() {
        return txtApellidoCasado;
    }

    public void setTxtApellidoCasado(JTextField txtApellidoCasado) {
        this.txtApellidoCasado = txtApellidoCasado;
    }

    public JTextField getTxtApellidoCliente1() {
        return txtApellidoCliente1;
    }

    public void setTxtApellidoCliente1(JTextField txtApellidoCliente1) {
        this.txtApellidoCliente1 = txtApellidoCliente1;
    }

    public JTextField getTxtApellidoCliente2() {
        return txtApellidoCliente2;
    }

    public void setTxtApellidoCliente2(JTextField txtApellidoCliente2) {
        this.txtApellidoCliente2 = txtApellidoCliente2;
    }

    public JTextField getTxtDireccion() {
        return txtDireccion;
    }

    public void setTxtDireccion(JTextField txtDireccion) {
        this.txtDireccion = txtDireccion;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(JTextField txtEmail) {
        this.txtEmail = txtEmail;
    }

    public JTextField getTxtEstado() {
        return txtEstado;
    }

    public void setTxtEstado(JTextField txtEstado) {
        this.txtEstado = txtEstado;
    }

    public JTextField getTxtFechaRegistro() {
        return txtFechaRegistro;
    }

    public void setTxtFechaRegistro(JTextField txtFechaRegistro) {
        this.txtFechaRegistro = txtFechaRegistro;
    }

    public JTextField getTxtLimiteCredito() {
        return txtLimiteCredito;
    }

    public void setTxtLimiteCredito(JTextField txtLimiteCredito) {
        this.txtLimiteCredito = txtLimiteCredito;
    }

    public JTextField getTxtNIT() {
        return txtNIT;
    }

    public void setTxtNIT(JTextField txtNIT) {
        this.txtNIT = txtNIT;
    }

    public JTextField getTxtNombreCliente1() {
        return txtNombreCliente1;
    }

    public void setTxtNombreCliente1(JTextField txtNombreCliente1) {
        this.txtNombreCliente1 = txtNombreCliente1;
    }

    public JTextField getTxtNombreCliente2() {
        return txtNombreCliente2;
    }

    public void setTxtNombreCliente2(JTextField txtNombreCliente2) {
        this.txtNombreCliente2 = txtNombreCliente2;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(JTextField txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    public JTextField getTxtTipoCliente() {
        return txtTipoCliente;
    }

    public void setTxtTipoCliente(JTextField txtTipoCliente) {
        this.txtTipoCliente = txtTipoCliente;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        fondoPanel = new javax.swing.JPanel();
        separador = new javax.swing.JSeparator();
        separador2 = new javax.swing.JSeparator();
        txtNombreCliente1 = new javax.swing.JTextField();
        txtNombreCliente2 = new javax.swing.JTextField();
        txtApellidoCasado = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtNIT = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtLimiteCredito = new javax.swing.JTextField();
        txtFechaRegistro = new javax.swing.JTextField();
        tblRegistroClientes = new javax.swing.JScrollPane();
        tblclientes = new javax.swing.JTable();
        btnSalir = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        marcaAgua = new javax.swing.JLabel();
        IDCliente = new javax.swing.JLabel();
        nombreCliente = new javax.swing.JLabel();
        NIT = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        telefono = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        txtApellidoCliente1 = new javax.swing.JTextField();
        txtApellidoCliente2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtTipoCliente = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        titulo1 = new javax.swing.JLabel();
        btnactualizar = new javax.swing.JButton();
        btningresar1 = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setPreferredSize(new java.awt.Dimension(850, 700));

        fondoPanel.setBackground(new java.awt.Color(28, 95, 118));
        fondoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        separador.setBackground(new java.awt.Color(255, 255, 255));
        separador.setForeground(new java.awt.Color(255, 255, 255));
        fondoPanel.add(separador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 610, 850, -1));

        separador2.setBackground(new java.awt.Color(255, 255, 255));
        separador2.setForeground(new java.awt.Color(255, 255, 255));
        fondoPanel.add(separador2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 620, 850, -1));

        txtNombreCliente1.setBackground(new java.awt.Color(75, 128, 146));
        txtNombreCliente1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtNombreCliente1.setForeground(new java.awt.Color(255, 255, 255));
        txtNombreCliente1.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNombreCliente1.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        txtNombreCliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreCliente1ActionPerformed(evt);
            }
        });
        fondoPanel.add(txtNombreCliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, 180, -1));

        txtNombreCliente2.setBackground(new java.awt.Color(75, 128, 146));
        txtNombreCliente2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtNombreCliente2.setForeground(new java.awt.Color(255, 255, 255));
        txtNombreCliente2.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNombreCliente2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        fondoPanel.add(txtNombreCliente2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 190, 180, -1));

        txtApellidoCasado.setBackground(new java.awt.Color(75, 128, 146));
        txtApellidoCasado.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtApellidoCasado.setForeground(new java.awt.Color(255, 255, 255));
        txtApellidoCasado.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtApellidoCasado, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 290, 160, -1));

        txtTelefono.setBackground(new java.awt.Color(75, 128, 146));
        txtTelefono.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(255, 255, 255));
        txtTelefono.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 320, 160, -1));

        txtNIT.setBackground(new java.awt.Color(75, 128, 146));
        txtNIT.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtNIT.setForeground(new java.awt.Color(255, 255, 255));
        txtNIT.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNIT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNITActionPerformed(evt);
            }
        });
        fondoPanel.add(txtNIT, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 350, 160, -1));

        txtDireccion.setBackground(new java.awt.Color(75, 128, 146));
        txtDireccion.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtDireccion.setForeground(new java.awt.Color(255, 255, 255));
        txtDireccion.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 380, 160, -1));

        txtLimiteCredito.setBackground(new java.awt.Color(75, 128, 146));
        txtLimiteCredito.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtLimiteCredito.setForeground(new java.awt.Color(255, 255, 255));
        txtLimiteCredito.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtLimiteCredito, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 500, 160, -1));

        txtFechaRegistro.setBackground(new java.awt.Color(75, 128, 146));
        txtFechaRegistro.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtFechaRegistro.setForeground(new java.awt.Color(255, 255, 255));
        txtFechaRegistro.setCaretColor(new java.awt.Color(255, 255, 255));
        txtFechaRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaRegistroActionPerformed(evt);
            }
        });
        fondoPanel.add(txtFechaRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 480, 140, -1));

        tblclientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblRegistroClientes.setViewportView(tblclientes);

        fondoPanel.add(tblRegistroClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 260, 350, 210));

        btnSalir.setBackground(new java.awt.Color(75, 128, 146));
        btnSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
        });
        btnSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("X");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 0, 30, 30));

        fondoPanel.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 10, 30, 30));

        marcaAgua.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        marcaAgua.setForeground(new java.awt.Color(255, 255, 255));
        marcaAgua.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        marcaAgua.setText("FARMACIA");
        fondoPanel.add(marcaAgua, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 640, 850, -1));

        IDCliente.setBackground(new java.awt.Color(255, 255, 255));
        IDCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        IDCliente.setForeground(new java.awt.Color(255, 255, 255));
        IDCliente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        IDCliente.setText("Primer Nombre:");
        fondoPanel.add(IDCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 130, -1));

        nombreCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        nombreCliente.setForeground(new java.awt.Color(255, 255, 255));
        nombreCliente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nombreCliente.setText("Segundo Nombre:");
        fondoPanel.add(nombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 190, 160, -1));

        NIT.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        NIT.setForeground(new java.awt.Color(255, 255, 255));
        NIT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        NIT.setText("NIT:");
        fondoPanel.add(NIT, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 130, -1));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Límite de Crédito:");
        fondoPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, 130, -1));

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Apellido Casada:");
        fondoPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 130, -1));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Email:");
        fondoPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 130, -1));

        telefono.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        telefono.setForeground(new java.awt.Color(255, 255, 255));
        telefono.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        telefono.setText("Teléfono:");
        fondoPanel.add(telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 130, -1));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Dirección:");
        fondoPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, 130, -1));

        txtEstado.setBackground(new java.awt.Color(75, 128, 146));
        txtEstado.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtEstado.setForeground(new java.awt.Color(255, 255, 255));
        txtEstado.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, 140, -1));

        txtApellidoCliente1.setBackground(new java.awt.Color(75, 128, 146));
        txtApellidoCliente1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtApellidoCliente1.setForeground(new java.awt.Color(255, 255, 255));
        txtApellidoCliente1.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtApellidoCliente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 180, -1));

        txtApellidoCliente2.setBackground(new java.awt.Color(75, 128, 146));
        txtApellidoCliente2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtApellidoCliente2.setForeground(new java.awt.Color(255, 255, 255));
        txtApellidoCliente2.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtApellidoCliente2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 220, 180, -1));

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Primer Apellido:");
        fondoPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 130, -1));

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Segundo Apellido:");
        fondoPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 220, 160, -1));

        txtEmail.setBackground(new java.awt.Color(75, 128, 146));
        txtEmail.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtEmail.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 410, 160, -1));

        txtTipoCliente.setBackground(new java.awt.Color(75, 128, 146));
        txtTipoCliente.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtTipoCliente.setForeground(new java.awt.Color(255, 255, 255));
        txtTipoCliente.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtTipoCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 470, 160, -1));

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Fecha Registro:");
        fondoPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 480, 120, -1));

        jLabel11.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Estado:");
        fondoPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 510, 120, -1));

        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Tipo de Cliente:");
        fondoPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, 130, -1));

        titulo1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        titulo1.setForeground(new java.awt.Color(255, 255, 255));
        titulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo1.setText("REGISTRO CLIENTES");
        fondoPanel.add(titulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 850, -1));

        btnactualizar.setBackground(new java.awt.Color(75, 128, 146));
        btnactualizar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnactualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnactualizar.setText("Actualizar");
        btnactualizar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnactualizarActionPerformed(evt);
            }
        });
        fondoPanel.add(btnactualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 550, 110, 30));

        btningresar1.setBackground(new java.awt.Color(75, 128, 146));
        btningresar1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btningresar1.setForeground(new java.awt.Color(255, 255, 255));
        btningresar1.setText("Ingresar");
        btningresar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btningresar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btningresar1ActionPerformed(evt);
            }
        });
        fondoPanel.add(btningresar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 550, 110, 30));

        btneliminar.setBackground(new java.awt.Color(75, 128, 146));
        btneliminar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btneliminar.setForeground(new java.awt.Color(255, 255, 255));
        btneliminar.setText("Eliminar");
        btneliminar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });
        fondoPanel.add(btneliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 550, 110, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fondoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fondoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
        System.exit(0);
    }//GEN-LAST:event_btnSalirMouseClicked

    private void txtFechaRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaRegistroActionPerformed

    }//GEN-LAST:event_txtFechaRegistroActionPerformed

    private void txtNombreCliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreCliente1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreCliente1ActionPerformed

    private void btningresar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btningresar1ActionPerformed
        controlador.agregarCliente();
        
// TODO add your handling code here:
    }//GEN-LAST:event_btningresar1ActionPerformed

    private void btnactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnactualizarActionPerformed
        controlador.actualizarCliente();
// TODO add your handling code here:
    }//GEN-LAST:event_btnactualizarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        controlador.eliminarCliente();
//TODO add your handling code here:
    }//GEN-LAST:event_btneliminarActionPerformed

    private void txtNITActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNITActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNITActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IDCliente;
    private javax.swing.JLabel NIT;
    public javax.swing.JPanel btnSalir;
    private javax.swing.JButton btnactualizar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btningresar1;
    private javax.swing.JPanel fondoPanel;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel marcaAgua;
    private javax.swing.JLabel nombreCliente;
    private javax.swing.JSeparator separador;
    private javax.swing.JSeparator separador2;
    private javax.swing.JScrollPane tblRegistroClientes;
    public javax.swing.JTable tblclientes;
    private javax.swing.JLabel telefono;
    private javax.swing.JLabel titulo1;
    public javax.swing.JTextField txtApellidoCasado;
    public javax.swing.JTextField txtApellidoCliente1;
    public javax.swing.JTextField txtApellidoCliente2;
    public javax.swing.JTextField txtDireccion;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtEstado;
    public javax.swing.JTextField txtFechaRegistro;
    public javax.swing.JTextField txtLimiteCredito;
    public javax.swing.JTextField txtNIT;
    public javax.swing.JTextField txtNombreCliente1;
    public javax.swing.JTextField txtNombreCliente2;
    public javax.swing.JTextField txtTelefono;
    public javax.swing.JTextField txtTipoCliente;
    // End of variables declaration//GEN-END:variables

    public void setControlador(ControladorClientes controlador) {
    }
}
