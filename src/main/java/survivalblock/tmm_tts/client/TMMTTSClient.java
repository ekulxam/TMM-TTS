package survivalblock.tmm_tts.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMMTTSClient implements ClientModInitializer, ClientCommandRegistrationCallback {
	public static final String MOD_ID = "tmm_tts";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final TMMTTSConfig CONFIG = TMMTTSConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", MOD_ID, TMMTTSConfig.class);

    public static boolean initializingAgain = false;

	@Override
	public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(this);
	}

    private static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    private static RequiredArgumentBuilder<FabricClientCommandSource, Boolean> booleanArgument(String name) {
        return RequiredArgumentBuilder.argument(name, BoolArgumentType.bool());
    }

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        CommandNode<FabricClientCommandSource> tmmtts = literal("tmm:tts").build();

        CommandNode<FabricClientCommandSource> toggleAutospeak = literal("autospeak")
                .then(
                        booleanArgument("maybespeak")
                                .executes(context -> {
                                    TMMTTSClient.CONFIG.autospeak = context.getArgument("maybespeak", Boolean.class);
                                    CONFIG.save();
                                    context.getSource().sendFeedback(Text.stringifiedTranslatable("commands.tmm_tts.autospeak.set", TMMTTSClient.CONFIG.autospeak));
                                    return 1;
                                })
                ).executes(context -> {
                    context.getSource().sendFeedback(Text.stringifiedTranslatable("commands.tmm_tts.autospeak.get", TMMTTSClient.CONFIG.autospeak));
                    return 1;
                }).build();

        dispatcher.getRoot().addChild(tmmtts);

        tmmtts.addChild(toggleAutospeak);
    }
}