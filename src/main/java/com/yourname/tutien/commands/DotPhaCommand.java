package com.yourname.tutien.commands;
//... (dán nội dung file DotPhaCommand.java)
import com.yourname.tutien.TuTienPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
public class DotPhaCommand implements CommandExecutor {
    private final TuTienPlugin plugin;
    public DotPhaCommand(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Lệnh này chỉ dành cho người chơi.");
            return true;
        }
        plugin.getTribulationManager().startTribulation((Player) sender);
        return true;
    }
}
