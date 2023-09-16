package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


//import static org.junit.Assert.assertEquals;
@TestInstance(Lifecycle.PER_CLASS)
public class DBconnTest {
    private Connection conn;
    private Statement stmt;

    @BeforeAll
    public void setUp() throws Exception {
        conn = DBconn.getConnection();
        stmt = conn.createStatement();
        try {
            stmt.executeUpdate("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255))");
        } catch (SQLException e) {
            // If the table already exists, print a message and continue.
            System.out.println("Table already exists.");
        }
    }

    @AfterAll
    public void tearDown() throws Exception {
        if (stmt != null && !stmt.isClosed()) {
            stmt.close();
        }
        stmt = conn.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS test_table");
        DBconn.closeConnection(conn, stmt);
    }


    @Test
    public void testGetConnection() throws Exception {
        assertNotNull(conn);
    }


}
