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
import better.files.Dsl._
import org.scalatest.BeforeAndAfterEach
/**
 *
 */
class BetterTempFilesSpec extends BetterFilesReferenceSpec with BeforeAndAfterEach {

  override val basePath: File = File.newTemporaryDirectory("better-temp-files-spec")
  override val filePath: File = basePath.createChild("test-file")

  override def setContent(s: String): Unit = filePath < s

  override protected def afterEach(): Unit = filePath.clear()
}
