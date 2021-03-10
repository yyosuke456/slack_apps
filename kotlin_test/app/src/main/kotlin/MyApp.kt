import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp
import com.slack.api.model.event.MessageEvent
import com.slack.api.model.kotlin_extension.view.blocks
import com.slack.api.model.view.Views.*

fun main() {
  System.setProperty("org.slf4j.simpleLogger.log.com.slack.api", "debug")

  // SLACK_BOT_TOKEN という環境変数が設定されている前提
  val app = App()

  // イベント API
  app.event(MessageEvent::class.java) { req, ctx ->
    req.event.
    ctx.say("こんにちは <@" + req.event.user + ">！")
    ctx.ack()
  }


  // ショートカットとモーダル
  app.globalShortcut("socket-mode-shortcut") { req, ctx ->
    val modalView = view { v -> v
      .type("modal")
      .callbackId("modal-id")
      .title(viewTitle { it.type("plain_text").text("タスク登録") })
      .submit(viewSubmit { it.type("plain_text").text("送信") })
      .close(viewClose { it.type("plain_text").text("キャンセル") })
      .blocks {
        input {
          blockId("input-task")
          element {
            plainTextInput {
              actionId("input")
              multiline(true)
            }
          }
          label("タスクの詳細・期限などを書いてください")
        }
      }
    }
    ctx.asyncClient().viewsOpen { it.triggerId(req.payload.triggerId).view(modalView) }
    ctx.ack()
  }
  app.viewSubmission("modal-id") { req, ctx ->
    ctx.logger.info("Submitted data: {}", req.payload.view.state.values)
    ctx.ack()
  }

  // SLACK_APP_TOKEN という環境変数が設定されている前提
  SocketModeApp(app).start()
}