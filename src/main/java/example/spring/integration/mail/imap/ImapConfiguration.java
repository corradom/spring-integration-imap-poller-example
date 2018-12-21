package example.spring.integration.mail.imap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceivingMessageSource;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

@Slf4j
@Configuration
@EnableConfigurationProperties(ImapConfigurationProperties.class)
@RequiredArgsConstructor
public class ImapConfiguration {

    private final ImapConfigurationProperties configurationProperties;

    @Bean
    @InboundChannelAdapter(value = "outputChannel", poller = @Poller(value = "pollerMetadata"))
    public MessageSource mailMessageSource(ImapMailReceiver imapMailReceiver) {
        return new MailReceivingMessageSource(imapMailReceiver);
    }

    @Bean
    PollerMetadata pollerMetadata(PeriodicTrigger trigger) {
        PollerMetadata poller = new PollerMetadata();
        poller.setMaxMessagesPerPoll(configurationProperties.getMaxMessagesPerPoll());
        poller.setTrigger(trigger);
        return poller;
    }

    @Bean
    PeriodicTrigger periodicTrigger() {
        return new PeriodicTrigger(configurationProperties.periodicTrigger);
    }

    @Bean
    DirectChannel outputChannel(@Qualifier(value = "MimeMailMessageHandler") MessageHandler messageHandler) {
        log.debug("Initializing output channel {}", messageHandler);
        DirectChannel channel = new DirectChannel();
        channel.subscribe(messageHandler);
        return channel;
    }

    @Bean
    ImapMailReceiver imapMailReceiver() throws UnsupportedEncodingException {
        String url = "imaps://" + URLEncoder.encode(configurationProperties.getUser(), "UTF-8") + ":" + configurationProperties.getPwd() + "@" + configurationProperties.getImapUrl() + ":" + configurationProperties.getPort() + "/" + configurationProperties.getFolder();

        log.debug("Initializing mailReceiver connecting with url {}", url);

        ImapMailReceiver mailReceiver = new ImapMailReceiver(url);
        mailReceiver.setJavaMailProperties(javaMailProperties());
        mailReceiver.setShouldMarkMessagesAsRead(true);
        mailReceiver.setShouldDeleteMessages(false);
        mailReceiver.setSearchTermStrategy(new UnreadSearchTermStrategy());
        mailReceiver.setMaxFetchSize(configurationProperties.getMaxFetchSize());
        return mailReceiver;
    }

    private Properties javaMailProperties() {
        Properties javaMailProperties = new Properties();

        javaMailProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.setProperty("mail.imap.socketFactory.fallback", "false");
        javaMailProperties.setProperty("mail.store.protocol", "imaps");
        javaMailProperties.setProperty("mail.debug", "false");

        return javaMailProperties;
    }
}
