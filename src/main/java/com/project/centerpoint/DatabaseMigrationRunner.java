package com.project.centerpoint;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
            jdbcTemplate.execute("ALTER TABLE addresses MODIFY user_id BIGINT NULL");
            jdbcTemplate.execute("ALTER TABLE carts MODIFY user_id BIGINT NULL");
            jdbcTemplate.execute("ALTER TABLE orders MODIFY user_id BIGINT NULL");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
            System.out.println("Database constraints updated successfully for guest checkout.");
            java.nio.file.Files.writeString(java.nio.file.Paths.get("db-migration.log"), "SUCCESS");
        } catch (Exception e) {
            String err = "Error: " + e.getMessage() + "\n";
            for (StackTraceElement el : e.getStackTrace()) {
                err += el.toString() + "\n";
            }
            try {
                java.nio.file.Files.writeString(java.nio.file.Paths.get("db-migration.log"), err);
            } catch (Exception ex) {}
            System.out.println("Could not alter tables: " + e.getMessage());
        }
    }
}
