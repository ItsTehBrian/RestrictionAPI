package xyz.tehbrian.restrictionhelper.restrictions;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;
import xyz.tehbrian.restrictionhelper.ActionType;
import xyz.tehbrian.restrictionhelper.DebugLogger;
import xyz.tehbrian.restrictionhelper.Restriction;

import java.util.Objects;

public final class WorldGuardRestriction extends Restriction {

    public WorldGuardRestriction(final DebugLogger debugLogger) {
        super(debugLogger);
    }

    @Override
    public boolean check(final Player player, final org.bukkit.Location bukkitLoc, final ActionType actionType) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(bukkitLoc);

        // Convert Bukkit objects to WorldEdit's proprietary objects
        com.sk89q.worldedit.util.Location weLoc = BukkitAdapter.adapt(bukkitLoc);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        // WorldGuard API
        WorldGuard worldGuard = WorldGuard.getInstance();

        // Player has permission to bypass
        if (worldGuard.getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld())) {
            debugLogger.log("WG: PASSED - Player has bypass.");
            return true;
        }

        // Shamelessly copied from https://worldguard.enginehub.org/en/latest/developer/regions/protection-query/
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        // Player has permission for everything, no need to check
        // specific flags.
        if (query.testState(weLoc, localPlayer, Flags.BUILD)) {
            debugLogger.log("WG: PASSED - Player has BUILD flag.");
            return true;
        }

        StateFlag flagToCheck;
        switch (actionType) {
            case ALL:
                flagToCheck = Flags.BUILD;
                break;
            case PLACE:
                flagToCheck = Flags.BLOCK_PLACE;
                break;
            case BREAK:
                flagToCheck = Flags.BLOCK_BREAK;
                break;
            case INTERACT:
                flagToCheck = Flags.INTERACT;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + actionType);
        }

        if (!query.testState(weLoc, localPlayer, flagToCheck)) {
            debugLogger.log("WG: FAILED - Player does not have %s flag.", flagToCheck.getName());
            return false;
        }

        debugLogger.log("WG: PASSED - Default return value.");
        return true;
    }
}
