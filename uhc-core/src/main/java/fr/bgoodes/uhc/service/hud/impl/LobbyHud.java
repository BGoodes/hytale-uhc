package fr.bgoodes.uhc.service.hud.impl;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.service.team.Team;
import fr.bgoodes.uhc.util.UHCMessageUtil;
import fr.bgoodes.uhc.util.UHCTeamUtil;
import org.jetbrains.annotations.NotNull;

public class LobbyHud extends UHCHud {

    public LobbyHud(@NotNull PlayerRef playerRef) {
        super(playerRef, IGameState.HUD_KEY);
    }

    @Override
    protected void build(@NotNull UICommandBuilder ui) {
        ui.append("Hud/Lobby.ui");
    }

    @Override
    public void refresh() {
        UICommandBuilder ui = new UICommandBuilder();
        boolean visible = UHCTeamUtil.isChoiceOpen();

        ui.set("#LobbyRoot.Visible", visible);

        if (visible) {
            ui.set("#TeamLabel.TextSpans", teamMessage());
        }

        update(false, ui);
    }

    private Message teamMessage() {
        Team team = UHC.get().getTeamService().getPlayerTeam(getPlayerRef().getUuid());

        if (team == null) {
            return UHCMessageUtil.tr("uhc.team.hud.choose");
        }

        Message name = Message.empty().insert(team.getName());

        if (team.getColor() != null) {
            name.color(team.getColor());
        }

        return UHCMessageUtil.tr("uhc.team.hud.current")
                .param("team", name)
                .param("count", team.getMembers().size())
                .param("size", UHC.get().getGameConfig().teamSize.get());
    }
}
