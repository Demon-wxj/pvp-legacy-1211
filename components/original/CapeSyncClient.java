package local.legacypvp;

import java.util.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class CapeSyncClient {
 private static final Set<UUID> enabled=new HashSet<>();
 private static Boolean sent;
 private static int cooldown;
 private CapeSyncClient(){}
 public static boolean enabled(UUID player){return enabled.contains(player);}
 private static void clear(){enabled.clear();sent=null;cooldown=0;}
 public static void init(){
  ClientPlayNetworking.registerGlobalReceiver(CapeNetwork.State.ID,(state,ctx)->{
   if(state.enabled())enabled.add(state.player());else enabled.remove(state.player());
  });
  ClientPlayConnectionEvents.JOIN.register((h,s,c)->clear());
  ClientPlayConnectionEvents.DISCONNECT.register((h,c)->clear());
  ClientTickEvents.END_CLIENT_TICK.register(c->{
   if(c.player==null||!ClientPlayNetworking.canSend(CapeNetwork.Select.ID))return;
   if(cooldown>0){cooldown--;return;}
   boolean value=LegacyPvp.config.saoCape;
   if(sent==null||sent!=value){ClientPlayNetworking.send(new CapeNetwork.Select(value));sent=value;cooldown=20;}
  });
 }
}
