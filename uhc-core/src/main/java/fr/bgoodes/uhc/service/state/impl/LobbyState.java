package fr.bgoodes.uhc.service.state.impl;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import fr.bgoodes.uhc.UHC;
import fr.bgoodes.uhc.service.hud.impl.LobbyHud;
import fr.bgoodes.uhc.service.hud.model.UHCHud;
import fr.bgoodes.uhc.service.flag.impl.UHCFlag;
import fr.bgoodes.uhc.service.state.model.IGameState;
import fr.bgoodes.uhc.util.UHCPlayerUtil;
import fr.bgoodes.uhc.util.UHCTeamUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LobbyState implements IGameState {

    public static final String STATE_ID = "lobby";

    @Override
    public void start() {
        UHC.get().getFlagService().setEnabled(UHCFlag.NEW_PLAYERS, true);
        UHC.get().getFlagService().setEnabled(UHCFlag.CONFIG, true);

        UHCTeamUtil.resetTeams();
    }

    @Override
    public @Nullable UHCHud createHud(@NotNull PlayerRef playerRef) {
        return new LobbyHud(playerRef);
    }

    @Override
    public void end() {
        UHC.get().getFlagService().setEnabled(UHCFlag.CONFIG, false);
        Universe.get().getPlayers().forEach(UHCPlayerUtil::closeInterfaces);
    }
}
