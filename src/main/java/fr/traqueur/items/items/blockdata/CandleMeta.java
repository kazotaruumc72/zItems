package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.block.data.type.Candle;

/**
 * BlockData metadata for candle blocks.
 * Sets the number of candles.
 */
public record CandleMeta(int candles) implements BlockDataMeta<Candle> {

    @Override
    public void apply(Candle blockData) {
        if (candles >= 1 && candles <= blockData.getMaximumCandles()) {
            blockData.setCandles(candles);
        }
    }
}
