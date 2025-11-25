package survivalblock.tmm_tts.mixin.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

/**
 * @see dev.doctor4t.trainmurdermystery.mixin.client.restrictions.KeyBindingMixin
 * Wow, you're really making me do this huh
 * I've never had to use Mixin^2 with expressions
 */
//@Debug(export = true)
@Mixin(value = KeyBinding.class, priority = 1500)
public class KeyBindingMixinMixin {

    @SuppressWarnings({"MixinAnnotationTarget", "InvalidInjectorMethodSignature"}) // this does run
    @TargetHandler(
            mixin = "dev.doctor4t.trainmurdermystery.mixin.client.restrictions.KeyBindingMixin",
            name = "shouldSuppressKey"
    )
	@WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;equals(Lnet/minecraft/client/option/KeyBinding;)Z"))
	private boolean allowChatAndCommand(KeyBinding one, KeyBinding two, Operation<Boolean> original) {
        GameOptions gameOptions = MinecraftClient.getInstance().options;
        if (tmm_tts$isEitherEqual(one, two, gameOptions.chatKey)) {
            return false;
        }
        if (tmm_tts$isEitherEqual(one, two, gameOptions.commandKey)) {
            return false;
        }
        return original.call(one, two);
	}

    @Unique
    private boolean tmm_tts$isEitherEqual(KeyBinding one, KeyBinding two, KeyBinding target) {
        return Objects.equals(one, target) || Objects.equals(two, target);
    }
}