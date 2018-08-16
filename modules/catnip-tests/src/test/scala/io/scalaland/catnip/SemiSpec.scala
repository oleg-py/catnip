package io.scalaland.catnip

import cats.implicits._
import org.specs2.mutable.Specification

class SemiSpec extends Specification {

  "@Semi" should {

    "handle non-parametric classes" in {
      // given
      @Semi(cats.Eq) final case class Test(a: String, b: String)

      // when
      val result1 = cats.Eq[Test].eqv(Test("a", "b"), Test("a", "b"))
      val result2 = cats.Eq[Test].eqv(Test("a", "b"), Test("c", "d"))

      // then
      result1 must beTrue
      result2 must beFalse
    }

    "handle parametric classes" in {
      // given
      @Semi(cats.Eq) final case class Test[A](a: A)

      // when
      val result1 = cats.Eq[Test[String]].eqv(Test("a"), Test("a"))
      val result2 = cats.Eq[Test[String]].eqv(Test("a"), Test("b"))

      // then
      result1 must beTrue
      result2 must beFalse
    }

    "handle type aliases" in {
      // given
      @Semi(Aliased.X) final case class Test(a: String, b: String)

      // when
      val result1 = cats.Eq[Test].eqv(Test("a", "b"), Test("a", "b"))
      val result2 = cats.Eq[Test].eqv(Test("a", "b"), Test("c", "d"))

      // then
      result1 must beTrue
      result2 must beFalse
    }

    /*
    // TODO: local aliases fail type checker
    "handle type aliases" in {
      // given
      type X[A] = cats.Eq[A]; val X = cats.Eq
      @Semi(X) final case class Test(a: String, b: String)

      // when
      val result1 = cats.Eq[Test].eqv(Test("a", "b"), Test("a", "b"))
      val result2 = cats.Eq[Test].eqv(Test("a", "b"), Test("c", "d"))

      // then
      result1 must beTrue
      result2 must beFalse
    }
     */

    /*
     // TODO: renamed imports fail type checker
    "handle imports" in {
      // given
      import cats.{ Eq => X }
      @Semi(X) final case class Test(a: String, b: String)

      // when
      val result1 = X[Test].eqv(Test("a", "b"), Test("a", "b"))
      val result2 = X[Test].eqv(Test("a", "b"), Test("c", "d"))

      // then
      result1 must beTrue
      result2 must beFalse
    }
     */

    "generate for cats.Eq" in {
      // given
      @Semi(cats.Eq) final case class Test(a: String)

      // when
      val result1 = cats.Eq[Test].eqv(Test("a"), Test("a"))
      val result2 = cats.Eq[Test].eqv(Test("a"), Test("b"))

      // then
      result1 must beTrue
      result2 must beFalse
    }

    "generate for cats.PartialOrder" in {
      // given
      @Semi(cats.PartialOrder) final case class Test(a: String)

      // when
      val result1 = Test("a") <= Test("a")
      val result2 = Test("a") >= Test("b")

      // then
      result1 must beTrue
      result2 must beFalse
    }

    "generate for cats.Order" in {
      // given
      @Semi(cats.Order) final case class Test(a: String)

      // when
      val result1 = Test("a") min Test("a")
      val result2 = Test("a") max Test("b")

      // then
      result1 must beEqualTo(Test("a"))
      result2 must beEqualTo(Test("b"))
    }

    "generate for cats.Hash" in {
      // given
      @Semi(cats.Hash) final case class Test(a: String)

      // when
      val result1 = cats.Hash.hash(Test("a"))
      val result2 = cats.Hash.hash(Test("b"))

      // then
      result1 must not(beEqualTo(result2))
    }

    "generate for cats.Show" in {
      // given
      @Semi(cats.Show) final case class Test(a: String)

      // when
      val result1 = Test("a").show
      val result2 = Test("b").show

      // then
      result1 must beEqualTo("Test(a = a)")
      result2 must beEqualTo("Test(a = b)")
    }

    "generate for cats.Monoid" in {
      // given
      @Semi(cats.Monoid, cats.Eq) final case class Test(a: String)

      // when
      val result1 = Test("").isEmpty
      val result2 = Test("b").isEmpty

      // then
      result1 must beTrue
      result2 must beFalse
    }

    "generate for cats.Semigroup" in {
      // given
      @Semi(cats.Semigroup) final case class Test(a: String)

      // when
      val result1 = Test("a") |+| Test("a")
      val result2 = Test("b") |+| Test("a")

      // then
      result1 must beEqualTo(Test("aa"))
      result2 must beEqualTo(Test("ba"))
    }

    "generate for alleycats.Empty" in {
      // given
      @Semi(alleycats.Empty) final case class Test(a: String)

      // when

      val result = alleycats.Empty[Test].empty

      // then
      result must beEqualTo(Test(""))
    }
  }
}