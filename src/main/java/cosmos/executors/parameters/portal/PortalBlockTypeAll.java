package cosmos.executors.parameters.portal;

import cosmos.Cosmos;
import cosmos.constants.CosmosKeys;
import cosmos.executors.parameters.CosmosBuilder;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.ValueParameter;

public class PortalBlockTypeAll implements CosmosBuilder<BlockType> {

    private final Parameter.Value.Builder<BlockType> builder;

    public PortalBlockTypeAll() {
        final ValueParameter<BlockType> value = new PortalBlockTypeFilter(
                src -> Cosmos.services().message()
                        .getMessage(src, "error.invalid.value")
                        .replace("param", CosmosKeys.BLOCK_TYPE),
                blockType -> true
        );
        this.builder = Parameter.builder(BlockType.class, value);
        this.builder.key(CosmosKeys.BLOCK_TYPE);
    }

    @Override
    public Parameter.Value<BlockType> build() {
        return this.builder.build();
    }

    @Override
    public CosmosBuilder<BlockType> key(final Parameter.Key<BlockType> key) {
        this.builder.key(key);
        return this;
    }

    @Override
    public CosmosBuilder<BlockType> key(final String key) {
        return this.key(Parameter.key(key, BlockType.class));
    }

    @Override
    public CosmosBuilder<BlockType> optional() {
        this.builder.optional();
        return this;
    }

}
