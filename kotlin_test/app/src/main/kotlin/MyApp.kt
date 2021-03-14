import com.slack.api.bolt.App
import com.slack.api.bolt.context.Context
import com.slack.api.bolt.context.builtin.SlashCommandContext
import com.slack.api.bolt.jetty.SlackAppServer
import com.slack.api.methods.request.conversations.ConversationsListRequest
import com.slack.api.model.ConversationType

fun main() {

    // export SLACK_BOT_TOKEN=xoxb-***
    // export SLACK_SIGNING_SECRET=123abc***
    val app = App()

    app.message("湯婆婆") {req, ctx -> 
      ctx.ack(":wave: Hello!");
    };
    
    val server = SlackAppServer(app)
    server.start()
}