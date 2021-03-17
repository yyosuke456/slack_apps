/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package slack.socket.app;

import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.model.view.View;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.input;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.plainTextInput;
import static com.slack.api.model.view.Views.*;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.response.chat.ChatGetPermalinkResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.reactions.ReactionsAddResponse;
import com.slack.api.model.event.MessageEvent;
import com.slack.api.model.event.ReactionAddedEvent;

import java.util.Arrays;
import java.util.regex.Pattern;


public class MyApp {
  public static void main(String[] args) throws Exception {
    System.setProperty("org.slf4j.simpleLogger.log.com.slack.api", "debug");

    // SLACK_BOT_TOKEN という環境変数が設定されている前提
    App app = new App();

    // メッセージがモニタリング対象のキーワードを含むか確認
    Pattern sdk = Pattern.compile(".*[(Java SDK)|(Bolt)|(slack\\-java\\-sdk)].*", Pattern.CASE_INSENSITIVE);
    app.message(sdk, (payload, ctx) -> {
      MessageEvent event = payload.getEvent();
      String text = event.getText();
      MethodsClient client = ctx.client();

      // 👀 のリアクション絵文字をメッセージにつける
      String channelId = event.getChannel();
      String ts = event.getTs();
      ReactionsAddResponse reaction = client.reactionsAdd(r -> r.channel(channelId).timestamp(ts).name("eyes"));
      if (!reaction.isOk()) {
        ctx.logger.error("reactions.add failed: {}", reaction.getError());
      }
      return ctx.ack();
    });

    // SLACK_APP_TOKEN という環境変数が設定されている前提
    new SocketModeApp(app).start();
  }
}
