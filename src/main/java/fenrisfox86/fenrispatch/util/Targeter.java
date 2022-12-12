package fenrisfox86.fenrispatch.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class Targeter {
    public static Entity getTargetEntity(
            final Entity entity, @Nullable Integer range_in, @Nullable List<Entity> blacklist) {
        final int range = range_in == null ? 5 : range_in;
        return getTarget(entity, entity.getNearbyEntities(range, range, range), range, blacklist);
    }

    public static <T extends Entity> T getTarget(
            Entity entity, Iterable<T> entities, Integer range, @Nullable List<T> blacklist) {
        T target = null;
        double tolerance = 2;
        //for each creature not blacklisted and closer to the current target
        for (final T other : entities) {
            final boolean enabled = !(blacklist != null && blacklist.contains(other));
            final boolean closest = (target == null || squareDistance(entity, target) > squareDistance(entity, other));
            if (closest && enabled) {
                // Create a tracer between player and other, ensure it's near corsair and not behind player
                final Vector tracer = locationVector(other).subtract(locationVector(entity));
                final boolean is_close = directionVector(entity).crossProduct(tracer).lengthSquared() < tolerance;
                final boolean is_behind = tracer.normalize().dot(directionVector(entity).normalize()) < 0;
                // replace old target if new one is valid
                if (is_close && !is_behind) target = other;
            }
            else if (!enabled) {
            }
        }
        return target;
    }

    public static double squareDistance(Entity one, Entity two) {
        return one.getLocation().distanceSquared(two.getLocation());
    }
    public static Vector locationVector(Entity entity) {return entity.getLocation().toVector();}
    public static Vector directionVector(Entity entity) {return entity.getLocation().getDirection().normalize();}
}
