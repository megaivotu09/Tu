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

        // --- Cập nhật Máu ---
        // Sử dụng trực tiếp hằng số từ enum Attribute, đây là cách làm đúng và an toàn nhất.
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            // Xóa modifier cũ của plugin (nếu có) để tránh cộng dồn
            healthAttribute.getModifiers().stream()
                    .filter(m -> m.getUniqueId().equals(HEALTH_MODIFIER_UUID))
                    .forEach(healthAttribute::removeModifier);

            // Thêm modifier mới (cộng thêm phần chênh lệch so với máu gốc là 20)
            AttributeModifier healthModifier = new AttributeModifier(
                    HEALTH_MODIFIER_UUID, "tutien_health", maxHealth - 20.0,
                    AttributeModifier.Operation.ADD_NUMBER);
            healthAttribute.addModifier(healthModifier);
        }

        // --- Cập nhật Giáp ---
        AttributeInstance defenseAttribute = player.getAttribute(Attribute.GENERIC_ARMOR);
        if (defenseAttribute != null) {
            healthAttribute.getModifiers().stream()
                    .filter(m -> m.getUniqueId().equals(DEFENSE_MODIFIER_UUID))
                    .forEach(defenseAttribute::removeModifier);

            AttributeModifier defenseModifier = new AttributeModifier(
                    DEFENSE_MODIFIER_UUID, "tutien_defense", defense,
                    AttributeModifier.Operation.ADD_NUMBER);
            defenseAttribute.addModifier(defenseModifier);
        }

        // --- Cập nhật Sát thương ---
        AttributeInstance damageAttribute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (damageAttribute != null) {
            damageAttribute.getModifiers().stream()
                    .filter(m -> m.getUniqueId().equals(DAMAGE_MODIFIER_UUID))
                    .forEach(damageAttribute::removeModifier);
            
            AttributeModifier damageModifier = new AttributeModifier(
                    DAMAGE_MODIFIER_UUID, "tutien_damage", attackDamage,
                    AttributeModifier.Operation.ADD_NUMBER);
            damageAttribute.addModifier(damageModifier);
        }
    }
}
