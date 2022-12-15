package fenrisfox86.fenrispatch.packet_listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import fenrisfox86.fenrispatch.Fenrispatch;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Objects;

public class RideListener extends PacketAdapter {
    public RideListener(Fenrispatch plugin, ProtocolManager manager) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE);
        manager.addPacketListener(this);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PacketContainer packet = event.getPacket();
        Entity entity = player.getVehicle();

        if(entity != null && Objects.equals(entity.getCustomName(), "Roach")) {
            float side = packet.getFloat().read(0);
            float forward = packet.getFloat().read(1);
            Location location = entity.getLocation();
            Vector velocity = player.getLocation().getDirection().setY(0).normalize();

            if (forward==0 && side == 0) {velocity.multiply(0);}
            else {
                if (forward > 0) velocity.multiply(2);
                else velocity.multiply(-2);
                /*if (side > 0) location.setPitch(entity.getLocation().getPitch() + 20F);
                else if (side < 0 ) {location.setPitch(entity.getLocation().getPitch() + -20F);}*/
            }
            Bukkit.getLogger().info(velocity.toString());
            entity.getLocation().setPitch(player.getLocation().getPitch());
            entity.setVelocity(velocity);
        }
    }
}
