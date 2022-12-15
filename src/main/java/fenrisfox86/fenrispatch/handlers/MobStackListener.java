package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import fenrisfox86.fenrispatch.util.Targeter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.Objects;

public class MobStackListener implements Listener {
    public final Material stack_item = Material.BOW;

    public MobStackListener(Fenrispatch plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void entityPickup(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player){
            Player player = (Player)damager;
            if (player.getInventory().getItemInMainHand().getType() == stack_item) {
                Entity target = Targeter.getTargetEntity(player, (x) -> x.getVehicle() != player, LivingEntity.class);
                if (target != null) getTopRider(player).addPassenger(target);
                if (event.getEntity().getVehicle() == player || event.getEntity() == target) event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void massDismount(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            Player player = event.getPlayer();
            dropRiders(player);
        }
    }

    @EventHandler
    public void onPlayerShootArrow(ProjectileLaunchEvent event) {
        ProjectileSource source = event.getEntity().getShooter();
        if (source instanceof HumanEntity && !((HumanEntity) source).getPassengers().isEmpty()) {
            Vector velocity = event.getEntity().getVelocity();
            sling(getTopRider((HumanEntity) source), velocity);
            event.setCancelled(true);
        }
    }

    public static Entity getTopRider(Entity vehicle) {
        if (vehicle.getPassengers().isEmpty()) return vehicle;
        return getTopRider(vehicle.getPassengers().get(0));
    }

    public static void sling(Entity entity, Vector sling_vector) {
        entity.leaveVehicle();
        entity.setVelocity(sling_vector);
        entity.setFallDistance(-1000000);
    }

    public static Entity getBaseRider(Entity mount) {
        return mount.isInsideVehicle() ? getBaseRider(Objects.requireNonNull(mount.getVehicle())) : mount;
    }

    public static void dropRiders(Entity mount) {
        for (Entity rider: mount.getPassengers()) {
            rider.leaveVehicle();
            rider.setFallDistance(-1000000);
            dropRiders(rider);
        }
    }
}
