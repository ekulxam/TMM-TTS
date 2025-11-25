package survivalblock.tmm_tts.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TMMTTSEnUsLangGenerator extends FabricLanguageProvider {

    public TMMTTSEnUsLangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("commands.tmm_tts.autospeak.set", "Autospeak is now %s");
        translationBuilder.add("commands.tmm_tts.autospeak.get", "Autospeak is currently %s");
    }
}
