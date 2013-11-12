package com.vilt.minium.mobile;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.Duration;

public interface Configuration {
    
    public interface WaitingPreset {
        public WaitingPreset timeout(Duration timeout);
        public WaitingPreset interval(Duration interval);
        public WaitingPreset timeout(long time, TimeUnit unit);
        public WaitingPreset interval(long time, TimeUnit unit);
        public WaitingPreset reset();

        public Duration timeout();
        public Duration interval();
        
        public Configuration done();
    }

    /**
     * Gets the default timeout.
     *
     * @return the default timeout
     */
    public abstract Duration defaultTimeout();

    /**
     * Default timeout.
     *
     * @param defaultTimeout the default timeout
     * @return the configuration
     */
    public abstract Configuration defaultTimeout(Duration defaultTimeout);

    /**
     * Default timeout.
     *
     * @param time the time
     * @param unit the unit
     * @return the configuration
     */
    public abstract Configuration defaultTimeout(long time, TimeUnit unit);

    /**
     * Gets the default interval.
     *
     * @return the default interval
     */
    public abstract Duration defaultInterval();

    /**
     * Default interval.
     *
     * @param defaultInterval the default interval
     * @return the configuration
     */
    public abstract Configuration defaultInterval(Duration defaultInterval);

    /**
     * Default interval.
     *
     * @param time the time
     * @param unit the unit
     * @return the configuration
     */
    public abstract Configuration defaultInterval(long time, TimeUnit unit);

    public abstract WaitingPreset waitingPreset(String preset);
    
}