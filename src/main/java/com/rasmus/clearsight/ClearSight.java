package com.rasmus.clearsight;

import com.rasmus.clearsight.config.ClearSightConfig;
import net.fabricmc.api.ModInitializer;

public class ClearSight implements ModInitializer {
    @Override
    public void onInitialize() {
        ClearSightConfig.register();
    }
}
