/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
/**
 *
 * @author anyi4
 */




public class CsvExporter {

    
    public static void exportForExcel(JTable table, File destino) throws Exception {
        export(table, destino, ';', true);
    }

  
    public static void export(JTable table, File destino, char sep, boolean excelSepHeader) throws Exception {
        TableModel m = table.getModel();
        try (OutputStream os = new FileOutputStream(destino);
             OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
             PrintWriter pw = new PrintWriter(osw)) {

          
            pw.write('\ufeff');

           
            if (excelSepHeader) {
                pw.println("sep=" + sep);
            }

      
            for (int c = 0; c < m.getColumnCount(); c++) {
                if (c > 0) pw.print(sep);
                pw.print(quote(m.getColumnName(c), sep));
            }
            pw.println();

          
            for (int r = 0; r < m.getRowCount(); r++) {
                for (int c = 0; c < m.getColumnCount(); c++) {
                    if (c > 0) pw.print(sep);
                    Object v = m.getValueAt(r, c);
                    pw.print(quote(v == null ? "" : v.toString(), sep));
                }
                pw.println();
            }
        }
    }

  
    private static String quote(String s, char sep) {
        boolean need =
            s.indexOf(sep) >= 0 || s.indexOf('"') >= 0 || s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0;
        if (!need) return s;
        return '"' + s.replace("\"", "\"\"") + '"';
    }
}
