package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import fenrisfox86.fenrispatch.util.Targeter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.Objects;

public class MobStackListener extends AbstractEventListener {
    public MobStackListener(Fenrispatch plugin) {
        super(plugin);
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

    @EventHandler
    public void entitySling(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final boolean holds_sling_item = player.getInventory().getItemInMainHand().getType() == Material.LEAD;
        if (holds_sling_item && getBaseRider(event.getRightClicked()) == player) {
            Vector movementVector = player.getLocation().getDirection().normalize().multiply(2);
            slingTopRider(player, movementVector);
            event.setCancelled(true);
        }
    }

    public static void setTopRider(Entity vehicle, Entity passenger_in) {
        if (passenger_in != vehicle) {
            if (vehicle.getPassengers().isEmpty()) {vehicle.addPassenger(passenger_in);}
            else {setTopRider(vehicle.getPassengers().get(0), passenger_in);}
        }
    }

    public static void slingTopRider(Entity entity, Vector sling_vector) {
        if (!entity.getPassengers().isEmpty()) {
            slingTopRider(entity.getPassengers().get(0), sling_vector);
        } else if (entity.isInsideVehicle()) {
            entity.leaveVehicle();
            entity.setVelocity(sling_vector);
            entity.setFallDistance(-1000000);
        }
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
