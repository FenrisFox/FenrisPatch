package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class AlbertListener extends AbstractEventListener {
    public AlbertListener(Fenrispatch plugin) {
        super(plugin);
    }

    // Make entities named "Albert" almost invincible, except when hit with a weapon named "/kill"
    @EventHandler
    public void onDamagedAlbert(EntityDamageEvent event) {
        boolean nemesisWeapon = false;

        // Check if the damage would be dealt by a weapon named "/kill" (nemesis weapon)
        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager_entity= (((EntityDamageByEntityEvent)event).getDamager());
            if (damager_entity instanceof HumanEntity) {
                ItemStack main_hand_item = ((HumanEntity) damager_entity).getInventory().getItemInMainHand();
                ItemMeta item_meta = main_hand_item.getItemMeta();
                if (item_meta != null) {
                    if (item_meta.getDisplayName().equals("/kill")) {
                        nemesisWeapon = true;
                    }
                }
            }
        }

        // Kill 'Albert's with any item named "/kill", otherwise make 'Albert's invincible
        if (Objects.equals(event.getEntity().getCustomName(), "Albert")) {
            if (nemesisWeapon) {
                event.setDamage(10000000);
            } else {
                event.setCancelled(true);
            }
        }
    }
}
