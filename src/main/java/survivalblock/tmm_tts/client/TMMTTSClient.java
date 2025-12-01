package survivalblock.tmm_tts.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import de.maxhenkel.voicechat.FabricVoicechatClientMod;
import de.maxhenkel.voicechat.FabricVoicechatMod;
import folk.sisby.kaleido.lib.quiltconfig.api.Config;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.api.ModInitializer;
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

        CommandNode<FabricClientCommandSource> triggerVoicechatReconnection = literal("reconnect")
                .executes(TMMTTSClient::reloadVoiceChat)
                .build();

        dispatcher.getRoot().addChild(tmmtts);

        tmmtts.addChild(toggleAutospeak);
        tmmtts.addChild(triggerVoicechatReconnection);
    }

    private static int reloadVoiceChat(CommandContext<FabricClientCommandSource> context) {
        FabricLoader floader = FabricLoader.getInstance();
        boolean mainSuccess = false;
        boolean clientSuccess = false;
        initializingAgain = true;

        try {
            floader.invokeEntrypoints("main", ModInitializer.class, modInitializer -> {
                if (modInitializer instanceof FabricVoicechatMod) {
                    modInitializer.onInitialize();
                }
            });
            mainSuccess = true;
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while executing FabricVoicechatMod#onInitialize!", throwable);
        }

        try {
            floader.invokeEntrypoints("client", ClientModInitializer.class, modInitializer -> {
                if (modInitializer instanceof FabricVoicechatClientMod) {
                    modInitializer.onInitializeClient();
                }
            });
            clientSuccess = true;
        } catch (Throwable throwable) {
            LOGGER.error("An error occurred while executing FabricVoicechatClientMod#onInitializeClient!", throwable);
        }

        initializingAgain = false;

        context.getSource().sendFeedback(Text.stringifiedTranslatable("commands.tmm_tts.reconnect.result", mainSuccess, clientSuccess));
        return mainSuccess || clientSuccess ? 1 : 0;
    }
}