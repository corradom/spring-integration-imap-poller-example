package example.spring.integration.mail.imap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mail.SearchTermStrategy;
import org.springframework.stereotype.Component;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.NotTerm;
import javax.mail.search.SearchTerm;

@Component
@Slf4j
public class UnreadSearchTermStrategy implements SearchTermStrategy {
    @Override
    public SearchTerm generateSearchTerm(Flags supportedFlags, Folder folder) {
        SearchTerm searchTerm = null;
        if (supportedFlags != null) {
            if (supportedFlags.contains(Flags.Flag.RECENT)) {
                searchTerm = new FlagTerm(new Flags(Flags.Flag.RECENT), true);
            }
            if (supportedFlags.contains(Flags.Flag.ANSWERED)) {
                NotTerm notAnswered = new NotTerm(new FlagTerm(new Flags(Flags.Flag.ANSWERED), true));
                if (searchTerm == null) {
                    searchTerm = notAnswered;
                } else {
                    searchTerm = new AndTerm(searchTerm, notAnswered);
                }
            }
            if (supportedFlags.contains(Flags.Flag.DELETED)) {
                NotTerm notDeleted = new NotTerm(new FlagTerm(new Flags(Flags.Flag.DELETED), true));
                if (searchTerm == null) {
                    searchTerm = notDeleted;
                } else {
                    searchTerm = new AndTerm(searchTerm, notDeleted);
                }
            }
            if (supportedFlags.contains(Flags.Flag.SEEN)) {
                NotTerm notSeen = new NotTerm(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
                if (searchTerm == null) {
                    searchTerm = notSeen;
                } else {
                    searchTerm = new AndTerm(searchTerm, notSeen);
                }
            }
        }

        return searchTerm;
    }
}
