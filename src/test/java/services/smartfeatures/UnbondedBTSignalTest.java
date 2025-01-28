package services.smartfeatures;

import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class UnbondedBTSignalImplTest {

    @Test
    void testBTbroadcastExecutesContinuously() throws Exception {
        UnbondedBTSignalImpl btSignal = new UnbondedBTSignalImpl("STATION123", 1);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                btSignal.BTbroadcast();
            } catch (ConnectException ignored) {
                // Ignorar excepcions per interrupcions
            }
        });

        TimeUnit.SECONDS.sleep(3); // Esperar prou temps per observar el comportament
        executor.shutdownNow(); // Aturar el fil que executa el bucle infinit
        executor.awaitTermination(1, TimeUnit.SECONDS);

        assertTrue(executor.isShutdown(), "El fil s'hauria d'haver aturat correctament.");
    }

    @Test
    void testBTbroadcastHandlesInterruption() {
        UnbondedBTSignalImpl btSignal = new UnbondedBTSignalImpl("STATION123", 1);

        Thread testThread = new Thread(() -> {
            try {
                btSignal.BTbroadcast();
            } catch (ConnectException e) {
                assertEquals("Emissió interrompuda.", e.getMessage(), "El missatge d'excepció hauria de coincidir.");
            }
        });

        testThread.start();
        testThread.interrupt(); // Provocar una interrupció

        try {
            testThread.join(); // Esperar que el fil acabi
        } catch (InterruptedException e) {
            fail("El test ha estat interromput.");
        }
    }
}
