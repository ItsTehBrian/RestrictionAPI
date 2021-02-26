package xyz.tehbrian.restrictionhelper.restrictions;

import com.plotsquared.core.plot.Plot;
import org.bukkit.entity.Player;
import xyz.tehbrian.restrictionhelper.ActionType;
import xyz.tehbrian.restrictionhelper.DebugLogger;
import xyz.tehbrian.restrictionhelper.Restriction;

import java.util.Objects;

public final class PlotSquaredRestriction extends Restriction {

    public PlotSquaredRestriction(final DebugLogger debugLogger) {
        super(debugLogger);
    }

    public boolean check(final Player player, final org.bukkit.Location bukkitLoc, final ActionType actionType) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(bukkitLoc);

        com.plotsquared.core.location.Location psLoc = new com.plotsquared.core.location.Location(
                Objects.requireNonNull(bukkitLoc.getWorld()).getName(),
                bukkitLoc.getBlockX(),
                bukkitLoc.getBlockY(),
                bukkitLoc.getBlockZ());

        if (psLoc.isPlotArea() || psLoc.isPlotRoad()) {
            // Location is in a plot area.

            // TODO If the player isn't in a proper plot (ie. the road) then plot will be null.
            // We need to figure out another method
            // of checking whether a player can build,
            // so that players who have proper permissions
            // will still be validated in roads.
            Plot plot = psLoc.getPlot();

            if (plot == null) {
                debugLogger.log("PS: FAILED - Plot is null.");
                return false;
            }

            if (plot.isAdded(player.getUniqueId())) {
                debugLogger.log("PS: PASSED - Player is added to plot.");
                return true;
            } else {
                debugLogger.log("PS: FAILED - Player is not added to plot.");
                return false;
            }
        } else {
            // Location is not in a plot area.
            debugLogger.log("PS: PASSED - Location isn't PlotArea or PlotRoad.");
            return true;
        }
    }
}
