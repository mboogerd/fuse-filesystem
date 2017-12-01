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
import org.scalatest.BeforeAndAfterAll

/**
 *
 */
class BetterFuseSpec extends BetterFilesReferenceSpec with BeforeAndAfterAll {

  override val basePath: File = File.newTemporaryDirectory("better-fuse-spec")
  override def filePath: File = basePath / "filename"

  val memoryFS = new MemoryFileFS("/filename")

  override def setContent(s: String): Unit = memoryFS.setContent(s)

  override protected def beforeAll(): Unit = {
    memoryFS.mount(basePath.path)
  }

  override protected def afterAll(): Unit = {
    memoryFS.umount()
    basePath.deleteOnExit()
  }
}
