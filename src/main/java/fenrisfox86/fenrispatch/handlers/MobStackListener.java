package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import fenrisfox86.fenrispatch.util.Targeter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.Objects;

public class MobStackListener implements Listener {
    public MobStackListener(Fenrispatch plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void entityPickup(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player){
            Player player = (Player)damager;
            if (player.getInventory().getItemInMainHand().getType() == Material.LEAD) {
                Entity target = Targeter.getTargetEntity(player, (x) -> x.getVehicle() != player, LivingEntity.class);
                if (target != null) setTopRider(player, target);
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

    /*@EventHandler
    public void entitySling(PlayerInteractEvent event) {
        Bukkit.getLogger().info("handling PlayerInteractEntityEvent");
        final Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final boolean holds_sling_item = player.getInventory().getItemInMainHand().getType() == Material.LEAD;
            if (holds_sling_item) {
                Vector movementVector = player.getLocation().getDirection().normalize().multiply(2);
                slingEntity(getTopRider(player), movementVector);
            }
        }
    }*/

    @EventHandler
    public void entitySling(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        if (event.getRightClicked().getVehicle() == player) {
            final boolean holds_sling_item = player.getInventory().getItemInMainHand().getType() == Material.LEAD;
            if (holds_sling_item) {
                Vector movementVector = player.getLocation().getDirection().normalize().multiply(2);
                Bukkit.getLogger().info("Slinging top entity");
                slingEntity(getTopRider(player), movementVector);
                event.setCancelled(true);
            }
        }
    }

    public static Entity getTopRider(Entity vehicle) {
        if (vehicle.getPassengers().isEmpty()) return vehicle;
        return getTopRider(vehicle.getPassengers().get(0));
    }

    public static void setTopRider(Entity vehicle, Entity passenger_in) {
        if (passenger_in != vehicle) {
            getTopRider(vehicle).addPassenger(passenger_in);}
        }

    public static void slingEntity(Entity entity, Vector sling_vector) {
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
