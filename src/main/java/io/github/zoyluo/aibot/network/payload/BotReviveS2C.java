package io.github.zoyluo.aibot.network.payload;

import io.github.zoyluo.aibot.AIBotMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * 服务端 -> 客户端：这只 bot 已经"原地复活"，请把客户端那份的血量与死亡动画复位。
 *
 * 为什么需要它：AIPlayerEntity 是 ServerPlayerEntity，玩家血量不走数据追踪器。
 * 死亡时客户端只收到 EntityStatusS2CPacket(3)，会把自己的 deathTime 顶到最大值；
 * 服务端复活时只改了本地副本，客户端收不到任何"血量恢复"的包，
 * 于是 deathTime 一直停在最大值 —— 渲染上就是躺在地上抽搐。
 */
public record BotReviveS2C(int entityId, float health) implements CustomPayload {
    public static final Id<BotReviveS2C> ID = new Id<>(Identifier.of(AIBotMod.MOD_ID, "bot_revive"));
    public static final PacketCodec<RegistryByteBuf, BotReviveS2C> CODEC = PacketCodec.of(BotReviveS2C::write, BotReviveS2C::new);

    private BotReviveS2C(RegistryByteBuf buf) {
        this(buf.readVarInt(), buf.readFloat());
    }

    private void write(RegistryByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeFloat(health);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
