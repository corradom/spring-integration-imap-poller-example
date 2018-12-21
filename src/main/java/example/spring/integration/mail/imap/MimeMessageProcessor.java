package example.spring.integration.mail.imap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;

@Slf4j
@Component
public class MimeMessageProcessor {

    public void extractAndProcessMessage(MimeMessage message) {
        try {
            String messageID = message.getMessageID();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            message.writeTo(output);
            String messageRaw = output.toString();

            RawMailMessageDTO rawMailMessageDTO = new RawMailMessageDTO(messageID, messageRaw);

            processRawMailMessageFile(rawMailMessageDTO);

        } catch (Exception e) {
            log.warn("Error on processing message {}", e.getLocalizedMessage());
        }
    }

    private void processRawMailMessageFile(RawMailMessageDTO rawMailMessageDTO) {
        log.debug("Processing message {}", rawMailMessageDTO.getMessageId());
    }

}
