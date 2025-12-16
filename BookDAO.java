package com.library;

import java.sql.*;

public class BookDAO {

    // ================= VIEW ALL BOOKS =================
    public void getAllBooks() {

        String sql = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Print header ONCE
            System.out.println("\n==================== LIBRARY BOOK LIST ====================\n");

            System.out.printf(
                "%-5s %-10s %-30s %-25s %-15s %-10s %-12s\n",
                "ID", "Code", "Title", "Author", "Genre", "Total", "Available"
            );

            System.out.println("----------------------------------------------------------------------------------------------");

            // Print rows
            while (rs.next()) {
                System.out.printf(
                    "%-5d %-10s %-30s %-25s %-15s %-10d %-12d\n",
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("totalCopies"),
                    rs.getInt("availableCopies")
                );
            }

            System.out.println("==============================================================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ISSUE BOOK =================
    public void issueBook(int bookId, String issuedTo) {

        String checkSql = "SELECT availableCopies FROM books WHERE id = ?";
        String issueSql = """
            INSERT INTO issued_books (book_id, issued_to, issue_date, due_date)
            VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY))
            """;
        String updateSql = "UPDATE books SET availableCopies = availableCopies - 1 WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("availableCopies") > 0) {

                PreparedStatement issueStmt = con.prepareStatement(issueSql);
                issueStmt.setInt(1, bookId);
                issueStmt.setString(2, issuedTo);
                issueStmt.executeUpdate();

                PreparedStatement updateStmt = con.prepareStatement(updateSql);
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                System.out.println("📕 Book issued successfully!");
                System.out.println("📅 Due date: 7 days from today");

            } else {
                System.out.println("❌ Book not available!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= RETURN BOOK =================
    public void returnBook(int issueId, int bookId) {

        String fetchSql = """
            SELECT DATEDIFF(CURDATE(), due_date) AS late_days
            FROM issued_books
            WHERE id = ? AND returned = FALSE
            """;

        String returnSql = """
            UPDATE issued_books
            SET returned = TRUE,
                return_date = CURDATE(),
                fine_amount = ?
            WHERE id = ?
            """;

        String updateBookSql = "UPDATE books SET availableCopies = availableCopies + 1 WHERE id = ?";

        final int FINE_PER_DAY = 10;

        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement fetchStmt = con.prepareStatement(fetchSql);
            fetchStmt.setInt(1, issueId);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                int lateDays = rs.getInt("late_days");
                int fine = 0;

                if (lateDays > 0) {
                    fine = lateDays * FINE_PER_DAY;
                }

                PreparedStatement returnStmt = con.prepareStatement(returnSql);
                returnStmt.setInt(1, fine);
                returnStmt.setInt(2, issueId);
                returnStmt.executeUpdate();

                PreparedStatement updateBookStmt = con.prepareStatement(updateBookSql);
                updateBookStmt.setInt(1, bookId);
                updateBookStmt.executeUpdate();

                System.out.println("📗 Book returned successfully!");

                if (fine > 0) {
                    System.out.println("💰 Late by " + lateDays + " days");
                    System.out.println("💸 Fine amount: ₹" + fine);
                } else {
                    System.out.println("✅ Returned on time. No fine!");
                }

            } else {
                System.out.println("❌ Invalid issue ID or book already returned.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
