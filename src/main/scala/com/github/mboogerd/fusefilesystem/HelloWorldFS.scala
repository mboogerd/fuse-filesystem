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

import java.util.Objects

import jnr.ffi.Pointer
import jnr.ffi.types.{ off_t, size_t }
import ru.serce.jnrfuse.struct.{ FileStat, FuseFileInfo }
import ru.serce.jnrfuse.{ ErrorCodes, FuseFillDir, FuseStubFS }

/**
 *
 */
class HelloWorldFS extends FuseStubFS {

  val HELLO_PATH = "/hello"
  val HELLO_STR = "Hello World!"

  override def getattr(path: String, stat: FileStat): Int = {
    var res = 0
    if (Objects.equals(path, "/")) {
      stat.st_mode.set(FileStat.S_IFDIR | 0x755)
      stat.st_nlink.set(2)
    } else if (HELLO_PATH.equals(path)) {
      stat.st_mode.set(FileStat.S_IFREG | 0x444)
      stat.st_nlink.set(1)
      stat.st_size.set(HELLO_STR.getBytes.length)
    } else res = -ErrorCodes.ENOENT
    res
  }

  override def readdir(path: String, buf: Pointer, filter: FuseFillDir, @off_t offset: Long, fi: FuseFileInfo): Int = {
    if (!("/" == path)) return -ErrorCodes.ENOENT
    filter.apply(buf, ".", null, 0)
    filter.apply(buf, "..", null, 0)
    filter.apply(buf, HELLO_PATH.substring(1), null, 0)
    0
  }

  override def open(path: String, fi: FuseFileInfo): Int = {
    if (!HELLO_PATH.equals(path)) return -ErrorCodes.ENOENT
    0
  }

  override def read(path: String, buf: Pointer, @size_t size: Long, @off_t offset: Long, fi: FuseFileInfo): Int = {
    if (!HELLO_PATH.equals(path)) return -ErrorCodes.ENOENT
    val bytes = HELLO_STR.getBytes
    val length = bytes.length
    val actualSize = (offset, length) match {
      case (o, l) if o < l ⇒ if (o + size > l) length - offset else size
      case (o, l) ⇒ 0
    }
    if (offset < length) buf.put(0, bytes, 0, bytes.length)
    actualSize.toInt
  }
}
