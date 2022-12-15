package fenrisfox86.fenrispatch.handlers;

import com.comphenix.protocol.ProtocolLibrary;
import fenrisfox86.fenrispatch.Fenrispatch;
import fenrisfox86.fenrispatch.packet_listeners.RideListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ProtocolLibListener implements Listener {
    Fenrispatch plugin;
    public ProtocolLibListener(Fenrispatch plugin_in) {
        plugin = plugin_in;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onServerLoad(ServerLoadEvent event){
        if(!plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib")){
            plugin.getLogger().warning("ProtocolLib not found! FenrisPatch won't be fully functional!");
            return;
        }

        new RideListener(plugin, ProtocolLibrary.getProtocolManager());
    }
}
