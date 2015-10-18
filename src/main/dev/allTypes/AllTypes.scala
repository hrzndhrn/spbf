package allTypes

import protobuf.Types._

object ProtocolBuffer {
  private object MessageType extends Enumeration {
    type MessageType = Value
    val AllTypesType = Value
  }

  import java.io.File

  import MessageType._
  import protobuf.Decoding.{from, markFieldData, messageDataStream}
  import protobuf.FieldRule._


  private val messageTypeMap = Map(
    1 -> AllTypesType,
    2 -> AllTypesType,
    3 -> AllTypesType,
    4 -> AllTypesType,
    5 -> AllTypesType,
    6 -> AllTypesType,
    7 -> AllTypesType,
    8 -> AllTypesType,
    9 -> AllTypesType,
    10 -> AllTypesType,
    11 -> AllTypesType,
    12 -> AllTypesType,
    13 -> AllTypesType,
    14 -> AllTypesType
  )

  private val fieldRuleMap = Map(
    1 -> Required,
    2 -> Required,
    3 -> Required,
    4 -> Required,
    5 -> Required,
    6 -> Required,
    7 -> Required,
    8 -> Required,
    9 -> Required,
    10 -> Required,
    11 -> Required,
    12 -> Required,
    13 -> Required,
    14 -> Required
  )

  def decode(file: File): Stream[Any] = {
    instance( messageDataFrom(file))
  }

  private def instance( objs:Stream[(MessageType, Map[Int, List[Byte]])]): Stream[Any] = {
    objs map {
      case (AllTypesType, data) => AllTypes(data)
      case _ => None
    }
  }

  private def messageDataFrom( data:Data):Stream[(MessageType, Fields)] = {
    messageDataStream( from(data) map markFieldData(messageTypeMap), fieldRuleMap)
  }

  private def messageDataFrom( file:File):Stream[(MessageType, Fields)] = {
    messageDataStream( from(file) map markFieldData(messageTypeMap), fieldRuleMap)
  }

}

                                       // Tag
case class AllTypes( doubleVal:Double, // 1
                     floatVal: Float,  // 2
                     stringVal:String, // 3
                     sint32Val:Int,    // 4
                     uint32Val:Int,    // 5
                     int32Val:Int,     // 6
                     sint64Val:Long,   // 7
                     uint64Val:Long,   // 8
                     int64Val:Long,    // 9
                     boolVal:Boolean,  // 10
                     fixed64Val:Long,  // 11
                     sfixed64Val:Long, // 12
                     fixed32Val:Int,   // 13
                     sfixed32Val:Int   // 14
                     ) {

  override def toString:String = {
    f"""AllTypes(
      |   1: doubleVal = $doubleVal%s
      |   2: floatVal = $floatVal%s
      |   3: stringVal = $stringVal%s
      |   4: sint32Val = $sint32Val%s
      |   5: uint32Val = $uint32Val%s
      |   6: int32Val = $int32Val%s
      |   7: sint32Val = $sint32Val%s
      |   8: uint32Val = $uint32Val%s
      |   9: int64Val = $int64Val%s
      |   10: boolVal = $boolVal%s
      |   11: fixed64Val = $fixed64Val%s
      |   12: sfixed64Val = $sfixed64Val%s
      |   13: fixed32Val = $fixed32Val%s
      |   14: sfixed32Val = $sfixed32Val%s
      | )
    """.stripMargin
  }
}

object AllTypes {
  import protobuf.Convert._

  def apply(fields:Fields) = {
    println("SimpleMessage fields: " + fields)

    val doubleVal:Double = fields.get(1) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for doubleVal found!")
    }

    val floatVal:Float = fields.get(2) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for floatVal found!")
    }

    val stringVal:String = fields.get(3) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for stringVal found!")
    }

    val sint32Val:Sint32 = fields.get(4) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for sint32Val found!")
    }

    val uint32Val:Int = fields.get(5) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for uint32Val found!")
    }

    val int32Val:Int = fields.get(6) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for int32Val found!")
    }
    // ------
    // required sint64 sint64Val = 7
    val sint64Val:Sint64 = fields.get(7) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for sint64Val found!")
    }

    // required uint64 uint64Val = 8
    val uint64Val:Long = fields.get(8) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for uint64Val found!")
    }

    // required uint64 uint64Val = 9
    val int64Val:Int64 = fields.get(9) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for int64Val found!")
    }

    // required bool boolVal = 10
    val boolVal:Boolean = fields.get(10) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for boolVal found!")
    }

    // required fixed64 fixed64Val = 11
    val fixed64Val:Fixed64 = fields.get(11) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for fixed64Val found!")
    }

    // required sfixed64 sfixed64Val = 12
    val sfixed64Val:Sfixed64 = fields.get(12) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for sfixed64Val found!")
    }

    // required fixed32 fixed64Val = 13
    val fixed32Val:Fixed32 = fields.get(13) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for fixed32Val found!")
    }

    // required sfixed32 sfixed64Val = 14
    val sfixed32Val:Sfixed32= fields.get(14) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for sfixed32Val found!")
    }

    new AllTypes(doubleVal,floatVal,stringVal, sint32Val, uint32Val,
      int32Val, sint64Val, uint64Val, int64Val, boolVal, fixed64Val,
      sfixed64Val, fixed32Val, sfixed32Val)
  }

  def decode = ProtocolBuffer.decode _
}

