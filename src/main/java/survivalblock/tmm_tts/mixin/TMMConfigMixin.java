package survivalblock.tmm_tts.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import dev.doctor4t.trainmurdermystery.TMMConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TMMConfig.class)
public class TMMConfigMixin {

    @WrapOperation(method = "writeChanges", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;options:Lnet/minecraft/client/option/GameOptions;", opcode = Opcodes.GETFIELD))
    private GameOptions nullableOptions(MinecraftClient instance, Operation<GameOptions> original, @Cancellable CallbackInfo ci) {
        GameOptions options = original.call(instance);
        if (options == null) {
            ci.cancel();
        }
        return options;
    }
}
