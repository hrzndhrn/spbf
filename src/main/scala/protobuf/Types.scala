package protobuf

import protobuf.Types._

/**
 * Created by kruse on 11.04.15.
 * TODO: comments!
 */
object Types {
  type Fields = Map[Int,List[Byte]]
  type Data = List[Byte]
}

object FieldRule extends  Enumeration {
  type FieldRule = Value
  val Unknown = Value
  val Required = Value
  val Optional = Value
  val Repeated = Value
}

object WireType extends Enumeration {
  type WireType = Value
  val Varint = Value(0)
  val Bit64 = Value(1)
  val LengthDelimited = Value(2)
  val Bit32 = Value(5)
}

/**
 * Representation of a tag with his data.
 * @param tag
 * @param data
 */
case class FieldData(tag: Int, data: Data)
