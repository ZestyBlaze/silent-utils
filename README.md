# Silent Utils

Just some helpful utility classes. Mostly intended for Minecraft modding, but could be used for anything.

## Adding Dependency

Add the following to your `build.gradle` to add to your project.

You alse need to generate a GitHub token and add it along with your GitHub username to your personal `gradle.properties` file in `C:\Users\YOUR_USERNAME\.gradle` or `~/.gradle/gradle.properties`. This file may not exist, and you would have to create it yourself.

GitHub tokens can be generated [here](https://github.com/settings/tokens)!

Click _Generate New Token_ and click the checkmark for _read:packages_

Example of `gradle.properties` file in `C:\Users\YOUR_USERNAME\.gradle` or `~/.gradle/gradle.properties`

```gradle
//Your GitHub username
gpr.username=SilentChaos512

// Your GitHub generated token (bunch of hex digits) with read permission
gpr.token=paste_your_token_here
```

-----------------------------------

Code to add to `build.gradle`

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/silentchaos512/silent-utils")
        credentials {
            username = property('gpr.username')
            password = property('gpr.token')
        }
    }
}
```

```gradle
dependencies {
    // Replace VERSION_NUMBER with the version you need (such as 1.+)
    // Example: compile "net.silentchaos512:silent-utils:1.+"
    // Available builds can be found here: https://github.com/SilentChaos512/silent-utils/packages
    compile "net.silentchaos512:silent-utils:VERSION_NUMBER"
}
```
