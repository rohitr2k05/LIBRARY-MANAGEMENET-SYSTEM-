package com.library;

public class TestConnection {
    public static void main(String[] args) {
        if (DBConnection.getConnection() != null) {
            System.out.println("Connected Successfully!");
        } else {
            System.out.println("Connection Failed!");
        }
    }
}
