package protobuf

import java.nio.{ByteOrder, ByteBuffer}

import protobuf.Types._

import scala.language.implicitConversions

object Convert {

  case class Sint32(value: Int)

  case class Sint64(value: Long)

  case class Int64(value: Long)

  case class Fixed32(value: Int)

  case class Sfixed32(value: Int)

  case class Fixed64(value: Long)

  case class Sfixed64(value: Long)

  implicit def dataToInt(data: Data): Int = {
    println("dataToInt: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    data.foldLeft(0) { (int, byte) =>
      (int << 7) | (byte & 0x7F)
    }
  }

  implicit def dataToLong(data: Data): Long = {
    println("dataToLong: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    data.foldLeft(0) { (int, byte) =>
      (int << 7) | (byte & 0x7F)
    }
  }

  // sfixed32
  implicit def dataToSfixed32(data: Data): Sfixed32 = {
    println("dataToSfixed32: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    val sfixed32: Int = ByteBuffer.wrap(data.toArray).order(ByteOrder.LITTLE_ENDIAN).getInt
    Sfixed32(sfixed32)
  }

  implicit def sfixed32ToInt(sfixed32: Sfixed32): Int = sfixed32.value

  // fixed32
  implicit def dataToFixed32(data: Data): Fixed32 = {
    println("dataToFixed32: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    val fixed32: Int = ByteBuffer.wrap(data.toArray).order(ByteOrder.LITTLE_ENDIAN).getInt
    Fixed32(fixed32)
  }

  implicit def fixed32ToInt(fixed32: Fixed32): Int = fixed32.value

  // sfixed64
  implicit def dataToSfixed64(data: Data): Sfixed64 = {
    println("dataToSfixed64: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    val sfixed64: Long = ByteBuffer.wrap(data.toArray).order(ByteOrder.LITTLE_ENDIAN).getLong
    Sfixed64(sfixed64)
  }

  implicit def sfixed64ToInt(sfixed64: Sfixed64): Long = sfixed64.value

  // fixed64
  implicit def dataToFixed64(data: Data): Fixed64 = {
    println("dataToFixed64: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    val fixed64: Long = ByteBuffer.wrap(data.toArray).order(ByteOrder.LITTLE_ENDIAN).getLong
    Fixed64(fixed64)
  }

  implicit def fixed64ToInt(fixed64: Fixed64): Long = fixed64.value

  // sint64
  implicit def dataToSint64(data: Data): Sint64 = {
    println("dataToSint64: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    val sint64: Long = data
    // TODO: (sint32 << 1) ^ (sint32 >> 31) // andere richtung
    Sint64(((~sint64 + 1) >> 1) * (((sint64 & 1) << 1) - 1))
  }

  implicit def sint64ToInt(sint64: Sint64): Long = sint64.value

  // int64
  implicit def dataToInt64(data: Data): Int64 = {
    println("dataToInt64: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    val int64: Long = data
    // TODO: (sint32 << 1) ^ (sint32 >> 31) // andere richtung
    Int64(((~int64 + 1) >> 1) * (((int64 & 1) << 1) - 1))
  }

  implicit def int64ToInt(int64: Int64): Long = int64.value


  implicit def dataToSint32(data: Data): Sint32 = {
    println("dataToSint32: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    val sint32: Int = data
    // (sint32 << 1) ^ (sint32 >> 31) // andere richtung
    Sint32(((~sint32 + 1) >> 1) * (((sint32 & 1) << 1) - 1))
  }

  implicit def sint32ToInt(sint32: Sint32): Int = sint32.value

  implicit def dataToString(data: Data): String = {
    println("dataToString: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    new String(data.toArray)
  }

  implicit def dataToDouble(data: Data): Double = {
    println("dataToDobule: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    ByteBuffer.wrap(data.toArray).order(ByteOrder.LITTLE_ENDIAN).getDouble
  }

  implicit def dataToFloat(data: Data): Float = {
    println("dataToFloat: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    ByteBuffer.wrap(data.toArray).order(ByteOrder.LITTLE_ENDIAN).getFloat
  }

  implicit def dataToBoolean(data: Data): Boolean = {
    println("dataToBoolean: data = " + data.map({ b: Byte => "0x%02x".format(b) }).mkString(", "))
    data.head != 0
  }

}
