package fenrisfox86.fenrispatch;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import fenrisfox86.fenrispatch.handlers.AlbertListener;
import fenrisfox86.fenrispatch.handlers.MobStackListener;
import fenrisfox86.fenrispatch.handlers.ProtocolLibListener;
import fenrisfox86.fenrispatch.handlers.RoachListener;
import fenrisfox86.fenrispatch.packet_listeners.RideListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class Fenrispatch extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Initialising FenrisPatch");
        new AlbertListener(this);
        new RoachListener(this);
        new MobStackListener(this);

        new ProtocolLibListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting down FenrisPatch");
    }
}
