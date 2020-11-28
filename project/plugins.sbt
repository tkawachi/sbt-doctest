libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value

addSbtPlugin("org.scalariform"     % "sbt-scalariform" % "1.8.2")
addSbtPlugin("com.timushev.sbt"    % "sbt-updates"     % "0.5.1")
addSbtPlugin("com.github.gseitz"   % "sbt-release"     % "1.0.13")
addSbtPlugin("org.xerial.sbt"      % "sbt-sonatype"    % "2.6")
addSbtPlugin("com.jsuereth"        % "sbt-pgp"         % "2.0.1")
addSbtPlugin("com.github.tkawachi" % "sbt-doctest"     % "0.9.8")
