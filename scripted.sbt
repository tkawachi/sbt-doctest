ScriptedPlugin.scriptedSettings

scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
  a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)
)

scriptedLaunchOpts += ("-Dplugin.version=" + version.value)

scriptedBufferLog := false
