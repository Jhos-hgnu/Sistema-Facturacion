// Vistas/VistaCuentasBD.java
package Vistas;

import Interfaces.CuentasDAO;
import Implementacion.CuentasDAOImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.awt.Font;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class VistaCuentasBD extends JFrame {

    private final JTable tablaCXC = new JTable();
    private final JTable tablaCXP = new JTable();
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private final NumberFormat nfGt = NumberFormat.getCurrencyInstance(new Locale("es","GT"));

    private final CuentasDAO dao = new CuentasDAOImpl();

    // Botones de exportación
    private final JButton btnExportarPDF = new JButton("Exportar PDF");
    private final JButton btnExportarCSV = new JButton("Exportar CSV");

    public VistaCuentasBD() {
        setTitle("Cuentas por Cobrar y Pagar (BD)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 600);
        setLocationRelativeTo(null);

    JLabel titulo = new JLabel("CUENTAS (Desde la Base de Datos)", SwingConstants.CENTER);
titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
titulo.setBorder(BorderFactory.createEmptyBorder(16,16,8,16));


        // Panel inferior de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelBotones.add(btnExportarPDF);
        panelBotones.add(btnExportarCSV);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Cuentas por Cobrar", new JScrollPane(tablaCXC));
        tabs.addTab("Cuentas por Pagar",  new JScrollPane(tablaCXP));

        add(titulo, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar datos
        cargarCXC();
        cargarCXP();

        // Eventos exportar
        btnExportarPDF.addActionListener(e -> exportarPDF(tabs.getSelectedIndex()));
        btnExportarCSV.addActionListener(e -> exportarCSV(tabs.getSelectedIndex()));
    }

    // ================= CARGA DE TABLAS =================
    private void cargarCXC() {
        var rows = dao.listarCXC(0);
        String[] cols = {"ID", "ID Venta", "Emisión", "Vence", "Monto", "Saldo", "Estado"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;}};
        for (var r : rows)
            m.addRow(new Object[]{r.id(), r.idVenta(), df.format(r.emision()), df.format(r.vence()), r.monto(), r.saldo(), r.estado()});
        tablaCXC.setModel(m);
        configurarTabla(tablaCXC, new int[]{60,80,100,100,110,110,90}, new int[]{4,5});
    }

    private void cargarCXP() {
        var rows = dao.listarCXP(0);
        String[] cols = {"ID", "ID Compra", "Emisión", "Vencimiento", "Monto Total", "Saldo Pend.", "Estado"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;}};
        for (var r : rows)
            m.addRow(new Object[]{r.id(), r.idCompra(), df.format(r.emision()), df.format(r.vencimiento()), r.montoTotal(), r.saldoPend(), r.estado()});
        tablaCXP.setModel(m);
        configurarTabla(tablaCXP, new int[]{60,90,100,120,110,110,90}, new int[]{4,5});
    }

    private void configurarTabla(JTable tabla, int[] widths, int[] moneyCols) {
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setAutoCreateRowSorter(true);
        for (int i = 0; i < widths.length; i++)
            tabla.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        DefaultTableCellRenderer money = new DefaultTableCellRenderer() {
            protected void setValue(Object v) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                setText(v==null ? "" : nfGt.format(v));
            }
        };
        for (int c : moneyCols)
            tabla.getColumnModel().getColumn(c).setCellRenderer(money);
    }

    // ================= EXPORTAR PDF =================
    private void exportarPDF(int tabIndex) {
        JTable tabla = (tabIndex == 0) ? tablaCXC : tablaCXP;
        String nombre = (tabIndex == 0) ? "CuentasPorCobrar" : "CuentasPorPagar";

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(nombre + ".pdf"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Document doc = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(doc, new java.io.FileOutputStream(chooser.getSelectedFile()));
                doc.open();

                Paragraph titulo = new Paragraph("Reporte " + nombre,
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE));
                titulo.setAlignment(Element.ALIGN_CENTER);
                doc.add(titulo);
                doc.add(new Paragraph("\n"));

                PdfPTable pdfTable = new PdfPTable(tabla.getColumnCount());
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    PdfPCell header = new PdfPCell(new Phrase(tabla.getColumnName(i)));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    pdfTable.addCell(header);
                }
                for (int r = 0; r < tabla.getRowCount(); r++)
                    for (int c = 0; c < tabla.getColumnCount(); c++)
                        pdfTable.addCell(String.valueOf(tabla.getValueAt(r, c)));

                doc.add(pdfTable);
                doc.close();

                JOptionPane.showMessageDialog(this, "✅ PDF exportado correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error exportando PDF: " + ex.getMessage());
            }
        }
    }

    // ================= EXPORTAR CSV =================
    private void exportarCSV(int tabIndex) {
        JTable tabla = (tabIndex == 0) ? tablaCXC : tablaCXP;
        String nombre = (tabIndex == 0) ? "CuentasPorCobrar" : "CuentasPorPagar";

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(nombre + ".csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {

                // Encabezados
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    pw.print(tabla.getColumnName(i));
                    if (i < tabla.getColumnCount() - 1) pw.print(",");
                }
                pw.println();

                // Filas
                for (int r = 0; r < tabla.getRowCount(); r++) {
                    for (int c = 0; c < tabla.getColumnCount(); c++) {
                        Object value = tabla.getValueAt(r, c);
                        pw.print(value != null ? value.toString().replace(",", " ") : "");
                        if (c < tabla.getColumnCount() - 1) pw.print(",");
                    }
                    pw.println();
                }

                JOptionPane.showMessageDialog(this, "✅ CSV exportado correctamente.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error exportando CSV: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        EventQueue.invokeLater(() -> new VistaCuentasBD().setVisible(true));
    }
}
