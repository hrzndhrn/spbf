// import com.sun.tools.javac.parser.Scanner
import protobuf.{Field, ProtoParser}

val parser = new ProtoParser()

val proto =
  """message RepMessage {
    |    repeated SimpleMessage msgs = 2;
    |}
    |
    |message SimpleMessage {
    |    required int32 value = 1;
    |    required int32 count = 1;
    |}
    |
  """.stripMargin

val msg =
  """message SimpleMessage {
    |  required int32 value = 1;
    |}
  """.stripMargin

val rule =
  """
    |required int32 value2 = 1;
  """.stripMargin

val result = parser.parseAll(parser.proto, proto)
