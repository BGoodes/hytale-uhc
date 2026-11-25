package fr.bgoodes.uhc.service.hud.impl;

import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.flag.FlagService;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import fr.bgoodes.uhc.util.UHCTeamUtil;
import org.jetbrains.annotations.NotNull;

public class StatsHud extends UHCHud {

    public static final String HUD_ID = "uhc:stats";

    private static final String PLAYERS_ICON = "Icons/players.png";
    private static final String TEAMS_ICON = "Icons/teams.png";
    private static final String KILLS_ICON = "Icons/ItemCategories/Items-Weapons.png";

    public StatsHud(@NotNull PlayerRef playerRef) {
        super(playerRef, HUD_ID);
    }

    @Override
    protected void build(@NotNull UICommandBuilder ui) {
        ui.append("Hud/Stats.ui");
        ui.set("#PlayersIcon.AssetPath", PLAYERS_ICON);
        ui.set("#TeamsIcon.AssetPath", TEAMS_ICON);
        ui.set("#KillsIcon.AssetPath", KILLS_ICON);
    }

    @Override
    public void refresh() {
        UICommandBuilder ui = new UICommandBuilder();
        FlagService flagService = UHC.get().getFlagService();
        boolean visible = flagService.isEnabled(UHCFlag.HUD_STATS);

        ui.set("#StatsRoot.Visible", visible);

        if (visible) {
            ui.set("#PlayersCount.Text", String.valueOf(UHC.get().getPlayerService().getParticipants().size()));

            ui.set("#TeamsStat.Visible", !UHCTeamUtil.isSolo());
            ui.set("#TeamsCount.Text", String.valueOf(UHC.get().getTeamService().getAliveTeams().size()));

            ui.set("#KillsCount.Text", String.valueOf(UHC.get().getPlayerService().getKills(getPlayerRef().getUuid())));
        }

        update(false, ui);
    }
}
