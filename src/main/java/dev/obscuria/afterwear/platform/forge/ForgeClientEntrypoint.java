package dev.obscuria.afterwear.platform.forge;

//? forge {
import dev.obscuria.afterwear.AfterwearClient;
import dev.obscuria.afterwear.config.ConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public final class ForgeClientEntrypoint {

	public static void init() {
		AfterwearClient.INSTANCE.onInitializeClient();
		ModLoadingContext.get().registerExtensionPoint(
				ConfigScreenHandler.ConfigScreenFactory.class,
				ForgeClientEntrypoint::configScreenFactory);
	}

	private static ConfigScreenHandler.ConfigScreenFactory configScreenFactory() {
		return new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ConfigScreen.INSTANCE.create(parent));
	}
}
//?}
