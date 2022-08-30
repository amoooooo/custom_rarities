package coffee.amo.custom_rarities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.awt.*;
import java.util.Map;

public class CustomRarityJSONListener extends SimpleJsonResourceReloadListener {
    public static final Gson GSON = new Gson();
    public CustomRarityJSONListener() {
        super(GSON, "rarities");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new CustomRarityJSONListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager rm, ProfilerFiller profiler) {
        CustomRarities.LOGGER.info("Loading custom rarity data...");
        for(int i = 0; i < object.size(); i++){
            ResourceLocation key = (ResourceLocation) object.keySet().toArray()[i];
            JsonObject jsonObject = object.get(key).getAsJsonObject();
            String name = jsonObject.getAsJsonPrimitive("name").getAsString();
            CustomRarities.LOGGER.info("Loading rarity data for " + name);
            String color = jsonObject.getAsJsonPrimitive("color").getAsString();
            JsonArray items = jsonObject.getAsJsonArray("items");
            for(JsonElement item : items){
                Item regItem = Registry.ITEM.get(ResourceLocation.tryParse(item.getAsString())).asItem();
                regItem.rarity = Rarity.create(name, s -> s.withColor(Integer.parseInt(color.replaceFirst("#", ""), 16)));
            }
            CustomRarities.LOGGER.info("Loaded rarity data for " + name);
        }
    }
}
