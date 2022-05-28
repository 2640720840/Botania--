![](web/img/logo.png)  
Welcome to the Botania repository.  

Botania is a [Minecraft](https://minecraft.net/) tech mod themed around natural magic. It's inspired by other magic mods, such as [Thaumcraft](https://www.curseforge.com/minecraft/mc-mods/thaumcraft) or [Blood Magic](https://www.curseforge.com/minecraft/mc-mods/blood-magic).  

The current iteration of Botania is made possible thanks to the massive help by the part of [williewillus](https://github.com/williewillus), who ported the mod from 1.8 through to the present day, so go buy him a beer or something, I dunno, he's pretty cool.

Botania is licensed under the [Botania License](http://botaniamod.net/license.php)

## Maven info

Maven artifacts are located [here](https://maven.blamejared.com/vazkii/botania/Botania/), each folder representing a version.

Note: As of 1.16, intermediate (non-release) Maven builds are no longer persisted.
That is, you must either depend on a *released* version of Botania, e.g. `1.16.2-407`, or specifically opt in to the bleeding-edge
build of the next version. For example, `1.16.2-408-SNAPSHOT` would be the current bleeding edge version of future version `1.16.2-408`. 

Note that `-SNAPSHOT` versions can be broken from time to time, and you are strongly discouraged from using them unless you are helping dogfood, test, or contribute to Botania. They may also be pruned from time to time to save disk space on the server. Do *not* rely on `-SNAPSHOT` versions for anything important!

In Forge, add the following to your `build.gradle`
```gradle
repositories {
    maven { url 'https://maven.blamejared.com' }
}

dependencies {
    // 1.14+
    compileOnly fg.deobf("vazkii.botania:Botania:[VERSION]:api")
    runtimeOnly fg.deobf("vazkii.botania:Botania:[VERSION]")
}
```
## Mixin Troubleshooting

Read this if you get crashes when depending on Botania and trying to launch in-dev.
Botania uses Mixins to implement various features.
This may cause issues when depending on Botania in-dev, since ForgeGradle/MixinGradle
do not yet properly support this in-dev like Fabric does.
As a workaround, disable refmaps by defining the `mixin.env.disableRefMap`
JVM argument to `true`.

## Making a Release
1. Pull from remote, test all changes, and commit everything.
2. `git tag -a release-<VERSION>`. All Botania versions *must* follow the version format `<MC-VER>-INT`, so it'll probably look like `git tag -a release-1.16.3-407`.
3. In the Git editor that pops up, write the changelog. Finish the tag process (usually by saving and closing the editor).
4. Copy the changelog to the webpage version under `web/changelog.txt`.`
5. Run `./gradlew incrementBuildNumber --no-daemon` to increment the build number of the next release. Commit this and the changelog.
6. Push: `git push origin master --tags`
7. Go to [Jenkins](https://ci.blamejared.com/job/Botania/view/tags/) and wait for the tag you just pushed to be compiled and built
8. Download the JAR and submit it to CurseForge
9. Push the website: `./syncweb.sh <remote username>`. If you don't provide a remote username to ssh into the webserver, it'll take your current login name.
