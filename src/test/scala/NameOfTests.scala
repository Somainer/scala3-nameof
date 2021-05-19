import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.matchers.should.Matchers

import com.somainer.nameof.NameOf.*

trait Default[T]:
  def defaultValue: T

object Default:
  def default[T](using d: Default[T]) = d.defaultValue

given Default[Int] with
  def defaultValue: Int = 1

enum Maybe[+A]:
  case Nothing
  case Just(value: A)

class NameOfTests extends AsyncWordSpec, Matchers:
  "plain names" should {
    "hold type names" in {
      nameOfType[String] shouldBe "String"
      nameOf[String](_.substring(???)) shouldBe "substring"
      nameOf(Int.MaxValue) shouldBe "MaxValue"

      def testMethod(): Unit =
        nameOf(testMethod()) shouldBe "testMethod"
      
      testMethod()
      def nilArgFunction: Unit = ???
      nameOf(nilArgFunction) shouldBe "nilArgFunction"
    }

    "hold consetituve names" in {
      nameOf(java.lang.Byte.MIN_VALUE) shouldBe "MIN_VALUE"
      nameOfType[java.lang.Byte] shouldBe "Byte"
      nameOf(scala.Byte) shouldBe "Byte"
    }

    "hold qualified names" in {
      import java.util.ArrayList
      qualifiedNameOfType[java.lang.String] shouldBe classOf[String].getCanonicalName
      qualifiedNameOfType[ArrayList[?]] shouldBe "java.util.ArrayList"
    }

    "hold type alias" in {
      type str = String
      nameOfType[str] shouldBe "str"
      dealiasedNameOfType[str] shouldBe nameOfType[String]
    }

    "hold this" in {
      new:
        private def suchMethod = ???
        nameOf(suchMethod) shouldBe nameOf(this.suchMethod)
      
      succeed
    }

    "hold curried functions" in {
      def curriedFunc(x: Int)(y: Int)(z: Int): Int = x + y + z
      nameOf(curriedFunc) shouldBe "curriedFunc"
      nameOf(curriedFunc(_)) shouldBe "curriedFunc"
    }

    "hold generic functions" in {
      def id[T](x: T): T = x
      nameOf(id) shouldBe "id"
    }

    "hold case ADTs" in {
      nameOf(Maybe) shouldBe "Maybe"
      nameOf(Maybe.Just) shouldBe "Just"
      nameOf(Maybe.Nothing) shouldBe "Nothing"
      nameOfType[Maybe.Just[?]] shouldBe "Just"
      nameOfType[Maybe.Nothing.type] shouldBe "Nothing"
      type N = Maybe.Nothing.type
      dealiasedNameOfType[N] shouldBe "Nothing"
    }

    "hold case classes" in {
      case class ++(p: Int)
      nameOfType[++] shouldBe "++"
      nameOf(++) shouldBe "++"
      nameOf[++](_.p) shouldBe "p"
    }
  }

  "implicit names" should {
    "handle extension method" in {
      extension(i: Int)
        def theMethod: Int = i
      
      nameOf[Int](_.theMethod) shouldBe "theMethod"
    }

    "handle given instances" in {
      import Default.default
      nameOf(default[Int]) shouldBe "default"
      nameOf(summon[Default[Int]].defaultValue) shouldBe "defaultValue"
      nameOf[Default.type](_.default) shouldBe nameOf(default)
      nameOf(default(using ???)) shouldBe "default"
    }
  }
