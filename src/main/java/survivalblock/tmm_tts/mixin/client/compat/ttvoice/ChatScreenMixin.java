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

        if (value.endsWith("</prosody></voice>")) {
            value = value.substring(0, value.length() - "</prosody></voice>".length());
            // giant regex my beloved -iri
//            value = value.replaceFirst("<voice gender=\"[a-z]+\" variant=\"[0-9]+\"><prosody rate=\"[+-][0-9.]+%\" pitch=\"[+-][0-9.]+%\" range=\"[+-][0-9.]+%\">", "");
            value = value.substring(value.indexOf('>')+1); // destroy <voice>
            value = value.substring(value.indexOf('>')+1); // destroy <prosody>
        }

        return "/speak <voice gender=\"%s\" variant=\"%d\"><prosody rate=\"%s\" pitch=\"%s\" range=\"%s\">"
                .formatted(CONFIG.synthesizedGender, CONFIG.synthesizedGenderVariant, CONFIG.relativizedSpeechRate(), CONFIG.relativizedPitch(), CONFIG.relativizedPitchRange())
                + value + "</prosody></voice>";
    }
}
