## `nameOf` macro for Scala3

Get the name of a variable, function, class member, or type as a string at compile-time.
Just like the C# `nameof` operator.

First, import the `nameOf` method.
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