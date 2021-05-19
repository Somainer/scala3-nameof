package com.somainer.nameof

import NameOfMacros.*

object NameOf:
  /**
   * Obtain an identifier name as a constant string.
   *
   * @example
   * {{{
   *   val amount = 5
   *   nameOf(amount) => "amount"
   * }}}
   */
  inline def nameOf(inline expr: Any): String = ${ nameOfImpl('expr) }

  /**
   * Obtain an identifier name as a constant string.
   *
   * This overload can be used to access an instance method without having an instance of the type.
   *
   * @example
   * {{{
   *   class Person(val name: String)
   *   nameOf[Person](_.name) => "name"
   * }}}
   */
  inline def nameOf[T](inline navigation: T => Any): String = ${ nameOfImpl('navigation) }

  /**
   * Obtain a type's unqualified name as a constant string.
   *
   * @example
   * {{{
   *   type str = String
   *   nameOfType[str] = "str"
   *   nameOfType[java.lang.String] => "String"
   *   nameOfType[fully.qualified.ClassName] => "ClassName"
   * }}}
   */
  inline def nameOfType[T]: String = ${ nameOfTypeImpl[T] }

  /**
   * Obtain a type's real dealiased unqualified name as a constant string.
   *
   * @example
   * {{{
   *   type str = String
   *   nameOfType[str] = "String"
   *   nameOfType[java.lang.String] => "String"
   *   nameOfType[fully.qualified.ClassName] => "ClassName"
   * }}}
   */
  inline def dealiasedNameOfType[T]: String = ${ dealiasedNameOfTypeImpl[T] }

  /**
   * Obtain a type's qualified name as a constant string.
   *
   * @example
   * {{{
   *   nameOfType[java.lang.String] => "java.lang.String"
   *   nameOfType[fully.qualified.ClassName] => "fully.qualified.ClassName"
   * }}}
   */
  inline def qualifiedNameOfType[T]: String = ${ qualifiedNameOfTypeImpl[T] }
