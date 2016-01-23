package com.github.neunkasulle.chronocommand.view;

import com.github.neunkasulle.chronocommand.control.MessageControl;
import com.github.neunkasulle.chronocommand.model.Message;
import org.hibernate.cfg.NotYetImplementedException;

/**
 * Created by Janze on 20.01.2016.
 */
public class MessageView extends BaseView {
    MessageControl messageControl;

    public MessageView(MessageControl messageControl) {
        this.messageControl = messageControl;
    }

    public void clickSendMessage() {
        messageControl.sendMessage(this.getSessionID(), getReceiverField(), getMessageField());
    }

    public void clickSaveDraft() {

    }

    private String getMessageField() {
        throw new NotYetImplementedException();
    }

    private String getReceiverField() {
        throw new NotYetImplementedException();
    }
}
