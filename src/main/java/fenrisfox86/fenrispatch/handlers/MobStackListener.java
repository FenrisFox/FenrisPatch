package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import fenrisfox86.fenrispatch.util.Targeter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class MobStackListener extends AbstractEventListener {
    public MobStackListener(Fenrispatch plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMobInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        // Entity entity = event.getRightClicked();
        Entity entity = Targeter.getTargetEntity(player, 5, player.getPassengers());
        if (entity instanceof LivingEntity) {
            setTopRider(player, entity);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            Player player = event.getPlayer();
            dropRiders(player);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!event.getPlayer().getPassengers().isEmpty()) {
            Vector movementVector = event.getPlayer().getLocation().getDirection()
                    .normalize().multiply(2);
            slingTopRider(event.getPlayer(), movementVector);
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
