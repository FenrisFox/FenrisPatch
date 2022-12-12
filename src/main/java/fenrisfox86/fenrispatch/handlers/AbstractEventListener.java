package fenrisfox86.fenrispatch.handlers;

import fenrisfox86.fenrispatch.Fenrispatch;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class AbstractEventListener implements Listener {
    public AbstractEventListener(Fenrispatch plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
