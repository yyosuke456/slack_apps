// https://qiita.com/seratch/items/1a460c08c3e245b56441

const { App } = require("@slack/bolt");

const app = new App({
  logLevel: "debug", // これはログレベルの調整なので削除しても OK です
  socketMode: true,
  token: "＊＊＊",
  appToken: "＊＊＊",
});

// グローバルショートカット
app.shortcut("socket-mode-shortcut", async ({ ack, body, client }) => {
  await ack();
  await client.views.open({
    trigger_id: body.trigger_id,
    view: {
      type: "modal",
      callback_id: "modal-id",
      title: {
        type: "plain_text",
        text: "タスク登録",
      },
      submit: {
        type: "plain_text",
        text: "送信",
      },
      close: {
        type: "plain_text",
        text: "キャンセル",
      },
      blocks: [
        {
          type: "input",
          block_id: "input-task",
          element: {
            type: "plain_text_input",
            action_id: "input",
            placeholder: {
              type: "plain_text",
              text: "タスクの詳細・期限などを書いてください",
            },
          },
          label: {
            type: "plain_text",
            text: "タスク",
          },
        },
      ],
    },
  });
});

app.view("modal-id", async ({ ack, view, logger }) => {
  logger.info(`Submitted data: ${view.state.values}`);
  await ack();
});

// イベント API
app.message("こんにちは", async ({ message, say }) => {
  await say(`:wave: こんにちは <@${message.user}>！`);
});

(async () => {
  await app.start();
  console.log("⚡️ Bolt app started");
})();
