package com.yourname.tutien.commands;
//... (dán nội dung file TuTienCommand.java)
import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.mechanics.RemnantSoulData;
import com.yourname.tutien.mechanics.StatManager;
import com.yourname.tutien.player.PlayerData;
import com.yourname.tutien.player.TuLuyenInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.text.NumberFormat;
import java.util.Locale;
public class TuTienCommand implements CommandExecutor {
    private final TuTienPlugin plugin;
    public TuTienCommand(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Lệnh này chỉ dành cho người chơi.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length > 0 && args[0].equalsIgnoreCase("truyenlinhkhi")) {
            handleTruyenLinhKhi(player, args);
            return true;
        }
        showPlayerInfo(player);
        return true;
    }
    private void handleTruyenLinhKhi(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage("§cUsage: /tutien truyenlinhkhi <tên_tàn_hồn> <số_lượng>");
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§cKhông tìm thấy người chơi này.");
            return;
        }
        try {
            long amount = Long.parseLong(args[2]);
            if (amount <= 0) {
                player.sendMessage("§cSố linh khí phải lớn hơn 0.");
                return;
            }
            plugin.getRemnantSoulManager().transferLinhKhi(player, target, amount);
        } catch (NumberFormatException e) {
            player.sendMessage("§cSố linh khí không hợp lệ.");
        }
    }
    private void showPlayerInfo(Player player) {
        if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) {
            showSoulInfo(player);
            return;
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            player.sendMessage("§cĐang tải dữ liệu, vui lòng thử lại sau giây lát.");
            return;
        }
        TuLuyenInfo info = data.getTuLuyenInfo();
        long linhKhiHienTai = data.getLinhKhi();
        long linhKhiCan = info.getLinhKhiCanThiet();
        StatManager statManager = new StatManager(data.getTuLuyenInfo());
        double maxHealth = statManager.getMaxHealth();
        double defense = statManager.getDefense();
        double attack = statManager.getAttackDamage() + 1.0;
        player.sendMessage("§7§m-----§r §6§lThông Tin Tu Luyện§r §7§m-----");
        player.sendMessage(" §bĐạo Hữu: §f" + player.getName());
        player.sendMessage(" §bLinh Căn: " + data.getLinhCan().getTenHienThi());
        player.sendMessage(" §bCảnh Giới: §e§l" + info.getTenHienThiDayDu());
        player.sendMessage(String.format(" §bThể Lực (Máu): §c%.1f", maxHealth));
        player.sendMessage(String.format(" §bPhòng Ngự (Giáp): §3%.1f", defense));
        player.sendMessage(String.format(" §bSức Mạnh (Sát thương): §6%.1f", attack));
        if (linhKhiCan != Long.MAX_VALUE) {
            player.sendMessage(String.format(" §bLinh Khí: §a%s §f/ §c%s", formatNumber(linhKhiHienTai), formatNumber(linhKhiCan)));
            if (data.canGrandBreakthrough()) {
                player.sendMessage(" §d§lĐã đủ điều kiện, hãy dùng /dotpha để đột phá!");
            } else if (info.getTang() == 9) {
                long yeuCau = (long) (linhKhiCan * plugin.getConfigManager().DOT_PHA_VUOT_MOC);
                player.sendMessage(" §bCần thêm: §e" + formatNumber(yeuCau - linhKhiHienTai) + " §blinh khí để độ kiếp.");
            }
        } else {
            player.sendMessage(" §bLinh Khí: §a" + formatNumber(linhKhiHienTai));
            player.sendMessage("§6§lĐạo hữu đã đạt đến đỉnh cao của tu tiên giới!");
        }
        player.sendMessage("§7§m---------------------------------");
    }
    private void showSoulInfo(Player player) {
        RemnantSoulData soulData = plugin.getRemnantSoulManager().getSoulData(player.getUniqueId());
        player.sendMessage("§7§m-----§r §8§lTrạng Thái Tàn Hồn§r §7§m-----");
        player.sendMessage("§fLinh khí đã thu thập: §e" + formatNumber(soulData.getLinhKhiCollected()) + " §7/ §c" + formatNumber(soulData.getLinhKhiNeeded()));
        long minutes = soulData.getTimeRemaining() / 60;
        long seconds = soulData.getTimeRemaining() % 60;
        player.sendMessage(String.format("§fThời gian còn lại: §c%02d phút %02d giây", minutes, seconds));
        player.sendMessage("§7Hãy nhờ người khác dùng §e/tutien truyenlinhkhi " + player.getName() + " <số>");
        player.sendMessage("§7§m---------------------------------");
    }
    private String formatNumber(long number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }
}
