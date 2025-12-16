package com.library;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BookDAO dao = new BookDAO();
        LoginService login = new LoginService();

        // ===== LOGIN =====
        if (!login.login()) {
            System.out.println("❌ Exiting system...");
            return;
        }

        // ===== MENU LOOP =====
        while (true) {
            System.out.println("""
                
                ========== LIBRARY MANAGEMENT SYSTEM ==========
                1. View All Books
                2. Issue Book
                3. Return Book
                4. Exit
                =============================================
                """);

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    dao.getAllBooks();
                    break;

                case 2:
                    System.out.print("Enter Book ID to issue: ");
                    int bookId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Issued to (name): ");
                    String issuedTo = sc.nextLine();

                    dao.issueBook(bookId, issuedTo);
                    break;

                case 3:
                    System.out.print("Enter Issue ID: ");
                    int issueId = sc.nextInt();

                    System.out.print("Enter Book ID: ");
                    int returnBookId = sc.nextInt();

                    dao.returnBook(issueId, returnBookId);
                    break;

                case 4:
                    System.out.println("👋 Thank you for using Library System!");
                    sc.close();
                    return;

                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
