package io.ishynss.sweety.system.bungee.sql;

import io.ishynss.sweety.system.bungee.MainBungee;

public enum DatabaseManager {

    Osaka2(new DatabaseCredentials(String.valueOf(MainBungee.datababase.get("ip")), String.valueOf(MainBungee.datababase.get("user")), String.valueOf(MainBungee.datababase.get("password")), String.valueOf(MainBungee.datababase.get("name")), 3306));
    private DatabaseAccess databaseAccess;


    DatabaseManager(DatabaseCredentials credentials){
        this.databaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public static void initAllDatabaseConnections() {
        for(DatabaseManager databaseManager: values()){
            databaseManager.databaseAccess.initPool();
        }
    }

    public static void closeAllDatabaseConnections() {
        for(DatabaseManager databaseManager: values()){
            databaseManager.databaseAccess.closePool();
        }

    }
}
