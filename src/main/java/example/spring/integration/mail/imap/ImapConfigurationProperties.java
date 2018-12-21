package example.spring.integration.mail.imap;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;

@Data
@ConfigurationProperties(prefix = "imap")
public class ImapConfigurationProperties {

    @NotBlank
    private String user;

    @NotBlank
    private String pwd;

    @NotBlank
    private String imapUrl;

    private long port = 993;

    @NotBlank
    private String folder;

    @Min(1)
    public long periodicTrigger = 10000;

    @Min(1)
    public long maxMessagesPerPoll = 10;

    @Min(1)
    public int maxFetchSize = 1;

}
