package me.gorenjec.mcagario.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.gorenjec.mcagario.McAgario;
import me.gorenjec.mcagario.models.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLStorage {
    private HikariDataSource dataSource;
    private final McAgario instance;
    private final FileConfiguration config;
    private static final String PLAYERDATA_TABLE = "aio_playerdata";

    public SQLStorage(McAgario instance) {
        this.instance = instance;
        this.config = instance.getConfig();

        boolean useMySQL = config.getBoolean("data_storage.use_mysql");
        String path = instance.getDataFolder().getPath();

        HikariConfig hikariConfig = new HikariConfig();

        if (useMySQL) {
            hikariConfig.setDriverClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikariConfig.setUsername(config.getString("data_storage.username"));
            hikariConfig.setPassword(config.getString("data_storage.password"));
            String hostname = config.getString("data_storage.ip");
            String port = config.getString("data_storage.port");
            String database = config.getString("data_storage.database");
            String useSSL = config.getBoolean("data_storage.database") ? "true" : "false";
            hikariConfig.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL);
        } else {
            hikariConfig.setDriverClassName("org.sqlite.JDBC");
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + path + "/database.sqlite");
        }

        hikariConfig.setPoolName("McAgarioPlugin");
        hikariConfig.setMaxLifetime(60000);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.addDataSourceProperty("database", config.getString("data_storage.database"));

        this.dataSource = new HikariDataSource(hikariConfig);

        constructTables();
    }

    private void constructTables() {
        try (Connection connection = dataSource.getConnection()) {
            String createTableSql = "CREATE TABLE IF NOT EXISTS " + PLAYERDATA_TABLE + "("
                    + "id VARCHAR(36) PRIMARY KEY, "
                    + "kills BIGINT(20), "
                    + "wins BIGINT(20), "
                    + "losses BIGINT(20), "
                    + "cell_material TEXT"
                    + ")";
            PreparedStatement statement = connection.prepareStatement(createTableSql);
            statement.execute();

            instance.getLogger().info("Verified data tables.");
        } catch (SQLException e) {
            instance.getLogger().severe("Could not create tables!");
            e.printStackTrace();
        }
    }

    public PlayerProfile getPlayer(Player player) {
        PlayerProfile playerProfile = null;

        String sql = "SELECT * FROM " + PLAYERDATA_TABLE + " WHERE id = ? LIMIT 1";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, player.getUniqueId().toString());

            ResultSet playerDataSet = statement.executeQuery();

            if (playerDataSet.next()) {
                playerProfile = new PlayerProfile(
                        player,
                        playerDataSet.getInt("kills"),
                        playerDataSet.getInt("wins"),
                        playerDataSet.getInt("losses"),
                        Material.valueOf(playerDataSet.getString("cell_material"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (playerProfile == null) {
            playerProfile = new PlayerProfile(
                    player,
                    0,
                    0,
                    0,
                    Material.WHITE_WOOL
            );

            addPlayer(playerProfile);
        }

        return playerProfile;
    }

    public void addPlayer(PlayerProfile playerProfile) {
        String sql = "INSERT INTO " + PLAYERDATA_TABLE + " VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, playerProfile.getPlayer().getUniqueId().toString());
            statement.setInt(2, playerProfile.getKills());
            statement.setInt(3, playerProfile.getWins());
            statement.setInt(4, playerProfile.getLosses());
            statement.setString(5, playerProfile.getCellMaterial().toString());

            statement.execute();
            instance.getLogger().info("Creating new player profile for " + playerProfile.getPlayer().getName() + ", player never joined before.");
        } catch (SQLException e) {
            instance.getLogger().severe("Could not store player data!");
            e.printStackTrace();
        }
    }

    public void updatePlayer(PlayerProfile playerProfile) {
        String sql = "UPDATE " + PLAYERDATA_TABLE + " SET kills = ?, wins = ?, losses = ?, cell_material = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, playerProfile.getKills());
            statement.setInt(2, playerProfile.getWins());
            statement.setInt(3, playerProfile.getLosses());
            statement.setString(4, playerProfile.getCellMaterial().toString());
            statement.setString(5, playerProfile.getPlayer().getUniqueId().toString());

            statement.execute();
            instance.getLogger().info("Updated " + playerProfile.getPlayer().getName() + ".");
        } catch (SQLException e) {
            instance.getLogger().severe("Could not store player data!");
            e.printStackTrace();
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
