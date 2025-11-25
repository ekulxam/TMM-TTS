package survivalblock.tmm_tts.mixin.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Debug(export = true)
@Mixin(value = KeyBinding.class, priority = 1500)
public class KeyBindingMixinMixin {

    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"}) // this does run
    @TargetHandler(
            mixin = "dev.doctor4t.trainmurdermystery.mixin.client.restrictions.KeyBindingMixin",
            name = "shouldSuppressKey"
    )
	@WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;equals(Lnet/minecraft/client/option/KeyBinding;)Z"))
	private boolean allowChatAndCommand(KeyBinding one, KeyBinding two, Operation<Boolean> original) {
        KeyBinding chat = MinecraftClient.getInstance().options.chatKey;
        KeyBinding command = MinecraftClient.getInstance().options.commandKey;
        if (Objects.equals(two, chat) || Objects.equals(two, command)) {
            return false;
        }
        return original.call(one, two);
	}
}