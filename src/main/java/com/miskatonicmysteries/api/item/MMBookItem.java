package com.miskatonicmysteries.api.item;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.Knowledge;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.util.Constants;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class MMBookItem extends Item implements Affiliated {

	private final Identifier id;
	private final Affiliation affiliation;
	private final boolean special;

	public MMBookItem(Identifier id, Affiliation affiliation, boolean special, Settings settings) {
		super(settings);
		this.id = id;
		this.affiliation = affiliation;
		this.special = special;
	}

	public Book getBook() {
		return BookRegistry.INSTANCE.books.get(id);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		Book book = getBook();
		if (player instanceof ServerPlayerEntity) {
			Knowledge.of(player).ifPresent(Knowledge::syncKnowledge);
			if (special && !InsanityHandler.hasSanityCapExpansion(player, Constants.Misc.NECRONOMICON_EXTENSION)) {
				Sanity.of(player).ifPresent(sanity -> sanity.addSanityCapExpansion(Constants.Misc.NECRONOMICON_EXTENSION, -10));

			}
			PatchouliAPI.get().openBookGUI((ServerPlayerEntity) player, book.id);
			SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.BOOK_OPEN);
			player.playSound(sfx, 1.0F, (float) (0.7D + Math.random() * 0.4D));
		}
		return TypedActionResult.success(player.getStackInHand(hand));
	}

	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		super.appendTooltip(stack, worldIn, tooltip, flagIn);
		Book book = getBook();
		if (book != null && book.getContents() != null) {
			tooltip.add(book.getSubtitle().formatted(Formatting.GRAY));
		}
		if (stack.getTag() != null && stack.getTag().contains(Constants.NBT.KNOWLEDGE)) {
			tooltip.add(new TranslatableText("knowledge.contains").formatted(Formatting.GRAY, Formatting.ITALIC));
			stack.getTag().getList(Constants.NBT.KNOWLEDGE, 8).forEach(s -> tooltip.add(new TranslatableText("knowledge." + s.asString())));
		}
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return affiliation;
	}

	@Override
	public boolean isSupernatural() {
		return special;
	}
}
