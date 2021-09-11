package info.itsthesky.disky3.core.skript.properties.message;

import info.itsthesky.disky3.api.messages.UpdatingMessage;
import info.itsthesky.disky3.api.skript.PropertyCondition;
import org.jetbrains.annotations.NotNull;

public class MessageIsTTS extends PropertyCondition<UpdatingMessage> {

    static {
        register(
                MessageIsTTS.class,
                PropertyType.BE,
                "(tts|text to speech)",
                "message"
        );
    }

    @Override
    public boolean check(@NotNull UpdatingMessage msg) {
        return msg.getMessage().isTTS();
    }

    @Override
    protected String getPropertyName() {
        return "tts";
    }
}
