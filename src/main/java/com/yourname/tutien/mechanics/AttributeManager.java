package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AttributeManager {
    private final TuTienPlugin plugin;
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("a8a3a3e6-71d5-45a2-8447-5d5a6a35368a");
    private static final UUID DEFENSE_MODIFIER_UUID = UUID.fromString("b8b3b3e6-71d5-45a2-8447-5d5a6b35368b");
    private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("c8c3c3e6-71d5-45a2-8447-5d5a6c35368c");

    public AttributeManager(TuTienPlugin plugin) {
        this.plugin = plugin;
    }

    public void updatePlayerAttributes(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) return;

        StatManager statManager = new StatManager(data.getTuLuyenInfo());
        double maxHealth = statManager.getMaxHealth();
        double defense = statManager.getDefense();
        double attackDamage = statManager.getAttackDamage();

        updateAttribute(player, Attribute.GENERIC_MAX_HEALTH, HEALTH_MODIFIER_UUID, "tutien_health", maxHealth - 20);
        updateAttribute(player, Attribute.GENERIC_ARMOR, DEFENSE_MODIFIER_UUID, "tutien_defense", defense);
        updateAttribute(player, Attribute.GENERIC_ATTACK_DAMAGE, DAMAGE_MODIFIER_UUID, "tutien_damage", attackDamage);
    }

    private void updateAttribute(Player player, Attribute attribute, UUID uuid, String name, double value) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;

        instance.getModifiers().stream()
                .filter(m -> m.getUniqueId().equals(uuid))
                .forEach(instance::removeModifier);

        AttributeModifier modifier = new AttributeModifier(uuid, name, value, AttributeModifier.Operation.ADD_NUMBER);
        instance.addModifier(modifier);
    }
}
