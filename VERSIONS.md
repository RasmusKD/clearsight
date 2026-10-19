# 26.1 vs 26.2

One jar runs on both versions. The runtime uses mojmap names directly, so
any renamed member is a hard crash on the other version. Add new finds here.

| What | 26.1.x | 26.2 | Fix |
|---|---|---|---|
| HUD class | `client.gui.Gui` | `client.gui.Hud`, members identical | mixin pair + `ClearSightMixinPlugin` |
| Screen on `Minecraft` | field `screen` | method `screen()` | reflection by name (rare-fish-finder) |
| `Minecraft.setScreen` | exists | gone | use `setScreenAndShow`, exists in both |
| Entity type constants | on `EntityType` | moved to `EntityTypes` | `BuiltInRegistries.ENTITY_TYPE.getValue(id)` instead |
| `GuiGraphicsExtractor.entity` | `Vector3f`/`Quaternionf` | `Vector3fc`/`Quaternionfc` (other descriptor) | resolve by name + param count (rare-fish-finder) |
| Screen effect helpers | `renderScreenEffect`, `renderFire`, `renderWater`, `renderTex` | `submit`, `submitFire`, `submitWater`, `submitBlockSprite` | mixin pair + plugin |
| Rain splash + sound | `WeatherEffectRenderer.tickRainParticles` | `ClientLevel.tickWeatherEffects` | mixin pair + plugin |
| Gamerule ids | `doDaylightCycle`, `doWeatherCycle` | `advance_time`, `advance_weather` | server commands only |

Identical in both, but not where you'd expect:

- Totem burst: `ClientPacketListener.handleEntityEvent` via
  `ParticleEngine.createTrackingEmitter`. The lone `addParticle` in
  `LivingEntity.handleEntityEvent` is PORTAL particles.
- Leaf drips in rain: `LeavesBlock.makeDrippingWaterParticles`, separate
  from both the rain rendering and the ground splashes.
- Rain fog and the faded sun and moon: `AtmosphericFogEnvironment.updateRainFogState`
  (the `rainFogMultiplier` field) and `SkyRenderer.extractRenderState`
  (`SkyRenderState.rainBrightness`), same signatures in both.
- Weather's sky, fog, cloud, light and star attributes: `WeatherAttributes`
  reads rain and thunder through the anonymous `WeatherAttributes$WeatherAccess$1`
  (`rainLevel()`/`thunderLevel()` calling `Level.getRainLevel/getThunderLevel`),
  identical in both.
- F3+B arrows: `EntityHitboxDebugRenderer.showHitboxes` via `Gizmos.arrow`.
- Vanilla already has settings for vignette, darkness pulsing, FOV
  effects, clouds, menu blur and lightning flashes (Accessibility).
  Don't duplicate them.

Verifying: both mojmap jars sit in
`~/.gradle/caches/fabric-loom/minecraftMaven/net/minecraft/minecraft-merged-deobf/`.
Run `javap -p` against BOTH before referencing anything directly. Before a
release, audit every `net/minecraft` reference in the built jar the same
way. 2.5.2 crashed on 26.2 because only one class got audited.

## 26.3 is a separate jar

`./gradlew build` produces `clearsight-X.Y.Z+26.1.jar` (26.1.x and 26.2). `./gradlew build -PmcTarget=26.3`
produces `clearsight-X.Y.Z+26.3.jar` (pins in `gradle-26.3.properties`). Files under `src/mc26_3/java` and
`src/mc26_3/resources` replace the same-named file of `src/main`; `src/mc26_3/remove.txt` lists main
files the 26.3 jar drops. `verifyMixinClasses` (part of `check`) fails when a mixins.json entry has no
class in that variant. Check each jar against the Minecraft versions it claims without starting the game:
`python C:\Users\Rasmus\.claude\skills\minecraft-mods\scripts\linkcheck.py <jar> <mapped minecraft jar>`
(plugin-gated twins report as missing on the other version; that is expected).

| What | 26.1/26.2 | 26.3 | Fix |
|---|---|---|---|
| `InputConstants.Type.KEYSYM` | exists | now `KEYBOARD` | `new KeyMapping(String, int, Category)`, exists on all three |
| `GpuTexture` | `com.mojang.blaze3d.textures` | `com.mojang.renderpearl.api.textures` | `var` in `FrozenFrame.capture`, no import |
| `GameRenderer.displayItemActivation` | on GameRenderer | `LocalPlayer.displayItemActivation` | `LocalPlayerItemActivationMixin` + 26.3 `GameRendererMixin` in `src/mc26_3` |
| Portal warp read | `LocalPlayer.portalEffectIntensity`, `oPortalEffectIntensity` in `GameRenderer.tick/renderLevel` | `PlayerRenderState.portalEffectIntensity` in `renderLevel()` | 26.3 `GameRendererMixin` redirect |
| `ItemInHandRenderer` (shield height) | exists | replaced by `FirstPersonHandsAndItemsRenderer`, item drawn via `ItemStackRenderState.submit` | OPEN: `shieldHeight` does nothing on 26.3; the port needs a runtime test |
| Loading hooks (reload fade, level loading, title, reconfigure freeze) | on | plugin keeps them OFF on 26.3 (`knownLoadingVersion`) until a runtime test confirms the injection points | OPEN: runtime test, then add 26.3 to the plugin |

Full 26.3 table: `C:\Users\Rasmus\Desktop\Ny mappe (3)\mc-26.3-splits.md`.
