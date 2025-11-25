package survivalblock.tmm_tts.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.tmm_tts.client.TMMTTSClient;

//@Debug(export = true)
@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @ModifyExpressionValue(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;normalize(Ljava/lang/String;)Ljava/lang/String;"))
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
