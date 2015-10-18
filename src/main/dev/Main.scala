import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import proto.{ProtoBuf}

object Main {

  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  def main(args: Array[String]) = {
    logger.debug("=============================")
    println("### Main -###############################################")
    // EncoderRep.encode("/Users/kruse/Projects/java/jpbf/data/repMsg.pbf")
    EncoderAllTypes.encode("/Users/kruse/Projects/java/jpbf/data/allTypes.pbf")
  }
}

object EncoderRep {

  import java.io.File

  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  def encode(fileName:String) = {
    val file = new File( fileName)
    println( "read: " + file)
    ProtoBuf.mergeFrom( file) foreach { obj =>
      println("=== print object: " + obj.getClass.getName + " ===")
      println(obj)
    }
  }
}

object EncoderAllTypes {

  import java.io.File

  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  def encode(fileName:String) = {
    import allTypes.AllTypes
    val file = new File( fileName)
    println( "read: " + file)
    AllTypes.decode( file) foreach { obj =>
      println("=== print object: " + obj.getClass.getName + " ===")
      println(obj)
    }
  }
}


object Parser {
  import protobuf._

  def parse =  {
    val parser = new ProtoParser()


    val text =
      """package bazinga;
        |
        |message baz {
        |  optional int foo = 1;
        |  // laksdjfö
        |  optional int bar = 2;
        |}
      """.stripMargin

    /*
    val text =
      """optional int foo = 1;
        |  optional int bar = 2;
      """.stripMargin
    */

    // val text = "message baz { optional int foo = 1; optional int bar = 2;}"
    println("text:")
    println(text)
    println("<eof")
    println("parse (proto):")
    val result = parser.parse(parser.proto, text)
    result match {
      case parser.Success(matched, _) => println( "matched: " + matched)
      case parser.Failure(msg, _) => println("FAILURE: " + msg)
      case parser.Error(msg, _) => println("ERROR: " + msg)
    }
    println("=================")
    println(result.get)
  }
}