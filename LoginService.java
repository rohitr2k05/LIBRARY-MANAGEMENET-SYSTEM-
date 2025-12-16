package com.library;

import java.util.Scanner;

public class LoginService {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    public boolean login() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Username: ");
        String user = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (USERNAME.equals(user) && PASSWORD.equals(pass)) {
            System.out.println("\n✅ Login Successful!\n");
            return true;
        } else {
            System.out.println("\n❌ Invalid credentials!\n");
            return false;
        }
    }
}
