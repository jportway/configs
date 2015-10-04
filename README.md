configs
=======
[![Build Status](https://travis-ci.org/kxbmap/configs.svg?branch=master)](https://travis-ci.org/kxbmap/configs)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.kxbmap/configs_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.kxbmap/configs_2.11)
[![Scaladoc](http://javadoc-badge.appspot.com/com.github.kxbmap/configs_2.11.svg?label=scaladoc)](http://javadoc-badge.appspot.com/com.github.kxbmap/configs_2.11)
[![License](https://img.shields.io/github/license/kxbmap/configs.svg)](https://github.com/kxbmap/configs/blob/master/LICENSE)

A Scala wrapper for Typesafe config


Usage
-----
### build.sbt
```scala
libraryDependencies += "com.github.kxbmap" %% "configs" % "0.2.5"
```

### Imports
```scala
import com.github.kxbmap.configs._
```

Examples
--------
```scala
import com.typesafe.config.ConfigFactory

val config = ConfigFactory.load()
```

#### Standard values
```scala
val a = config.get[Int]("a")          // == config.getInt("a")
val b = config.get[String]("b")       // == config.getString("b")
val c = config.get[List[Double]]("c") // Returns scala.List[Double], NOT java.util.List[java.lang.Double]
```

#### Bytes
Use `com.github.kxbmap.configs.Bytes`
```scala
val Bytes(bytes) = config.get[Bytes]("bs") // bytes == config.getBytes("bs").longValue()
```

#### Map
```scala
val m = config.get[Map[String, Int]]("m")
```

#### Duration
```scala
import scala.concurrent.duration.Duration
val d = config.get[Duration]("d")
```

#### Option
```scala
val s = config.get[Option[String]]("string")  // == Some("something")
val t = config.opt[String]("string")          // alias
```

By default, `get[Option[T]]` handles only `ConfigException.Missing`
```scala
// read missing value
val m = config.opt[String]("missing")  // == None

// read wrong type value
val n = config.opt[Int]("string")  // Exception! throws ConfigException.WrongType
```

Import implicit `CatchCond` (alias for `Throwable => Boolean`) value, change this behavior
```scala
import CatchCond.configException

// read wrong type value
val n = config.opt[Int]("string")  // == None
```

#### Either
```scala
val r = config.get[Either[Throwable, String]]("string")  // == Right("something")
val l = config.get[Either[Throwable, Int]]("string")     // == Left(ConfigException.WrongType(...))
```

#### Try
```scala
val s = config.get[Try[String]]("string")  // == Success("something")
val f = config.get[Try[Int]]("string")     // == Failure(ConfigException.WrongType(...))
```
