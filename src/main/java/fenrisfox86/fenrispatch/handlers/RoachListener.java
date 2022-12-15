package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
