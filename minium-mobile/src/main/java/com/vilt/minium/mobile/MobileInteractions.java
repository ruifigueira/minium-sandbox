package com.vilt.minium.mobile;


public class MobileInteractions {

    public static MobileInterationPerformer withWaitingPreset(String preset) {
        return new MobileInterationPerformer(preset);
    }

    public static void click(DefaultMobileElements webElements) {
        defaultPerformer().click(webElements);
    }


    public static void fill(DefaultMobileElements webElements, String text) {
        defaultPerformer().fill(webElements, text);
    }

    private static MobileInterationPerformer defaultPerformer() {
        return new MobileInterationPerformer(null);
    }
}
