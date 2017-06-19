libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

addSbtPlugin("com.typesafe.sbt"    % "sbt-scalariform" % "1.3.0")
addSbtPlugin("com.github.tkawachi" % "sbt-lock"        % "0.2.3")
addSbtPlugin("com.timushev.sbt"    % "sbt-updates"     % "0.1.9")
addSbtPlugin("com.github.gseitz"   % "sbt-release"     % "1.0.0")
addSbtPlugin("org.xerial.sbt"      % "sbt-sonatype"    % "1.0")
addSbtPlugin("com.jsuereth"        % "sbt-pgp"         % "1.0.0")
addSbtPlugin("com.github.tkawachi" % "sbt-doctest"     % "0.4.1")
