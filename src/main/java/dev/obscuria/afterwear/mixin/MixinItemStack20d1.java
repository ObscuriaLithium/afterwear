package dev.obscuria.afterwear.mixin;

//? 1.20.1 {
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import dev.obscuria.afterwear.common.helper.BreakageHelper;
import dev.obscuria.afterwear.common.helper.DegradationHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
@MixinEnvironment(type = MixinEnvironment.Env.DEFAULT)
public abstract class MixinItemStack20d1 {

	@Inject(method = "hurtAndBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z"), cancellable = true)
	private <T extends LivingEntity> void hurtAndBreak$override(
			int amount, T entity, Consumer<T> onBroken, CallbackInfo ci
	) {
		if (!BreakageHelper.INSTANCE.hurtAndBreak((ItemStack) (Object) this, entity, amount)) return;
		ci.cancel();
	}

	@ModifyReturnValue(method = "getAttributeModifiers", at = @At("RETURN"))
	private Multimap<Attribute, AttributeModifier> getAttributeModifiers$modifyReturnValue(
			Multimap<Attribute, AttributeModifier> original,
			@Local(argsOnly = true) EquipmentSlot slot
	) {
		return DegradationHelper.INSTANCE.degradeAttributes((ItemStack) (Object) this, slot, original);
	}
}
//?}
