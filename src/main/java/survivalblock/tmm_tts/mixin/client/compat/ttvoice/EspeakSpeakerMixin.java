package survivalblock.tmm_tts.mixin.client.compat.ttvoice;

import com.flooferland.espeak.Espeak;
import com.flooferland.ttvoice.speech.EspeakSpeaker;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.sun.jna.Pointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author afamiliarquiet
 */
@Mixin(EspeakSpeaker.class)
public class EspeakSpeakerMixin {
    @WrapOperation(method = "speak", at = @At(value = "INVOKE", target = "Lcom/flooferland/espeak/Espeak;synth$default(Lcom/flooferland/espeak/Espeak;Ljava/lang/String;Lcom/flooferland/espeak/Espeak$Position;I[ILcom/sun/jna/Pointer;ILjava/lang/Object;)Ljava/lang/Error;"))
    private Error withSSML(Espeak espeak, String text, Espeak.Position position, int flags, int[] id, Pointer pointer, int i, Object o, Operation<Error> original) {
        return espeak.synth(text, EspeakPositionInvoker.tmm_tts$invokeInit(Espeak.PositionType.Word, 0, 0), flags | Espeak.Flags.SSML.getB(), id, pointer);
    }
}
