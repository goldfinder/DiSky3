package info.itsthesky.disky3.core.events.bot;

/* public class BotLoad extends DiSkyEvent<ReadyEvent> {

    static {
        DiSkyEvent.register("Bot Load", ReadyEvent.class, BotLoad.class,
                "[discord] bot (load|start)")
                .setName("Bot Load");


        EventValues.registerEventValue(EvtBotLoad.class, Bot.class, new Getter<Bot, EvtBotLoad>() {
            @Override
            public Bot get(EvtBotLoad event) {
                return BotManager.searchFromJDA(event.getJDAEvent().getJDA());
            }
        }, 0);

    }

    public static class EvtBotLoad extends SimpleDiSkyEvent<ReadyEvent> {
        public EvtBotLoad(BotLoad event) { }
    }

} */