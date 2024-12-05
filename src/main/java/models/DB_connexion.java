/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author akach
 */
public class DB_connexion {


    public static Connection con() {
     Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mastertaskapp", "root", "");
                System.out.println("Connexion established");
            } catch (SQLException ex) {
                Logger.getLogger(DB_connexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB_connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }}
