/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aod.smartmeterdevice;

import edu.coloado.aos.config.DefaultConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sawhneya
 */
public class Simulator {
    public interface Callback {
        void handle(Reading reading);
    }

    private boolean dispatchReading = false;
    private Callback callback = null;

    public void enable() {
        this.dispatchReading = true;
    }

    public void disable() {
        this.dispatchReading = false;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        final ReadingGenerator generator = new ReadingGenerator();
        final ReadingDispatcher dispatcher = new ReadingDispatcher(
                DefaultConfig.HOST_NAME,
                DefaultConfig.PORT);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (dispatchReading) {
                        try {
                            Reading reading = generator.generate();
                            dispatcher.dispatch(reading);
                            callback.handle(reading);
                            Thread.sleep(DefaultConfig.SLEEP);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }.start();
    }
}
