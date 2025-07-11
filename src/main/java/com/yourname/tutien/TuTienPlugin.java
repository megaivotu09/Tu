package com.yourname.tutien;

import com.yourname.tutien.commands.*;
import com.yourname.tutien.core.ConfigManager;
import com.yourname.tutien.display.BossBarManager;
import com.yourname.tutien.display.ScoreboardManager;
import com.yourname.tutien.listeners.*;
import com.yourname.tutien.mechanics.*;
import com.yourname.tutien.player.PlayerDataManager;
import com.yourname.tutien.storage.StorageManager;
import com.yourname.tutien.tasks.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashSet;
import java.util.UUID;

public final class TuTienPlugin extends JavaPlugin {
    private static TuTienPlugin instance;
    private StorageManager storageManager;
    private PlayerDataManager playerDataManager;
    private ConfigManager configManager;
    private AttributeManager attributeManager;
    private FlightManager flightManager;
    private MeditationManager meditationManager;
    private TribulationManager tribulationManager;
    private RemnantSoulManager remnantSoulManager;
    private ScoreboardManager scoreboardManager;
    private BossBarManager bossBarManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();
        this.storageManager = new StorageManager(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.attributeManager = new AttributeManager(this);
        this.flightManager = new FlightManager(this);
        this.meditationManager = new MeditationManager(this);
        this.tribulationManager = new TribulationManager(this);
        this.remnantSoulManager = new RemnantSoulManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.bossBarManager = new BossBarManager(this);

        getCommand("tutien").setExecutor(new TuTienCommand(this));
        getCommand("tuluyen").setExecutor(new TuLuyenCommand(this));
        getCommand("dotpha").setExecutor(new DotPhaCommand(this));
        getCommand("setlinhkhi").setExecutor(new SetLinhKhiCommand(this));
        getCommand("setlinhcan").setExecutor(new SetLinhCanCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerCombatListener(this), this);
        getServer().getPluginManager().registerEvents(new MeditationListener(this), this);

        new LinhKhiTask(this).runTaskTimerAsynchronously(this, 0L, 100L);
        new DisplayUpdateTask(this).runTaskTimer(this, 0L, 20L);
        new FlightCheckTask(this).runTaskTimer(this, 0L, 20L);
        new ParticleTask(this).runTaskTimer(this, 0L, 4L);
        new SoulDecayTask(this).runTaskTimer(this, 0L, 20L);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerDataManager.loadPlayerData(player);
        }
        getLogger().info("§aPlugin TuTienPlugin da duoc bat!");
    }

    @Override
    public void onDisable() {
        for (UUID uuid : new HashSet<>(meditationManager.getMeditatingPlayers())) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) meditationManager.stopMeditation(player);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (remnantSoulManager.isSoul(player.getUniqueId())) {
                remnantSoulManager.dissipateSoul(player.getUniqueId());
            } else {
                playerDataManager.saveAndUnloadPlayerData(player);
            }
        }
        getLogger().info("§cPlugin TuTienPlugin da duoc tat!");
    }

    public static TuTienPlugin getInstance() { return instance; }
    public StorageManager getStorageManager() { return storageManager; }
    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public ConfigManager getConfigManager() { return configManager; }
    public AttributeManager getAttributeManager() { return attributeManager; }
    public FlightManager getFlightManager() { return flightManager; }
    public MeditationManager getMeditationManager() { return meditationManager; }
    public TribulationManager getTribulationManager() { return tribulationManager; }
    public RemnantSoulManager getRemnantSoulManager() { return remnantSoulManager; }
    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }
    public BossBarManager getBossBarManager() { return bossBarManager; }
}
