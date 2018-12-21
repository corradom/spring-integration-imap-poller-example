package example.spring.integration.mail.imap;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Slf4j
@Component(value = "ImapMessageHandler")
@Qualifier(value = "MimeMailMessageHandler")
public class MimeMailMessageHandler implements MessageHandler {

    @NonNull
    MimeMessageProcessor processor;

    public MimeMailMessageHandler(@NonNull MimeMessageProcessor imapMailExtractJobProcessor) {
        this.processor = imapMailExtractJobProcessor;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        log.debug("received message {}", message);
        MimeMessage mimeMessage = (MimeMessage) message.getPayload();
        processor.extractAndProcessMessage(mimeMessage);
    }
}
