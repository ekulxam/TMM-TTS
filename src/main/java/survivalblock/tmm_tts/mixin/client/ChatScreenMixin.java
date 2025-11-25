package survivalblock.tmm_tts.mixin.client;

import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.tmm_tts.client.TMMTTSClient;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @ModifyVariable(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;normalize(Ljava/lang/String;)Ljava/lang/String;", shift = At.Shift.AFTER), argsOnly = true)
    private String autospeak(String value) {
        if (value.startsWith("/")) {
            return value;
        }

        if (!TMMTTSClient.autospeak) {
            return value;
        }

        return "/speak " + value;
    }
}
