/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;

/**
 *
 * @author Madelin
 */


public class Valida {
    public static boolean esEnteroPositivo(String t) { return t!=null && t.matches("\\d+"); }
    public static boolean esTelefono(String t)       { return t==null || t.matches("\\d{6,15}"); }
    public static boolean esEmail(String t)          { return t==null || t.isBlank() || t.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"); }
}

