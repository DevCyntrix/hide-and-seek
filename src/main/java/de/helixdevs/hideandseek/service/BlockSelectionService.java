package de.helixdevs.hideandseek.service;

import com.google.common.base.Preconditions;
import de.helixdevs.hideandseek.api.Service;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class BlockSelectionService implements Service {

    private final BlockData[] blockData;
    private final Map<Player, BlockData> selectedBlock = new HashMap<>();

    public BlockSelectionService(BlockData... blockData) {
        this.blockData = Arrays.stream(blockData).filter(data -> data.getMaterial().isBlock()).toArray(BlockData[]::new);
        Preconditions.checkArgument(this.blockData.length > 0);
    }

    public BlockData[] getSelectableBlocks() {
        return blockData;
    }

    public void select(@NotNull Player player, @NotNull BlockData data) {
        this.selectedBlock.put(player, data);
    }

    public @Nullable BlockData get(@NotNull Player player) {
        return this.selectedBlock.get(player);
    }

    public void unset(@NotNull Player player) {
        this.selectedBlock.remove(player);
    }

    public void clear() {
        this.selectedBlock.clear();
    }

    public @NotNull BlockData selectRandom(Player player) {
        BlockData data = this.blockData[ThreadLocalRandom.current().nextInt(blockData.length)];
        select(player, data);
        return data;
    }

    @Override
    public void close() {
    }
}
