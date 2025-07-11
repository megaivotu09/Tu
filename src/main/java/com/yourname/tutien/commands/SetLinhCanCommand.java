package com.yourname.tutien.commands;
//... (dán nội dung file SetLinhCanCommand.java)
import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.LinhCan;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.stream.Collectors;
public class SetLinhCanCommand implements CommandExecutor {
    private final TuTienPlugin plugin;
    public SetLinhCanCommand(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("tutien.admin")) {
            sender.sendMessage("§cBạn không có quyền sử dụng lệnh này.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("§cSai cú pháp! Sử dụng: /setlinhcan <tên_người_chơi> <tên_linh_căn>");
            String validLinhCan = Arrays.stream(LinhCan.values()).map(Enum::name).collect(Collectors.joining(", "));
            sender.sendMessage("§eCác linh căn hợp lệ: " + validLinhCan);
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cKhông tìm thấy người chơi: " + args[0]);
            return true;
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(target);
        if (data == null) {
            sender.sendMessage("§cKhông tìm thấy dữ liệu tu tiên của người chơi này.");
            return true;
        }
        try {
            LinhCan newLinhCan = LinhCan.valueOf(args[1].toUpperCase());
            data.setLinhCan(newLinhCan);
            sender.sendMessage("§aĐã đặt linh căn của người chơi §e" + target.getName() + "§a thành " + newLinhCan.getTenHienThi());
            target.sendMessage("§a[Hệ Thống] §fLinh căn của bạn đã được một Tiên Nhân thay đổi thành " + newLinhCan.getTenHienThi());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cTên linh căn không hợp lệ. Hãy sử dụng một trong các tên ở trên.");
        }
        return true;
    }
}
