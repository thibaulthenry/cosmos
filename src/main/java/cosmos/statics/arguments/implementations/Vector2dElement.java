package cosmos.statics.arguments.implementations;

import com.flowpowered.math.vector.Vector2d;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.SpongeApiTranslationHelper;
import org.spongepowered.api.util.StartsWithPredicate;
import org.spongepowered.api.util.blockray.BlockRay;
import org.spongepowered.api.util.blockray.BlockRayHit;
import org.spongepowered.api.world.Locatable;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings({"NullableProblems", "ReuseOfLocalVariable"})
public class Vector2dElement extends EnhancedCommandElement {

    private static final ImmutableSet<String> SPECIAL_TOKENS = ImmutableSet.of("#target", "#me");

    public Vector2dElement(@Nullable Text key) {
        super(key);
    }

    private static double parseRelativeDouble(CommandArgs args, String arg, @Nullable Double relativeTo) throws ArgumentParseException {
        boolean relative = arg.startsWith("~");
        if (relative) {
            if (relativeTo == null) {
                throw args.createError(SpongeApiTranslationHelper.t("Relative position specified but source does not have a position"));
            }
            arg = arg.substring(1);
            if (arg.isEmpty()) {
                return relativeTo;
            }
        }
        try {
            double ret = Double.parseDouble(arg);
            return relative ? ret + relativeTo : ret;
        } catch (NumberFormatException e) {
            throw args.createError(SpongeApiTranslationHelper.t("Expected input %s to be a double, but was not", arg));
        }
    }

    @Override
    public Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String xStr;
        String zStr;
        xStr = args.next();
        if (xStr.contains(",")) {
            String[] split = xStr.split(",");
            if (split.length != 2) {
                throw args.createError(SpongeApiTranslationHelper.t("Comma-separated vector must have 2 elements, not %s", split.length));
            }
            xStr = split[0];
            zStr = split[1];
        } else if ("#target".equals(xStr) && source instanceof Entity) {
            Optional<BlockRayHit<World>> hit = BlockRay
                    .from(((Entity) source))
                    .whilst(BlockRay.continueAfterFilter(BlockRay.onlyAirFilter(), 1))
                    .build()
                    .end();
            if (!hit.isPresent()) {
                throw args.createError(SpongeApiTranslationHelper.t("No target block is available! Stop stargazing!"));
            }
            return hit.get().getPosition().toVector2(true);
        } else if ("#me".equalsIgnoreCase(xStr) && source instanceof Locatable) {
            return ((Locatable) source).getLocation().getPosition().toVector2(true);
        } else {
            zStr = args.next();
        }

        double x = parseRelativeDouble(args, xStr, source instanceof Locatable ? ((Locatable) source).getLocation().getX() : null);
        double z = parseRelativeDouble(args, zStr, source instanceof Locatable ? ((Locatable) source).getLocation().getZ() : null);

        return new Vector2d(x, z);
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        Optional<String> arg = args.nextIfPresent();
        // Traverse through the possible arguments. We can't really complete arbitrary integers
        if (arg.isPresent()) {
            if (arg.get().startsWith("#")) {
                return SPECIAL_TOKENS.stream().filter(new StartsWithPredicate(arg.get())).collect(Collectors.toList());
            } else if (arg.get().contains(",") || !args.hasNext()) {
                return ImmutableList.of(arg.get());
            } else {
                arg = args.nextIfPresent();
                if (args.hasNext()) {
                    args.nextIfPresent().map(ImmutableList::of).orElseGet(ImmutableList::of);
                }
                return arg.map(ImmutableList::of).orElseGet(ImmutableList::of);
            }
        }
        return ImmutableList.of();
    }
}
