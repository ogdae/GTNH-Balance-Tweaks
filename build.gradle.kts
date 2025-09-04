plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

project.ext.set("modVersion", "2.0.2")

dependencies {
    // GregTech API (dev jar)
    compileOnly("com.github.GTNewHorizons:GT5-Unofficial:5.09.45.74:dev")
}

base {
    archivesName.set("GTNHBalanceTweaks")
}

version = "2.0.2"
group = "com.rilliko"
