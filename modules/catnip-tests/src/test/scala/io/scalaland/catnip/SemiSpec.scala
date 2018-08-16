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
  }
}
