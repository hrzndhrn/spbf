package protobuf

import java.io.{File, FileInputStream}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import protobuf.FieldRule._
import protobuf.Types.Data
import protobuf.WireType._
import scala.collection.immutable.Stream.Empty
import scala.reflect.ClassTag


object Decoding {
  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  
  def from(file: File): Stream[FieldData] = {
    fieldDataStream(byteStream(new FileInputStream(file)))
  }

  def from(data: Data): Stream[FieldData] = {
    fieldDataStream(data.toStream)
  }

  /**
   * Converts a FileInputStream into a stream of bytes.
   * @param fileIn
   * @return
   */
  def byteStream(fileIn: FileInputStream): Stream[Byte] = {
    if (fileIn.available > 0) {
      val byte: Byte = fileIn.read.toByte
      logger.debug("file read: %02x".format(byte))
      byte #:: byteStream(fileIn)
    }
    else {
      fileIn.close()
      Stream.empty
    }
  }

  def getByteList(byteStream: Stream[Byte]): (List[Byte], Stream[Byte]) = {
    def getByteList(byteStream: Stream[Byte], byteList: List[Byte]): (List[Byte], Stream[Byte]) = {
      byteStream match {
        case byte #:: bytes if (byte & 0x80) == 0x80 => getByteList(bytes, byte :: byteList)
        case byte #:: bytes => (byte :: byteList, bytes)
        case Empty => (byteList, Stream.empty)
      }
    }

    getByteList(byteStream, List())
  }

  def getByteList(byteStream: Stream[Byte], n: Int): (List[Byte], Stream[Byte]) = {
    byteStream.splitAt(n) match {
      case (a, b) => (a.toList, b)
    }
  }

  def getVarint(byteStream: Stream[Byte]): (Int, Stream[Byte]) = {
    val (bytesList, byteTail) = getByteList(byteStream)
    val value = bytesList.foldLeft(0) { (number, byte) => (number << 7) | (byte & 0x7F) }
    (value, byteTail)
  }

  def varintStream(byteStream: Stream[Byte]): Stream[Int] = {
    if (byteStream.isEmpty) Stream.empty
    else {
      val (varint, tail) = getVarint(byteStream)
      varint #:: varintStream(tail)
    }
  }

  def getData(byteIn: Stream[Byte], wireType: WireType): (Data, Stream[Byte]) = {
    wireType match {
      case LengthDelimited =>
        val (length, byteTail) = getVarint(byteIn)
        getByteList(byteTail, length)
      case Bit32 =>
        getByteList(byteIn, 4)
      case Bit64 =>
        getByteList(byteIn, 8)
      case _ =>
        getByteList(byteIn)
    }
  }

  def getFieldData(byteIn: Stream[Byte]): (FieldData, Stream[Byte]) = {
    val (tagVarint, byteTail) = getVarint(byteIn)

    val tag = tagVarint >> 3
    val wireType = WireType(tagVarint & 0x7)

    val (data, dataTail) = getData(byteTail, wireType)

    (FieldData(tag, data), dataTail)
  }

  def fieldDataStream(byteStream: Stream[Byte]): Stream[FieldData] = {
    if (byteStream.isEmpty) Stream.empty
    else {
      val (wireObject, byteTail) = getFieldData(byteStream)
      wireObject #:: fieldDataStream(byteTail)
    }
  }

  def markFieldData[T](messageTypeMap: Map[Int, T])(fieldData: FieldData): (Option[T], FieldData) = {
    (messageTypeMap.get(fieldData.tag), fieldData)
  }

  def getMessageData[T](markedFieldData: Stream[(Option[T], FieldData)],
                        fieldRuleMap: Map[Int, FieldRule]): Option[(T, Map[Int, List[Byte]], Int)] = {
    def collect(markedFieldData: Stream[(Option[T], FieldData)],
                currentMessageType: T,
                map: Map[Int, List[Byte]],
                count: Int): (T, Map[Int, List[Byte]], Int) = {

      def repeatable(messageType: T, tagNumber: Int) =
        messageType == currentMessageType && fieldRuleMap(tagNumber) == Repeated

      def collectable(messageType: T, tagNumber: Int) =
        messageType == currentMessageType && !map.isDefinedAt(tagNumber)

      markedFieldData match {
        case (Some(messageType), wireObject) #:: rest if repeatable(messageType, wireObject.tag) =>
          val updatedMap = map + (wireObject.tag -> (map.getOrElse(wireObject.tag, List()) ++ wireObject.data))
          collect(rest, messageType, updatedMap, count + 1)
        case (Some(messageType), wireObject) #:: rest if collectable(messageType, wireObject.tag) =>
          val updatedMap = map + (wireObject.tag -> wireObject.data)
          collect(rest, messageType, updatedMap, count + 1)
        case _ =>
          (currentMessageType, map, count)
      }
    }

    // Set the seed.
    markedFieldData match {
      case (Some(messageType), fieldData) #:: rest =>
        Some(collect(rest, messageType, Map(fieldData.tag -> fieldData.data), 1))
      case _ =>
        None
    }
  }

  def messageDataStream[T: ClassTag](markedFieldData: Stream[(Option[T], FieldData)],
                                     fieldRuleMap: Map[Int, FieldRule]): Stream[(T, Map[Int, List[Byte]])] = {
    getMessageData(markedFieldData, fieldRuleMap) match {
      case Some((messageType: T, map, count)) => (messageType, map) #:: messageDataStream(markedFieldData.drop(count),
        fieldRuleMap)
      case _ => Stream.empty
    }
  }

}


