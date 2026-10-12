# Clear Sight

Client-only Fabric mod that removes view-blocking effects (fog, overlays, particles, loading fades). Every feature is a plain Cloth Config toggle. Mod id `clearsight`, GitHub RasmusKD/clearsight, branch main.

Read the `minecraft-mods` skill first (`C:\Users\Rasmus\.claude\skills\minecraft-mods\SKILL.md`). It holds the cross-mod rules: dual-version builds, release flow, CurseForge/Modrinth, testing in instances, config and icon style. Memory note for this mod: `clearsight-mod.md`.

## Build

- Java 25: `JAVA_HOME=C:\Users\Rasmus\.jdks\jdk-25.0.3+9`, then `./gradlew build`. The jar is written to `build/libs/`.
- No unit tests; the check is a clean build plus a run in a Modrinth App instance (see the skill).
- One jar runs on 26.1.x and 26.2. Also compile against 26.2 before claiming support: `./gradlew build -Pminecraft_version=26.2 -Pfabric_version=0.158.0+26.2`. Renamed vanilla members are listed in `VERSIONS.md`; add new finds there.
- Mixin pairs for renamed classes are selected by `ClearSightMixinPlugin`; hooks that cannot apply on an unknown MC version switch themselves off.

## Release

No workflow. Publish by hand through the Modrinth API (recipe in the memory note, token kept outside the repo), then `git push`. Modrinth project `clear-sight`; beware the unrelated foreign project with slug `clearsight`. Do not publish or tag unless Rasmus asks. Modrinth policy 6.2 applies (see the skill).

## Keep skills and docs current (same rule as Nordvec)

When you learn something new, update the doc the next agent reads in the same change: this file for build and release facts, `README.md` for user-facing behavior, `VERSIONS.md` for 26.1/26.2 splits, and the `minecraft-mods` skill for cross-mod lessons. Correct or delete outdated text instead of adding a contradicting note. Then run `python C:\Users\Rasmus\.claude\skills\minecraft-mods\scripts\check_citations.py`.

No em or en dashes in any text. Commit messages describe the code change.
