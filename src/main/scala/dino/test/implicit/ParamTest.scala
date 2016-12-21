package dino.test.impl

 /**
  * Created by Dino on 2016/12/15.
  * 隐式转换，相当于java中使用工具类或者继承实现
   *  隐式类
   *    隐式类只能定义在另外一个class/object/trait里头
   *    构造器只能带一个不是implicit类型的参数
   *    作用域中不能有与隐式类类名相同的变量/函数名/object名
   *  隐式参数
   *
   * 可识别范围：
   *  1、位于当前作用域内可以单个标识符指代的隐式函数
   *  2、位于源或者目标类型中的隐式函数
   *
   *  可以把一个实例当做另一个实例，这样可以无需修改原来的类就可以将方法附着于对象上
   *  scala一次至多应用一个隐式转换
   *
  */
class ParamTest {

}
// 类隐式扩展
object Context{
  implicit val ccc:String = "implicit"
  implicit val cccdd:String = "implicitdddd"
}

// 参数隐式扩展
object Param{
  def print(content:String)(implicit prefix:String){
    println(prefix+":"+content)
  }
}

object ImplicitDemo {
  def main(s:Array[String])
  {
    //import Context.cccdd
    // 指定隐式转换方法
    Param.print("jack")("hello")
    Param.print("jack")
    testParam  // 调用方法，里面参数为string，找到string的隐式函数获取到参数传入
    testParam("show....")//有参数 直接调用该方法
  }

  def testParam(implicit value:String): Unit ={
    println("name:"+value)
  }
  implicit val name ="Implicit...."
}

