package proto

import java.io.File
import protobuf.Decoding.{from,markFieldData,messageDataStream}
import protobuf.FieldRule
import protobuf.Types._

object ProtoBuf {
  object MessageType extends Enumeration {
    type MessageType = Value
    val RepMessageType = Value
    val SimpleMessageType = Value
  }

  import FieldRule._
  import MessageType._

  val messageTypeMap = Map(
    1 -> SimpleMessageType,
    2 -> RepMessageType
  )

  val fieldRuleMap = Map(
    1 -> Required,
    2 -> Repeated
  )


  def mergeFrom(file: File): Stream[Any] = {
    instance( messageDataFrom(file))
  }

  def messageDataFrom( data:Data):Stream[(MessageType, Map[Int, List[Byte]])] = {
    messageDataStream( from(data) map markFieldData(messageTypeMap), fieldRuleMap)
  }

  def messageDataFrom( file:File):Stream[(MessageType, Map[Int, List[Byte]])] = {
    messageDataStream( from(file) map markFieldData(messageTypeMap), fieldRuleMap)
  }

  def instance( objs:Stream[(MessageType, Map[Int, List[Byte]])]): Stream[Any] = {
    objs map {
      case (SimpleMessageType, data) => SimpleMessage(data)
      case (RepMessageType, data) => RepMessage(data)
      case _ => None
    }
  }
}

case class SimpleMessage(id: Int) {
  override def toString: String = {
    "SimpleMessage\n- id = " + id + "\n"
  }
}

object SimpleMessage {
  import protobuf.Convert._
  import ProtoBuf._
  import ProtoBuf.MessageType._

  def apply(fields:Fields) = {
    println("SimpleMessage fields: " + fields)

    val id:Int = fields.get(1) match {
      case Some(data:Data) => data
      case None => throw new Exception("No value for a found!")
    }

    new SimpleMessage(id)
  }

  def mergeFrom(data:Data):Stream[SimpleMessage] = {
    messageDataFrom(data) map {
      case (SimpleMessageType, data: Map[Int, List[Byte]]) => SimpleMessage(data)
      case _ => throw new Exception("Wrong type!")
    }
  }
}

case class RepMessage(msgs: List[SimpleMessage]) {
  override def toString: String = {
    "RepMessage\n" + ( msgs map { msg => msg.toString})
  }
}

object RepMessage {

  def apply(fields:Fields) = {
    println(" RepMessage fields: " + fields)

    val msgs:List[SimpleMessage] = fields.get(2) match {
      case Some(data:Data) => SimpleMessage.mergeFrom(data).toList
      case None => List()
    }


    new RepMessage(msgs)
  }
}


