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

import java.sql.Array;
import java.util.Arrays;
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
            boolean jump = packet.getBooleans().read(0);
            Location location = entity.getLocation();

            Vector jump_vector = new Vector (0, 0, 0);
            Vector velocity = new Vector(0, 0, 0);
            if (forward != 0.0 || side != 0.0) {
                velocity = player.getLocation().getDirection().setY(0).normalize();

                if (forward > 0) velocity.multiply(1);
                else velocity.multiply(-1);
            }
            if (jump && entity.isOnGround()) {
                velocity.setY(2);
            }

            velocity = velocity.add(velocity);

            entity.getLocation().setPitch(player.getLocation().getPitch());
            entity.setVelocity(velocity);
        }
    }
}
