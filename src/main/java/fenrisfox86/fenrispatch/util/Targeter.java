package fenrisfox86.fenrispatch.util;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class Targeter {
    public static Entity getTargetEntity(final Entity entity, @Nullable List<Entity> blacklist) {
        return getTarget(entity, entity.getWorld().getEntities(), blacklist);
    }

    public static <T extends Entity> T getTarget(Entity entity, Iterable<T> entities, @Nullable List<T> blacklist) {
        T target = null;
        double threshold = 1;
        //for each creature not blacklisted and closer to the current target
        for (final T other : entities) {
            final boolean enabled = !(blacklist != null && blacklist.contains(other));
            if ((target == null || getSquareDistance(entity, target) > getSquareDistance(entity, other)) && enabled) {
                // Create a tracer between player and other, ensure it's near corsair and not behind player
                final Vector tracer = locationVector(other).subtract(locationVector(entity));
                final boolean is_close = directionVector(entity).crossProduct(tracer).lengthSquared() < threshold;
                final boolean is_behind = tracer.normalize().dot(directionVector(entity).normalize()) < 0;
                // replace old target if new one is valid
                if (is_close && !is_behind) target = other;
            }
        }
        return target;
    }

    public static double getSquareDistance(Entity one, Entity two) {
        return one.getLocation().distanceSquared(two.getLocation());
    }
    public static Vector locationVector(Entity entity) {return entity.getLocation().toVector();}
    public static Vector directionVector(Entity entity) {return entity.getLocation().getDirection().normalize();}
}
