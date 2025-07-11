package com.yourname.tutien.commands;
//... (dán nội dung file SetLinhKhiCommand.java)
import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.text.NumberFormat;
import java.util.Locale;
public class SetLinhKhiCommand implements CommandExecutor {
    private final TuTienPlugin plugin;
    public SetLinhKhiCommand(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Lệnh này chỉ dành cho người chơi.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("tutien.admin")) {
            player.sendMessage("§cBạn không có quyền sử dụng lệnh này.");
            return true;
        }
        if (args.length != 1) {
            player.sendMessage("§cSai cú pháp! Sử dụng: /setlinhkhi <số_lượng>");
            return true;
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            player.sendMessage("§cKhông tìm thấy dữ liệu của bạn, vui lòng thử lại sau.");
            return true;
        }
        try {
            long amount = Long.parseLong(args[0]);
            if (amount < 0) {
                player.sendMessage("§cSố linh khí không thể là số âm.");
                return true;
            }
            data.setLinhKhi(amount);
            String formattedAmount = NumberFormat.getNumberInstance(Locale.US).format(amount);
            player.sendMessage("§a[Admin] §fĐã đặt linh khí của bạn thành §e" + formattedAmount + "§f.");
        } catch (NumberFormatException e) {
            player.sendMessage("§cSố bạn nhập không hợp lệ.");
        }
        return true;
    }
}
