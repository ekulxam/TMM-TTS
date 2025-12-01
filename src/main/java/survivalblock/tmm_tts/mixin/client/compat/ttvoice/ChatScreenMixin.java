package survivalblock.tmm_tts.mixin.client.compat.ttvoice;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static survivalblock.tmm_tts.client.TMMTTSClient.CONFIG;

//@Debug(export = true)
@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @ModifyExpressionValue(method = "sendMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;normalize(Ljava/lang/String;)Ljava/lang/String;"))
    private String autospeak(String value) {
        boolean speaking = CONFIG.autospeak && !value.startsWith("/");

        if (value.startsWith("/speak ")) {
            value = value.substring("/speak ".length());
            speaking = true;
        }

        if (!speaking) {
            return value;
        }

        return ("/speak <voice gender=\"%s\" variant=\"%d\"><prosody rate=\"%s\" pitch=\"%s\" range=\"%s\">" + value + "</prosody></voice>")
                .formatted(CONFIG.synthesizedGender, CONFIG.synthesizedGenderVariant, CONFIG.relativizedSpeechRate(), CONFIG.relativizedPitch(), CONFIG.relativizedPitchRange());
    }
}
