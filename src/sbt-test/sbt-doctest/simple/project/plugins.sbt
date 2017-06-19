{
  val pluginVersion = System.getProperty("plugin.version")
  if(pluginVersion == null)
    throw new RuntimeException(
      s"""|The system property 'plugin.version' is not defined.
          |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
  else
    addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % pluginVersion)
}
