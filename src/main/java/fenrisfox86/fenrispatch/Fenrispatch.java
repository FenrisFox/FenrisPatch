package fenrisfox86.fenrispatch;

import fenrisfox86.fenrispatch.handlers.AlbertListener;
import fenrisfox86.fenrispatch.handlers.MobStackListener;
import fenrisfox86.fenrispatch.handlers.RoachListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Fenrispatch extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Initialising FenrisPatch");
        new AlbertListener(this);
        new RoachListener(this);
        new MobStackListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("Shutting down FenrisPatch");
    }
}
