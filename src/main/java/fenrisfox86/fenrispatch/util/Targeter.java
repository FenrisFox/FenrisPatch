package fenrisfox86.fenrispatch.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Targeter {
    public static Entity getTargetEntity(final Entity entity,
                                         @Nullable Integer range_in,
                                         @Nullable List<Entity> blacklist,
                                         @Nullable Predicate<Entity> predicate,
                                         @Nullable Class<? extends Entity> cls) {
        final int range = range_in == null ? 5: range_in;
        return getTarget(entity, entity.getNearbyEntities(range, range, range), blacklist, predicate, cls);
    }

    public static Entity getTargetEntity(final Entity entity) {
        return getTargetEntity(entity, null, null, null, null);}
    public static Entity getTargetEntity(final Entity entity, Predicate<Entity> predicate) {
        return getTargetEntity(entity, null, null, predicate, null);}
    public static <T extends Entity> Entity getTargetEntity(final Entity entity, Predicate<Entity> predicate, Class<T> cls) {
        return getTargetEntity(entity, null, null, predicate, cls);}
    public static Entity getTargetEntity(final Entity entity, List<Entity> blacklist) {
        return getTargetEntity(entity, null, blacklist, null, null);}
    public static Entity getTargetEntity(final Entity entity, List<Entity> blacklist, Predicate<Entity> predicate) {
        return getTargetEntity(entity, null, blacklist, predicate, null);}


    public static <T extends Entity> Entity getTarget(Entity entity, Iterable<Entity> entities,
            @Nullable List<Entity> blackListIn,
            @Nullable Predicate<Entity> predicateIn,
            @Nullable Class<T> clsIn) {

        Entity target = null;
        Class<? extends Entity> cls = clsIn == null ? Entity.class : clsIn;
        double tolerance = 2;
        //for each creature not blacklisted, valid and closer to the current target
        for (final Entity other : entities) {
            final boolean nonBlacklisted = !(blackListIn != null && blackListIn.contains(other));
            final boolean meetsCondition = !(predicateIn != null && !predicateIn.test(other)) && cls.isInstance(other);
            final boolean closest = (target == null || squareDistance(entity, target) > squareDistance(entity, other));
            if (closest && nonBlacklisted && meetsCondition) {
                // Create a tracer between player and other, ensure it's near corsair and not behind player
                final Vector tracer = locationVector(other).subtract(locationVector(entity));
                final boolean is_close = directionVector(entity).crossProduct(tracer).lengthSquared() < tolerance;
                final boolean is_behind = tracer.normalize().dot(directionVector(entity).normalize()) < 0;
                // replace old target if new one is valid
                if (is_close && !is_behind) target = other;
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
