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

package com.github.kxbmap.configs.util

import com.typesafe.config.{ConfigException, ConfigList, ConfigValue}
import java.{util => ju}
import scalaz.Need


trait IsMissing[A] {
  def check(a: Need[A]): Boolean
}

object IsMissing {

  def apply[A](implicit A: IsMissing[A]): IsMissing[A] = A

  implicit def defaultIsMissing[A]: IsMissing[A] = a =>
    intercept0(a.value) {
      case _: ConfigException.Missing => true
    }
}


trait IsWrongType[A] {
  def check(a: Need[A]): Boolean
}

object IsWrongType {

  def apply[A](implicit A: IsWrongType[A]): IsWrongType[A] = A

  implicit def defaultIsWrongType[A]: IsWrongType[A] = default.asInstanceOf[IsWrongType[A]]

  private[this] final val default: IsWrongType[Any] = a =>
    intercept0(a.value) {
      case _: ConfigException.WrongType => true
    }

  implicit val configValueIsWrongType: IsWrongType[ConfigValue] = a => {
    a.value
    true
  }

}


trait WrongTypeValue[A] {
  def value: Any
}

object WrongTypeValue {

  def apply[A](implicit A: WrongTypeValue[A]): WrongTypeValue[A] = A

  def string[A]: WrongTypeValue[A] = _string.asInstanceOf[WrongTypeValue[A]]

  private[this] final val _string: WrongTypeValue[Any] = new WrongTypeValue[Any] {
    val value: Any = "wrong type value"
  }

  def list[A]: WrongTypeValue[A] = _list.asInstanceOf[WrongTypeValue[A]]

  private[this] final val _list: WrongTypeValue[Any] = new WrongTypeValue[Any] {
    val value: Any = ju.Collections.emptyList()
  }

  implicit def defaultWrongTypeValue[A]: WrongTypeValue[A] = list[A]

  implicit val configListWrongTypeValue: WrongTypeValue[ConfigList] = string[ConfigList]

  implicit def javaListWrongTypeValue[A]: WrongTypeValue[ju.List[A]] = string[ju.List[A]]

  implicit def javaSetWrongTypeValue[A]: WrongTypeValue[ju.Set[A]] = string[ju.Set[A]]

  implicit def traversableWrongTypeValue[F[_] <: Traversable[_], A]: WrongTypeValue[F[A]] = string[F[A]]

  implicit def arrayWrongTypeValue[A]: WrongTypeValue[Array[A]] = string[Array[A]]

}
