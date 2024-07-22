package cn.salx.plugins.dailyexplimit.database;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SimpleDatabaseHelper {
    private final SQLManager sqlManager;
    private final String prefix;
    private final DatabaseDriverType databaseDriverType;

    public SimpleDatabaseHelper(SQLManager sqlManager, String prefix, DatabaseDriverType databaseDriverType) throws SQLException {
        this.sqlManager = sqlManager;
        this.prefix = prefix;
        this.databaseDriverType = databaseDriverType;
        checkTables();
    }

    private void checkTables() throws SQLException {
        DataTables.initializeTables(sqlManager, prefix, this.databaseDriverType);
    }

    public CompletableFuture<@Nullable Integer> getPlayerGainedExp(@NotNull UUID player) {
        return CompletableFuture.supplyAsync(() -> {
            try (SQLQuery query = DataTables.PLAYER_GAINED_EXP.createQuery()
                    .addCondition("uuid", player.toString())
                    .selectColumns("uuid", "gained_exp")
                    .setLimit(1)
                    .build().execute();
                 ResultSet resultSet = query.getResultSet()) {
                if (resultSet.next()) {
                    return resultSet.getInt("gained_exp");
                }
                return null;
            } catch (SQLException e) {
                throw new IllegalStateException("执行 SQL 查询时出现错误", e);
            }
        });
    }

    public CompletableFuture<Integer> updatePlayerGainedExp(UUID player, Integer integer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return DataTables.PLAYER_GAINED_EXP.createInsert()
                        .setColumnNames("uuid", "gained_exp")
                        .setParams(player.toString(), integer)
                        .execute();
            } catch (SQLException e) {
                throw new IllegalStateException("执行 SQL 查询时出现错误", e);
            }
        });

    }

    public CompletableFuture<Integer> deleteAll() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return DataTables.PLAYER_GAINED_EXP.createDelete().build().execute();
            } catch (SQLException e) {
                throw new IllegalStateException("执行 SQL 查询时出现错误", e);
            }
        });
    }
}
