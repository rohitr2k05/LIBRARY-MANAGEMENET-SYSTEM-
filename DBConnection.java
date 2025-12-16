package com.library;

import java.sql.*;

public class DBConnection {

    public static Connection getConnection() {

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to your MySQL database
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/librarydb",
                "root",
                "Mals@123"
            );

            return con;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

