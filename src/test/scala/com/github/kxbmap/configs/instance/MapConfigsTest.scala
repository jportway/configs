/*
 * Copyright 2013-2015 Tsukasa Kitachi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.kxbmap.configs.instance

import com.github.kxbmap.configs.util.ConfigVal
import com.github.kxbmap.configs.{ConfigProp, Configs}
import scalaprops.{Gen, Scalaprops}
import scalaz.Equal


object MapConfigsTest extends Scalaprops with ConfigProp {

  def checkMap[A: Configs : Gen : ConfigVal : Equal] =
    check[Map[String, A]]("string key").product(check[Map[Symbol, A]]("symbol key"))

  val map = checkMap[java.time.Duration]


  implicit lazy val symbolKeyGen = Gen[String].map(Symbol.apply)

  implicit def symbolMapGen[A: Gen]: Gen[Map[Symbol, A]] =
    Gen.mapGen[String, A].map(_.map(t => Symbol(t._1) -> t._2))

  implicit def symbolMapConfigVal[A: ConfigVal]: ConfigVal[Map[Symbol, A]] =
    ConfigVal[Map[String, A]].contramap(_.map(t => t._1.name -> t._2))

}