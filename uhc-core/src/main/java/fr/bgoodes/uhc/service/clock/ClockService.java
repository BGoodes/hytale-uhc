package fr.bgoodes.uhc.service.clock;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.task.TaskRegistration;
import fr.bgoodes.uhc.UHC;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClockService {

    public ClockService() {
        ScheduledFuture<?> task = HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(this::dispatchClockEvent, 1, 1, TimeUnit.SECONDS);
        UHC.get().getTaskRegistry().register(new TaskRegistration(task));
    }

    private void dispatchClockEvent() {
        try {
            HytaleServer.get().getEventBus().dispatchFor(ClockEvent.class).dispatch(new ClockEvent());
        } catch (Exception e) {
            UHC.LOGGER.atSevere().withCause(e).log("Failed to dispatch ClockEvent.");
        }
    }
}
