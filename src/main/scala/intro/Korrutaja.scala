package intro

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.vdom.html_<^.{<, ^}
import org.scalajs.dom._

object Korrutaja {

  private val Calculations = ScalaFnComponent[List[String]]{ props =>
    def createItem(itemText: String) = <.li(itemText)
    <.ul(props map createItem: _*)
  }

  case class State(x: Int, y: Int, results: List[String])

  class Backend($: BackendScope[Unit, State]) {

    val updateX: StateAccessPure[Int] = $.zoomState(_.x)(value => _.copy(x = value))
    val updateY: StateAccessPure[Int] = $.zoomState(_.y)(value => _.copy(y = value))

    def handler(accessor: StateAccessPure[Int])(e: ReactEventFromInput): CallbackTo[Unit] = {
      accessor.setState(e.target.value.toInt)
    }

    def handleSubmit(e: ReactEventFromInput): CallbackTo[Unit] =
      e.preventDefaultCB >>
        $.modState(s => State(s.x, s.y, s.results :+ s"${s.x} * ${s.y} = ${s.x * s.y}"))

    def render(state: State) =
      <.div(
        Calculations(state.results),
        <.form(^.onSubmit ==> handleSubmit,
          input.text(value := state.x, onChange ==> handler(updateX)),
          input.text(value := state.y, onChange ==> handler(updateY)),
          <.button("Add to history"),
          br,
          s"${state.x} * ${state.y} = ${state.x * state.y}",
        )
      )
  }

  private val component = ScalaComponent.builder[Unit]("Korrutaja")
    .initialState(State(0, 0, Nil))
    .renderBackend[Backend]
    .build

  def renderInto(c: Element): Unit = {
    component().renderIntoDOM(c)
  }
}
