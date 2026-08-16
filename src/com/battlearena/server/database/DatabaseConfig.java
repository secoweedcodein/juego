package com.battlearena.server.database;

/** Configuracion de MySQL mediante variables de entorno. */
public final class DatabaseConfig {

    private DatabaseConfig() { }

    public static final String URL = value("JDBC_DATABASE_URL",
            "jdbc:mysql://" + value("DB_HOST", value("MYSQLHOST", "localhost"))
                    + ":" + value("DB_PORT", value("MYSQLPORT", "3306"))
                    + "/" + value("DB_NAME", value("MYSQLDATABASE", "battle_arena"))
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");

    public static final String USER = value("DB_USER", value("MYSQLUSER", "root"));
    public static final String PASSWORD = value("DB_PASSWORD", value("MYSQLPASSWORD", ""));

    private static String value(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
