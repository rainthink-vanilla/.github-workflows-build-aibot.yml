package io.github.zoyluo.aibot.network.payload;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class AIPayloads {
    private static boolean registered;

    private AIPayloads() {
    }

    public static synchronized void register() {
        if (registered) {
            return;
        }
        registered = true;
        PayloadTypeRegistry.playC2S().register(SubscribeBotC2S.ID, SubscribeBotC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(BotCommandC2S.ID, BotCommandC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(SetOptionC2S.ID, SetOptionC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(BotItemMoveC2S.ID, BotItemMoveC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(BotTeleportC2S.ID, BotTeleportC2S.CODEC);
        PayloadTypeRegistry.playS2C().register(BotSnapshotS2C.ID, BotSnapshotS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(BotChatS2C.ID, BotChatS2C.CODEC);
        // 死亡动画复位：复活后告诉客户端把血量与 deathTime 归零
        PayloadTypeRegistry.playS2C().register(BotReviveS2C.ID, BotReviveS2C.CODEC);
    }
}
