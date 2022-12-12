package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class RoachListener extends AbstractEventListener {
    public RoachListener(Fenrispatch plugin) {
        super(plugin);
    }

    // Make entities named "Roach" rideable
    @EventHandler
    public void onInteractRoach(PlayerInteractEntityEvent event) {
        if (Objects.equals(event.getRightClicked().getCustomName(), "Roach") &
                event.getRightClicked() instanceof LivingEntity) {
            event.getRightClicked().addPassenger(event.getPlayer());
        }
    }
}
