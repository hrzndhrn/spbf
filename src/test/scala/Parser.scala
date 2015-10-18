import org.scalatest._
import protobuf.{Field, ProtoParser}

class TestParser extends FlatSpec with Matchers {

  "Parser" should "matching 'tag' (42)" in {
    val parser = new ProtoParser()
    val text = "42"
    val result = parser.parse(parser.tag, text)
    result.isEmpty should be(false)
    result.get should be(42)
  }

  "Parser" should "matching 'name' (foo)" in {
    val parser = new ProtoParser()
    val text = "foo"
    val result = parser.parse(parser.name, text)
    result.isEmpty should be(false)
    result.get should be("foo")
  }

  "Parser" should "matching 'name' (foo_bar)" in {
    val parser = new ProtoParser()
    val text = "foo_bar"
    val result = parser.parse(parser.name, text)
    result.isEmpty should be(false)
    result.get should be("foo_bar")
  }

  "Parser" should "matching 'pbfType' (string)" in {
    val parser = new ProtoParser()
    val text = "string"
    val result = parser.parse(parser.pbfType, text)
    result.isEmpty should be(false)
    result.get should be("string")
  }

  "Parser" should "matching 'pbfType' (int)" in {
    val parser = new ProtoParser()
    val text = "int"
    val result = parser.parse(parser.pbfType, text)
    result.isEmpty should be(false)
    result.get should be("int")
  }

  "Parser" should "not matching 'undef' as 'pbfType'" in {
    val parser = new ProtoParser()
    val text = "undef"
    val result = parser.parse(parser.pbfType, text)
    result.isEmpty should be(true)
  }

  "Parser" should "matching 'rule' (required)" in {
    val parser = new ProtoParser()
    val text = "required"
    val result = parser.parse(parser.rule, text)
    result.isEmpty should be(false)
    result.get should be("required")
  }

  "Parser" should "not matching 'undef' as 'rule'" in {
    val parser = new ProtoParser()
    val text = "undef"
    val result = parser.parse(parser.rule, text)
    result.isEmpty should be(true)
  }

  "Parser" should "matching 'field' (optional int xyz = 2;)" in {
    val parser = new ProtoParser()
    val text = "optional int xyz = 2;"
    val result = parser.parse(parser.field, text)
    result.isEmpty should be(false)
    result.get should equal (Field("optional", "int", "xyz", 2))
  }

  "Parser" should "matching 'fields'" in {
    val parser = new ProtoParser()
    val text =
      """optional int foo = 1;
        |optional int bar = 2;
      """.stripMargin
    val result = parser.parse(parser.fields, text)
    result.isEmpty should be(false)
    result.get should equal (List(
      Field("optional", "int", "foo", 1),
      Field("optional", "int", "bar", 2)))
  }

}