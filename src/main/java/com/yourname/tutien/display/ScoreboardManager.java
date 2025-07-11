package com.yourname.tutien.display;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import com.yourname.tutien.player.TuLuyenInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ScoreboardManager {
    private final TuTienPlugin plugin;
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

    public ScoreboardManager(TuTienPlugin plugin) { this.plugin = plugin; }

    public void setScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("tutien_sb", Criteria.DUMMY, "§6§lTU TIÊN");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);
        updateScoreboard(player);
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public void updateScoreboard(Player player) {
        if (!player.isOnline()) return;
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) return;
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            objective.getScore("§7Đang tải dữ liệu...").setScore(1);
            return;
        }
        TuLuyenInfo info = data.getTuLuyenInfo();
        objective.getScore("§7§m--------------------").setScore(8);
        objective.getScore("§fLinh Căn: " + data.getLinhCan().getTenHienThi()).setScore(7);
        objective.getScore("§fCảnh Giới:").setScore(6);
        objective.getScore("  " + info.getTenHienThiDayDu()).setScore(5);
        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(4);
        long linhKhiCan = info.getLinhKhiCanThiet();
        if (linhKhiCan != Long.MAX_VALUE) {
            if (data.canGrandBreakthrough()) {
                 objective.getScore("§d§lSẵn sàng đột phá!").setScore(3);
            } else {
                 objective.getScore("§fLinh Khí: §a" + formatNumber(data.getLinhKhi())).setScore(3);
            }
        } else {
            objective.getScore("§fLinh Khí: §6§lVIÊN MÃN").setScore(3);
        }
        objective.getScore(ChatColor.DARK_BLUE.toString()).setScore(2);
        objective.getScore("§eplay.yourserver.com").setScore(1);
        objective.getScore("§7§m--------------------" + ChatColor.WHITE).setScore(0);
    }

    private String formatNumber(long number) {
        return numberFormat.format(number);
    }
}
