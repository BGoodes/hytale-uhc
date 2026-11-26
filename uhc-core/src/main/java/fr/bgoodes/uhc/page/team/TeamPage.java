package fr.bgoodes.uhc.page.team;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.event.team.TeamChangeEvent;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.service.team.TeamService;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCSoundUtil;
import fr.bgoodes.uhc.util.UHCTeamUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeamPage extends InteractiveCustomUIPage<TeamPage.Data> {

    private static final String DOCUMENT = "Team/TeamPage.ui";
    private static final String CARD = "Team/TeamCard.ui";
    private static final String SLOT = "Team/TeamSlot.ui";
    private static final Color NO_COLOR = new Color(0xC8D2DC);

    private static final String REFUSED_SOUND = "SFX_Bow_No_Ammo";

    private static final Set<TeamPage> OPEN_PAGES = ConcurrentHashMap.newKeySet();

    public TeamPage(@NotNull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, Data.CODEC);
    }

    public static void refreshAll() {
        OPEN_PAGES.forEach(TeamPage::refresh);
    }

    @Override
    public void build(
            @NotNull Ref<EntityStore> ref,
            @NotNull UICommandBuilder ui,
            @NotNull UIEventBuilder events,
            @NotNull Store<EntityStore> store
    ) {
        OPEN_PAGES.add(this);

        ui.append(DOCUMENT);
        buildTeams(ui, events);
    }

    @Override
    public void onDismiss(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store) {
        OPEN_PAGES.remove(this);
    }

    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull Data data) {
        if (!UHCTeamUtil.isChoiceOpen()) {
            refresh();
            return;
        }

        String teamId = "Join".equals(data.action) ? data.id : null;

        TeamChangeEvent event = HytaleServer.get().getEventBus()
                .dispatchFor(TeamChangeEvent.class)
                .dispatch(new TeamChangeEvent(playerRef.getUuid(), teamId));

        if (event.isCancelled()) {
            UHCSoundUtil.send(playerRef, REFUSED_SOUND);
            refresh();
        }
    }

    private void refresh() {
        if (!UHCTeamUtil.isChoiceOpen()) {
            closeOnWorld();
            return;
        }

        UICommandBuilder ui = new UICommandBuilder();
        UIEventBuilder events = new UIEventBuilder();

        buildTeams(ui, events);
        sendUpdate(ui, events, false);
    }

    private void closeOnWorld() {
        OPEN_PAGES.remove(this);
        Ref<EntityStore> ref = playerRef.getReference();

        if (ref == null || !ref.isValid()) {
            return;
        }

        ref.getStore().getExternalData().getWorld().execute(() -> {
            if (ref.isValid()) {
                close();
            }
        });
    }

    private void buildTeams(UICommandBuilder ui, UIEventBuilder events) {
        TeamService teamService = UHC.get().getTeamService();
        Team ownTeam = teamService.getPlayerTeam(playerRef.getUuid());
        int teamSize = UHC.get().getGameConfig().teamSize.get();
        int index = 0;

        ui.clear("#TeamGrid");

        for (Team team : teamService.getTeams()) {
            Color color = team.getColor() != null ? team.getColor() : NO_COLOR;
            String card = "#TeamGrid[" + index++ + "]";
            List<String> members = memberNames(team);
            boolean own = team == ownTeam;
            boolean full = members.size() >= teamSize;

            ui.append("#TeamGrid", CARD);
            ui.set(card + " #Name.TextSpans", Message.empty().insert(team.getName()).color(color));
            ui.set(card + " #Count.Text", members.size() + "/" + teamSize);

            for (int slot = 0; slot < teamSize; slot++) {
                ui.append(card + " #Slots", SLOT);

                if (slot < members.size()) {
                    String slotSelector = card + " #Slots[" + slot + "]";

                    ui.set(slotSelector + " #Dot.TextSpans", Message.raw("•").color(color));
                    ui.set(slotSelector + " #Name.TextSpans", Message.raw(members.get(slot)).color(color));
                }
            }

            ui.set(card + " #JoinButton.TextSpans", UHCMessageUtil.tr(own ? "uhc.page.team.leave" : full ? "uhc.page.team.full" : "uhc.page.team.join"));
            ui.set(card + " #JoinButton.Disabled", full && !own);

            events.addEventBinding(CustomUIEventBindingType.Activating, card + " #JoinButton",
                    new EventData().append("Action", own ? "Leave" : "Join").append("Id", team.getId()), false);
        }
    }

    private static List<String> memberNames(Team team) {
        return team.getMembers().stream()
                .map(TeamPage::username)
                .sorted(Comparator.comparing(String::toLowerCase))
                .toList();
    }

    private static String username(UUID uuid) {
        PlayerRef playerRef = Universe.get().getPlayer(uuid);
        return playerRef != null ? playerRef.getUsername() : "?";
    }

    public static class Data {

        public static final BuilderCodec<Data> CODEC = BuilderCodec.builder(Data.class, Data::new)
                .append(new KeyedCodec<>("Action", Codec.STRING, false),
                        (data, value) -> data.action = value,
                        (data) -> data.action).add()
                .append(new KeyedCodec<>("Id", Codec.STRING, false),
                        (data, value) -> data.id = value,
                        (data) -> data.id).add()
                .build();

        private @Nullable String action;
        private @Nullable String id;
    }
}
