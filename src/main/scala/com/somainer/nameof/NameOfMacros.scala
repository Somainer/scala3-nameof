package com.somainer.nameof

import scala.annotation.tailrec
import scala.quoted.*
import scala.reflect.NameTransformer

object NameOfMacros:
  def nameOfImpl(expr: Expr[Any])(using Quotes): Expr[String] = {
    import quotes.reflect.*
    
    @tailrec
    def extract(term: Term): String = term match
      case Inlined(_, _, inlined) => extract(inlined)
      case Ident(n) => NameTransformer.decode(n)
      case Select(_, n) => NameTransformer.decode(n)
      case Block(DefDef(_, _, _, Some(term)) :: Nil, _) => extract(term)
      case Block(_, expr) => extract(expr)
      case Apply(func, _) => extract(func)
      case Typed(expr, _) => extract(expr)
      case TypeApply(func, _) => extract(func)
      case _ => report.throwError(s"Can not know how to get name of ${expr.show}: ${term}")

    // report.info(s"${expr.show} => ${extract(expr.asTerm)}")
    Expr(extract(expr.asTerm))
  }

  private def typeSymbol[T](using q: Quotes)(using Type[T]): q.reflect.Symbol =
    import q.reflect.*
    TypeRepr.of[T].typeSymbol
  
  private def dealiasedTypeSymbol[T](using q: Quotes)(using Type[T]): q.reflect.Symbol =
    import q.reflect.*
    TypeRepr.of[T].dealias.typeSymbol
  
  def dealiasedNameOfTypeImpl[T](using Quotes, Type[T]): Expr[String] =
    import quotes.reflect.*
    def extract(term: TypeRepr): String =
      term match
        case TypeRef(_, tpe) => tpe
        case TermRef(t, tp) => tp
        case _ => dealiasedTypeSymbol.name

    Expr(extract(TypeRepr.of[T].dealias))

  def nameOfTypeImpl[T](using Quotes, Type[T]): Expr[String] =
    import quotes.reflect.*
    def extract(term: TypeRepr): String =
      term match
        case TypeRef(_, tpe) => tpe
        case TermRef(t, tp) => tp
        case _ => typeSymbol.name

    Expr(extract(TypeRepr.of[T]))
  
  def qualifiedNameOfTypeImpl[T](using Quotes, Type[T]): Expr[String] =
    Expr(dealiasedTypeSymbol.fullName)
