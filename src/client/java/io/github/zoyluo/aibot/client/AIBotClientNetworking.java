package io.github.zoyluo.aibot.client;

import io.github.zoyluo.aibot.network.payload.BotChatS2C;
import io.github.zoyluo.aibot.network.payload.BotReviveS2C;
import io.github.zoyluo.aibot.network.payload.BotSnapshotS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public final class AIBotClientNetworking {
    private AIBotClientNetworking() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(BotSnapshotS2C.ID, (payload, context) ->
                context.client().execute(() -> BotClientState.INSTANCE.setSnapshot(payload)));
        // 死亡动画复位：服务端复活了 bot，但客户端只收到过"死亡"广播，它自己的 deathTime 停在最大值 → 躺尸抽搐。这里把血量与计时都归零。
        ClientPlayNetworking.registerGlobalReceiver(BotReviveS2C.ID, (payload, context) ->
                context.client().execute(() -> {
                    if (context.client().world == null) {
                        return;
                    }
                    Entity entity = context.client().world.getEntityById(payload.entityId());
                    if (entity instanceof LivingEntity living) {
                        living.setHealth(payload.health());
                        living.deathTime = 0;
                        living.hurtTime = 0;
                        living.timeUntilRegen = 0;
                    }
                }));
        ClientPlayNetworking.registerGlobalReceiver(BotChatS2C.ID, (payload, context) ->
                context.client().execute(() -> {
                    if (BotClientState.INSTANCE.matchesTarget(payload.botName())) {
                        BotClientState.INSTANCE.addTranscript(payload.role(), payload.text());
                    }
                }));
    }
}
