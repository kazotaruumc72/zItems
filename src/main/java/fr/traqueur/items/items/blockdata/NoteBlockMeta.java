package fr.traqueur.items.items.blockdata;

import fr.traqueur.items.api.annotations.AutoBlockDataMeta;
import fr.traqueur.items.api.blockdata.BlockDataMeta;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.block.data.type.NoteBlock;

/**
 * BlockData metadata for note blocks.
 * Sets the instrument and note.
 */
@AutoBlockDataMeta("note-block")
public record NoteBlockMeta(Instrument instrument, int note) implements BlockDataMeta<NoteBlock> {

    @Override
    public void apply(NoteBlock blockData) {
        blockData.setInstrument(instrument);
        blockData.setNote(new Note(note));
    }
}
