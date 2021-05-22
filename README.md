## `nameOf` macro for Scala3

[![](https://maven-badges.herokuapp.com/maven-central/com.somainer/scala3-nameof_3/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.somainer/scala3-nameof_3)
[![javadoc](https://javadoc.io/badge2/com.somainer/scala3-nameof_3/javadoc.svg)](https://javadoc.io/doc/com.somainer/scala3-nameof_3)

Get the name of a variable, function, class member, or type as a string at compile-time.
Just like the C# `nameof` operator.

## Usage
First, add this library as a `provided` dependency in your `Scala3` project, 
since `nameOf` is only needed during compilation and not at runtime.

```scala
libraryDependencies += "com.somainer" %% "scala3-nameof" % "0.0.1" % Provided
```

Then, import the methods in `NameOf` object.
```scala
import com.somainer.nameof.NameOf.*
```

Now you can use `nameOf` to get the name of a variable or class member:

```scala
case class Person(name: String, age: Int):
  def toMap = Map(
    nameOf(name) -> name,
    nameOf(age) -> age
  )
object Person:
  def logName(person: Person): Unit =
    println(s"${nameOf(person.name)}: ${person.name}")
```

Getting name of methods:

```scala
def computation(value: Int): Unit =
  println(s"Starting ${nameOf(computation)} with ${nameOf(value)}: $value")
```

Without having an instance of the type:
```scala
case class Person(name: String, age: Int) {
  def sayHello(other: Person) = s"Hello ${other.name}!"
}

assert(nameOf[Person](_.age) == "age")
assert(nameOf[Person](_.sayHello) == "sayHello")
assert(nameOf[Person](_.sayHello(???)) == "sayHello")
```

Handling extension methods and typeclasses:

```scala
extension(i: Int)
  def theMethod: Int = i
      
assert(nameOf[Int](_.theMethod) == "theMethod")

trait Default[T]:
  def defaultValue: T

object Default:
  def default[T](using d: Default[T]) = d.defaultValue

given Default[Int] with
  def defaultValue: Int = 1

import Default.default
assert(nameOf(default[Int]) == "default")
assert(nameOf(default) == "default")
assert(nameOf(Default.default) == "default")
assert(nameOf(summon[Default[Int]].defaultValue) == "defaultValue")
assert(nameOf[Default.type](_.default) == nameOf(default))
assert(nameOf(default(using ???)) == "default")
```

Even handing inline methods and values:

```scala
inline def idZero(inline x: Int): Int = x
inline val zero = 0

assert(nameOf(idZero(zero)) == "idZero")
assert(nameOf(zero) == "zero")
```

Getting name of enum classes:

```scala
enum Maybe[+A]:
  case Nothing
  case Just(v: A)

assert(nameOf(Maybe[?]) == "Maybe")
assert(nameOf(Maybe.Nothing) == "Nothing")
assert(nameOfType[Maybe.Nothing.type] == "Nothing")
assert(nameOfType[Maybe.Just[?]] == "Just")
```

Getting name of types:

## `qualifiedNameOf` will always dealias first.

```scala
type str = String

assert(nameOf[str] == "str")
assert(dealiasedNameOf[str] == nameOf[String])
// Since full name of type alias does not make sense, qualified name will always dealias first.
assert(qualifiedNameOfType[str] == "java.lang.String") 
```