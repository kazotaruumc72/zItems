package fr.traqueur.items.api.effects;

import fr.traqueur.items.api.settings.models.IngredientWrapper;
import fr.traqueur.items.api.settings.models.ItemStackWrapper;
import fr.traqueur.recipes.api.Util;
import fr.traqueur.recipes.api.domains.Ingredient;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.api.Loadable;

import java.util.List;

/**
 * Configuration for representing an effect as a physical item that can be applied to equipment.
 * When an effect has this representation, players can obtain it as an item and apply it
 * via smithing table or applicator GUI.
 */
public record EffectRepresentation(
        @Options(inline = true) ItemStackWrapper item,
        ApplicatorType applicatorType,
        @Options(optional = true) List<IngredientWrapper> ingredients,
        @Options(optional = true) IngredientWrapper template
) implements Loadable {

    /**
     * Gets the template ingredient parsed from the template string.
     * Used for smithing table recipes.
     *
     * @return the parsed ingredient, or null if template is not specified
     */
    public Ingredient getTemplateIngredient() {
        if (template == null) {
            return null;
        }
        return Util.parseIngredient(template.item(), template.sign(), template.strict());
    }

    /**
     * Defines how an effect can be applied to an item.
     */
    public enum ApplicatorType {
        /**
         * Use vanilla smithing table interface.
         */
        SMITHING_TABLE,

        /**
         * Use custom zItems applicator GUI.
         */
        ZITEMS_APPLICATOR
    }
}