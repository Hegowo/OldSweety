package io.ishynss.sweety.system.spigot.sql;


import io.ishynss.sweety.system.spigot.MainSpigot;

public enum DatabaseManager {

    Osaka2(new DatabaseCredentials(MainSpigot.getPlugin().getConfig().getString("DataBase.ip"), MainSpigot.getPlugin().getConfig().getString("DataBase.user"), MainSpigot.getPlugin().getConfig().getString("DataBase.pass"), MainSpigot.getPlugin().getConfig().getString("DataBase.name"), 3306));

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
