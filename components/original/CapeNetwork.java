package local.legacypvp;

import java.util.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/** Common-side relay: the sender can change only their own cosmetic state. */
public final class CapeNetwork implements ModInitializer {
 public record Select(boolean enabled) implements CustomPayload {
  public static final Id<Select> ID=new Id<>(Identifier.of("legacy_pvp_assist","cape_select_v1"));
  public static final PacketCodec<RegistryByteBuf,Select> CODEC=PacketCodec.of((p,b)->b.writeBoolean(p.enabled),b->new Select(b.readBoolean()));
  public Id<Select> getId(){return ID;}
 }
 public record State(UUID player,boolean enabled) implements CustomPayload {
  public static final Id<State> ID=new Id<>(Identifier.of("legacy_pvp_assist","cape_state_v1"));
  public static final PacketCodec<RegistryByteBuf,State> CODEC=PacketCodec.of((p,b)->{b.writeUuid(p.player);b.writeBoolean(p.enabled);},b->new State(b.readUuid(),b.readBoolean()));
  public Id<State> getId(){return ID;}
 }
 private final Map<UUID,Boolean> states=new HashMap<>();
 private final Map<UUID,Integer> lastUpdate=new HashMap<>();
 @Override public void onInitialize(){
  PayloadTypeRegistry.playC2S().register(Select.ID,Select.CODEC);
  PayloadTypeRegistry.playS2C().register(State.ID,State.CODEC);
  ServerPlayNetworking.registerGlobalReceiver(Select.ID,(p,ctx)->{
   var sender=ctx.player();var id=sender.getUuid();int tick=ctx.server().getTicks();
   boolean first=!states.containsKey(id);
   // Official client sends at most once/second. Duplicates and abusive bursts are ignored.
   if(!first&&(states.get(id)==p.enabled()||tick-lastUpdate.getOrDefault(id,tick)<10))return;
   if(first)states.forEach((uuid,on)->send(sender,new State(uuid,on)));
   states.put(id,p.enabled());lastUpdate.put(id,tick);
   for(var viewer:ctx.server().getPlayerManager().getPlayerList())send(viewer,new State(id,p.enabled()));
  });
  ServerPlayConnectionEvents.DISCONNECT.register((h,s)->{
   UUID id=h.player.getUuid();states.remove(id);lastUpdate.remove(id);
   for(var viewer:s.getPlayerManager().getPlayerList())send(viewer,new State(id,false));
  });
  ServerLifecycleEvents.SERVER_STOPPED.register(s->{states.clear();lastUpdate.clear();});
 }
 private static void send(ServerPlayerEntity player,State state){
  if(ServerPlayNetworking.canSend(player,State.ID))ServerPlayNetworking.send(player,state);
 }
}
