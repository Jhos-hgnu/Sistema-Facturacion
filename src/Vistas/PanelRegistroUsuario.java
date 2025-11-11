/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Vistas;

import Controlador.ControladorRegistroUsuario;
import Modelo.ModeloRegistroUsuario;

/**
 *
 * @author cindy
 */
public class PanelRegistroUsuario extends javax.swing.JPanel {

    /**
     * Creates new form PanelRegistroUsuario
     */
    public PanelRegistroUsuario() {
        initComponents();

        // No borres este cableado
        ModeloRegistroUsuario modelo = new ModeloRegistroUsuario(this);
        ControladorRegistroUsuario controlador = new ControladorRegistroUsuario(modelo);
        setControlador(controlador);

        // Mostrar/ocultar lo tuyo
        btnBorrar.setVisible(true);
        labelFechaRegistro.setVisible(false);
        txtFechaCreacion.setVisible(false);

        // traer al frente por si hay solapes
        btnBorrar.getParent().setComponentZOrder(btnBorrar, 0);
        btnBorrar.revalidate();
        btnBorrar.repaint();

        // ─────────────────────────────────────────────
        //  BOTÓN "ACTUALIZAR" DINÁMICO (JPanel + JLabel)
        // ─────────────────────────────────────────────
        final java.awt.Color ACT_BASE = new java.awt.Color(70, 130, 180);  // azul visible desde el inicio
        final java.awt.Color ACT_HOVER = new java.awt.Color(60, 115, 160);

        javax.swing.JPanel btnActualizar = new javax.swing.JPanel(new java.awt.BorderLayout());
        btnActualizar.setName("btnActualizar");
        btnActualizar.setOpaque(true);
        btnActualizar.setBackground(ACT_BASE);
        btnActualizar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255, 80)));
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.JLabel lblActualizar = new javax.swing.JLabel("Actualizar", javax.swing.SwingConstants.CENTER);
        lblActualizar.setName("lblActualizar");
        lblActualizar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblActualizar.setForeground(java.awt.Color.WHITE);
        lblActualizar.setOpaque(false);

        btnActualizar.add(lblActualizar, java.awt.BorderLayout.CENTER);
        this.add(btnActualizar); // agregar antes de posicionar para que tenga parent

        // ─────────────────────────────────────────────
        // Posicionar respecto a "Borrar": a la DERECHA si cabe; si no, a la IZQUIERDA
        // ─────────────────────────────────────────────
        Runnable place = () -> {
            // contenedor donde están los botones
            java.awt.Container parent = btnBorrar.getParent();
            int parentW = parent != null ? parent.getWidth() : this.getWidth();

            // medidas base tomando "Borrar" como referencia
            int bw = (btnBorrar.getWidth() > 0) ? btnBorrar.getWidth() : 110;
            int bh = (btnBorrar.getHeight() > 0) ? btnBorrar.getHeight() : 30;
            int bx = (btnBorrar.getX() > 0) ? btnBorrar.getX() : 500;
            int by = (btnBorrar.getY() > 0) ? btnBorrar.getY() : 550;

            int gapX = 16;      // separación horizontal
            int offsetY = 0;    // ajuste vertical si lo quieres más abajo/arriba

            // propuesta: a la DERECHA de "Borrar"
            int ax = bx + bw + gapX;
            int ay = by + offsetY;

            // CLAMP: si se sale por la derecha, lo pasamos a la IZQUIERDA;
            // si aun así se sale por la izquierda, lo pegamos a 16 px del borde.
            if (parentW > 0) {
                if (ax + bw > parentW - 16) {
                    ax = bx - (bw + gapX); // a la izquierda
                    if (ax < 16) {
                        ax = 16;  // margen mínimo
                    }
                }
            }

            // aplicar bounds y forzar visibilidad
            btnActualizar.setBounds(ax, ay, bw, bh);
            btnActualizar.setVisible(true);

            // traer al frente por si otro componente lo tapa
            if (btnActualizar.getParent() != null) {
                btnActualizar.getParent().setComponentZOrder(btnActualizar, 0);
            }

            this.revalidate();
            this.repaint();
        };

        // Colocar cuando haya layout/tamaño real
        javax.swing.SwingUtilities.invokeLater(place);
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                place.run();
            }

            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                place.run();
            }
        });

        // Registrar listeners con tu controlador (click y hover)
        btnActualizar.addMouseListener(controlador);
        lblActualizar.addMouseListener(controlador);

        // refrescar final
        this.revalidate();
        this.repaint();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fondoPanel = new javax.swing.JPanel();
        txtIdUsuario = new javax.swing.JTextField();
        txtNombrePersonal = new javax.swing.JTextField();
        txtApellidoPersonal = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtFechaCreacion = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        boxTipoUsuario = new javax.swing.JComboBox<>();
        btnRegistrar = new javax.swing.JPanel();
        registrar = new javax.swing.JLabel();
        btnBorrar = new javax.swing.JPanel();
        borrar = new javax.swing.JLabel();
        btnSalir = new javax.swing.JPanel();
        salir = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        separador2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        tipoUsuario = new javax.swing.JLabel();
        marcaAgua = new javax.swing.JLabel();
        contraseñaUsuario = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nombreUsuario = new javax.swing.JLabel();
        titulo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelFechaRegistro = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNombreUsuario1 = new javax.swing.JTextField();
        nombreUsuario1 = new javax.swing.JLabel();
        txtApellidoCadado = new javax.swing.JTextField();
        txtIdRol = new javax.swing.JTextField();
        txtEstado = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        fondoPanel.setBackground(new java.awt.Color(28, 95, 118));
        fondoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtIdUsuario.setBackground(new java.awt.Color(75, 128, 146));
        txtIdUsuario.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtIdUsuario.setForeground(new java.awt.Color(255, 255, 255));
        txtIdUsuario.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtIdUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 190, 170, -1));

        txtNombrePersonal.setBackground(new java.awt.Color(75, 128, 146));
        txtNombrePersonal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtNombrePersonal.setForeground(new java.awt.Color(255, 255, 255));
        txtNombrePersonal.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNombrePersonal.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        fondoPanel.add(txtNombrePersonal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 420, -1));

        txtApellidoPersonal.setBackground(new java.awt.Color(75, 128, 146));
        txtApellidoPersonal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtApellidoPersonal.setForeground(new java.awt.Color(255, 255, 255));
        txtApellidoPersonal.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtApellidoPersonal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 290, 420, -1));

        txtTelefono.setBackground(new java.awt.Color(75, 128, 146));
        txtTelefono.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtTelefono.setForeground(new java.awt.Color(255, 255, 255));
        txtTelefono.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 440, 130, -1));

        txtFechaCreacion.setBackground(new java.awt.Color(75, 128, 146));
        txtFechaCreacion.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtFechaCreacion.setForeground(new java.awt.Color(255, 255, 255));
        txtFechaCreacion.setCaretColor(new java.awt.Color(255, 255, 255));
        txtFechaCreacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaCreacionActionPerformed(evt);
            }
        });
        fondoPanel.add(txtFechaCreacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 440, 120, -1));

        txtEmail.setBackground(new java.awt.Color(75, 128, 146));
        txtEmail.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtEmail.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 470, 220, -1));

        txtPassword.setBackground(new java.awt.Color(75, 128, 146));
        txtPassword.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtPassword.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 360, 240, -1));

        boxTipoUsuario.setBackground(new java.awt.Color(75, 128, 146));
        boxTipoUsuario.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        boxTipoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        boxTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "VENDEDOR", "ADMINISTRADOR" }));
        boxTipoUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fondoPanel.add(boxTipoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 220, 110, -1));

        btnRegistrar.setBackground(new java.awt.Color(75, 128, 146));
        btnRegistrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRegistrar.setPreferredSize(new java.awt.Dimension(110, 30));
        btnRegistrar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        registrar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        registrar.setForeground(new java.awt.Color(255, 255, 255));
        registrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registrar.setText("Registrar");
        registrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.add(registrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 100, 20));

        fondoPanel.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 550, -1, 30));

        btnBorrar.setBackground(new java.awt.Color(75, 128, 146));
        btnBorrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBorrar.setPreferredSize(new java.awt.Dimension(110, 30));
        btnBorrar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        borrar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        borrar.setForeground(new java.awt.Color(255, 255, 255));
        borrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        borrar.setText("Borrar");
        borrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBorrar.add(borrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 30));

        fondoPanel.add(btnBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 550, -1, 30));

        btnSalir.setBackground(new java.awt.Color(75, 128, 146));
        btnSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
        });
        btnSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        salir.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        salir.setForeground(new java.awt.Color(255, 255, 255));
        salir.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        salir.setText("X");
        salir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.add(salir, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 0, 30, 30));

        fondoPanel.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 10, 30, 30));

        separador.setBackground(new java.awt.Color(255, 255, 255));
        separador.setForeground(new java.awt.Color(255, 255, 255));
        fondoPanel.add(separador, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 620, 850, -1));

        separador2.setBackground(new java.awt.Color(255, 255, 255));
        separador2.setForeground(new java.awt.Color(255, 255, 255));
        fondoPanel.add(separador2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 610, 850, -1));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nombres Personales:");
        fondoPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 160, -1));

        tipoUsuario.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        tipoUsuario.setForeground(new java.awt.Color(255, 255, 255));
        tipoUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tipoUsuario.setText("Tipo de Usuario:");
        fondoPanel.add(tipoUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 220, 130, 20));

        marcaAgua.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        marcaAgua.setForeground(new java.awt.Color(255, 255, 255));
        marcaAgua.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        marcaAgua.setText("FARMACIA");
        fondoPanel.add(marcaAgua, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 640, 850, -1));

        contraseñaUsuario.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        contraseñaUsuario.setForeground(new java.awt.Color(255, 255, 255));
        contraseñaUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        contraseñaUsuario.setText("Contraseña Usuario:");
        fondoPanel.add(contraseñaUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 160, -1));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Teléfono:");
        fondoPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, 70, -1));

        nombreUsuario.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        nombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        nombreUsuario.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nombreUsuario.setText("Apellidos Casado:");
        fondoPanel.add(nombreUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 160, -1));

        titulo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("REGISTRO DE USUARIOS");
        fondoPanel.add(titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 850, -1));

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Email:");
        fondoPanel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 470, 70, -1));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("ID Rol:");
        fondoPanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 500, 70, -1));

        labelFechaRegistro.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        labelFechaRegistro.setForeground(new java.awt.Color(255, 255, 255));
        labelFechaRegistro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelFechaRegistro.setText("Fecha de Creación:");
        fondoPanel.add(labelFechaRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 440, 140, -1));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("ID Usuario:");
        fondoPanel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 160, -1));

        btnBuscar.setBackground(new java.awt.Color(75, 128, 146));
        btnBuscar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Buscar");
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout btnBuscarLayout = new javax.swing.GroupLayout(btnBuscar);
        btnBuscar.setLayout(btnBuscarLayout);
        btnBuscarLayout.setHorizontalGroup(
            btnBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
        );
        btnBuscarLayout.setVerticalGroup(
            btnBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnBuscarLayout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        fondoPanel.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 550, 110, 30));

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Incluya una combinación de letras mayúsculas, minúsculas, números y símbolos");
        fondoPanel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 380, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Nombre Usuario:");
        fondoPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 160, -1));

        txtNombreUsuario1.setBackground(new java.awt.Color(75, 128, 146));
        txtNombreUsuario1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtNombreUsuario1.setForeground(new java.awt.Color(255, 255, 255));
        txtNombreUsuario1.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtNombreUsuario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 220, 170, -1));

        nombreUsuario1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        nombreUsuario1.setForeground(new java.awt.Color(255, 255, 255));
        nombreUsuario1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nombreUsuario1.setText("Apellidos Personales:");
        fondoPanel.add(nombreUsuario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 160, -1));

        txtApellidoCadado.setBackground(new java.awt.Color(75, 128, 146));
        txtApellidoCadado.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtApellidoCadado.setForeground(new java.awt.Color(255, 255, 255));
        txtApellidoCadado.setCaretColor(new java.awt.Color(255, 255, 255));
        fondoPanel.add(txtApellidoCadado, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 320, 420, -1));

        txtIdRol.setBackground(new java.awt.Color(75, 128, 146));
        txtIdRol.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtIdRol.setForeground(new java.awt.Color(255, 255, 255));
        txtIdRol.setCaretColor(new java.awt.Color(255, 255, 255));
        txtIdRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdRolActionPerformed(evt);
            }
        });
        fondoPanel.add(txtIdRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 500, 450, -1));

        txtEstado.setBackground(new java.awt.Color(75, 128, 146));
        txtEstado.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        txtEstado.setForeground(new java.awt.Color(255, 255, 255));
        txtEstado.setCaretColor(new java.awt.Color(255, 255, 255));
        txtEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoActionPerformed(evt);
            }
        });
        fondoPanel.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 470, 110, -1));

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Estado:");
        fondoPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 470, 70, -1));

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

    private void txtFechaCreacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaCreacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaCreacionActionPerformed

    private void txtIdRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdRolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRolActionPerformed

    private void txtEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel borrar;
    public javax.swing.JComboBox<String> boxTipoUsuario;
    public javax.swing.JPanel btnBorrar;
    public javax.swing.JPanel btnBuscar;
    public javax.swing.JPanel btnRegistrar;
    public javax.swing.JPanel btnSalir;
    private javax.swing.JLabel contraseñaUsuario;
    private javax.swing.JPanel fondoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel labelFechaRegistro;
    private javax.swing.JLabel marcaAgua;
    private javax.swing.JLabel nombreUsuario;
    private javax.swing.JLabel nombreUsuario1;
    private javax.swing.JLabel registrar;
    private javax.swing.JLabel salir;
    private javax.swing.JSeparator separador;
    private javax.swing.JSeparator separador2;
    private javax.swing.JLabel tipoUsuario;
    private javax.swing.JLabel titulo;
    public javax.swing.JTextField txtApellidoCadado;
    public javax.swing.JTextField txtApellidoPersonal;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtEstado;
    public javax.swing.JTextField txtFechaCreacion;
    public javax.swing.JTextField txtIdRol;
    public javax.swing.JTextField txtIdUsuario;
    public javax.swing.JTextField txtNombrePersonal;
    public javax.swing.JTextField txtNombreUsuario1;
    public javax.swing.JPasswordField txtPassword;
    public javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
   public void setControlador(ControladorRegistroUsuario controlador) {
        btnRegistrar.addMouseListener(controlador);
        registrar.addMouseListener(controlador);   // ← añade esto
        btnBorrar.addMouseListener(controlador);
        btnBuscar.addMouseListener(controlador);
    }

    public javax.swing.JLabel getRegistrarLabel() {
        return registrar;
    }

}
