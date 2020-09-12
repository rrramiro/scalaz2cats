
import scalafix.v1._
import scala.meta._

package object fix {
  def printTerm(t: Tree)(implicit doc: SemanticDocument): Unit = {
    println(t.syntax)
    println(t.symbol)
    println(t.getClass.getName)
  }

  def printMethodParameters(symbol: Symbol)(implicit doc: Symtab): Unit = {
    symbol.info.get.signature match {
      case signature @ MethodSignature(typeParameters, parameterLists, _) =>
        if (typeParameters.nonEmpty) {
          println("typeParameters")
          println(typeParameters.mkString("  ", "\n  ", ""))
        }
        parameterLists.foreach { parameterList =>
          println("parametersList")
          println(parameterList.mkString("  ", "\n  ", ""))
        }
    }
  }

  def printReturnType(symbol: Symbol)(implicit doc: Symtab): Unit = {
    symbol.info.get.signature match {
      case signature @ MethodSignature(_, _, returnType) =>
        println("returnType = " + returnType)
        println("signature  = " + signature)
        println("structure  = " + returnType.structure)
    }
  }
}
