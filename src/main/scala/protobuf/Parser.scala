package protobuf

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.lexical.Scanners

class ProtoParser extends RegexParsers {
  override protected val whiteSpace = """(\s|\/\/.*)+""".r

  def tag: Parser[Int] = """[1-9][0-9]*""".r ^^ { _.toInt }

  def name: Parser[String] = """[a-zA-Z][a-zA-Z0-9_]+""".r ^^ { _.toString }

  def pbfType: Parser[String] = """[a-zA-Z][a-zA-Z0-9_]+""".r ^^ { _.toString }

  def rule: Parser[String] = """(required|optional|repeated)""".r ^^ { _.toString }

  def field: Parser[Field] = rule~pbfType~name~"="~tag~";" ^^ {
    case rule~pbfType~name~_~tag~_ => Field( rule, pbfType, name, tag)
  }

  def fields: Parser[List[Field]] = rep(field)

  def protoPackage: Parser[Any] = "package" ~ name ~ ";" ^^ {
    case _~name~_ => name
  }

  def message: Parser[Message] = "message" ~ name ~ "{" ~ fields ~ "}" ^^ {
    case _~name~_~fields~_ => Message( name, fields)
  }

  def comment: Parser[String] = """^\s*//.*$""".r ^^ { _.toString }

  def protoExpr: Parser[Any] = message | protoPackage

  // def proto: Parser[Any] = repsep( protoExpr, "")
  def proto: Parser[Any] = rep( protoExpr)
}

case class Field( val rule:String, val pbfType:String, val name:String, val tag:Int)

case class Message( val name:String, val fields:List[Field])