/*
 * Copyright 2017 Merlijn Boogerd
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

package com.github.mboogerd.fusefilesystem

import better.files._
import org.scalatest.{ FlatSpecLike, Matchers }

import scala.util.Random

/**
 *
 */
abstract class BetterFilesReferenceSpec extends FlatSpecLike with Matchers {

  def basePath: File
  def filePath: File
  def setContent(s: String): Unit

  it should "allow reading a file in once" in withContent { content ⇒
    filePath.contentAsString shouldBe content
  }

  it should "allow reading a file buffered" in withContent { content ⇒
    filePath.bufferedReader.map(_.readLine()) shouldBe content
  }

  it should "allow reading a file randomly" in withContent { content ⇒
    val bytes = Array.fill[Byte](content.length - 2)(0)
    val randomAccess = filePath.randomAccess()
    randomAccess.foreach { f ⇒
      f.skipBytes(1)
      f.read(bytes)
    }
    bytes.map(_.toChar).mkString shouldBe content.tail.init
  }

  it should "allow zip-reader" in withContent { content ⇒
    filePath.zip().unzip().contentAsString shouldBe content
  }

  def withContent(test: String ⇒ Any): Unit = {
    val content = Random.alphanumeric.take(10).mkString
    println(content) // FIXME
    setContent(content)
    test(content)
  }

  //  it should "allow file creation and existence checking" in {
  //    (filePath / "touched").touch()
  //    (filePath / "touched").exists shouldBe true
  //  }
  //
  //  it should "allow basic file reads/writes" in {
  //    filePath / "write-test" < "somedata"
  //    (filePath / "write-test").contentAsString shouldBe "somedata"
  //  }
  //
  //  it should "allow writing and clearing files" in {
  //    filePath / "write-test" < "somedata"
  //    (filePath / "write-test").clear()
  //    (filePath / "write-test").contentAsString shouldBe 'empty
  //  }
  //
  //  it should "allow moving files" in {
  //    (filePath / "source").touch()
  //    (filePath / "source").moveTo(filePath / "target")
  //    (filePath / "target").exists shouldBe true
  //  }
  //
  //  it should "allow copying files" in {
  //    filePath / "source" < "somedata"
  //    (filePath / "source").copyTo(filePath / "target")
  //    filePath / "source" === filePath / "target" shouldBe true
  //  }
}
