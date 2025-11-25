package survivalblock.tmm_tts.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import de.maxhenkel.voicechat.Voicechat;
import de.maxhenkel.voicechat.net.NetManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.tmm_tts.client.TMMTTSClient;

@Environment(EnvType.CLIENT)
@Mixin(value = Voicechat.class, remap = false)
public class VoicechatMixin {

    @WrapWithCondition(method = "initialize", at = @At(value = "INVOKE", target = "Lde/maxhenkel/voicechat/net/NetManager;init()V"))
    private boolean stopNetManagerOnSecondInit(NetManager instance) {
        return !TMMTTSClient.initializingAgain;
    }
}
