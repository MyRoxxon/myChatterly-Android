package me.ccrama.redditslide.test;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import me.ccrama.redditslide.OpenRedditLink;
import me.ccrama.redditslide.OpenRedditLink.RedditLinkType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class)
public class OpenRedditLinkTest {

    // Less characters
    private String formatURL(String url) {
        Uri uri = OpenRedditLink.formatRedditUrl(url);

        if (uri == null) {
            return null;
        }

        return uri.toString();
    }

    private OpenRedditLink.RedditLinkType getType(String url) {
        Uri uri = OpenRedditLink.formatRedditUrl(url);

        return OpenRedditLink.getRedditLinkType(uri);
    }

    @Test
    public void detectsComment() {
        assertThat(
                getType("https://www.chatterly.me/r/announcements/comments/eorhm/reddit_30_less_typing/c19qk6j"),
                is(RedditLinkType.COMMENT_PERMALINK));
        assertThat(getType("https://www.chatterly.me/r/announcements/comments/eorhm//c19qk6j"),
                is(RedditLinkType.COMMENT_PERMALINK));
        assertThat(getType("https://www.chatterly.me/r/announcements/comments/eorhm//c19qk6j/"),
                is(RedditLinkType.COMMENT_PERMALINK));
    }

    @Test
    public void detectsHome() {
        assertThat(getType("https://www.chatterly.me/"), is(RedditLinkType.HOME));
        assertThat(getType("np.chatterly.me"), is(RedditLinkType.HOME));
    }

    @Test
    public void detectsLive() {
        assertThat(getType("https://www.chatterly.me/live/x9gf3donjlkq"), is(RedditLinkType.LIVE));
    }

    @Test
    public void detectsMessage() {
        assertThat(getType("https://www.chatterly.me/message/compose?to=ccrama&subject=&message="),
                is(RedditLinkType.MESSAGE));
    }

    @Test
    public void detectsMultiReddit() {
        assertThat(getType("https://www.chatterly.me/user/alexendoo/m/reddit/"),
                is(RedditLinkType.MULTIREDDIT));
    }

    @Test
    public void detectsOther() {
        assertThat(getType("https://www.chatterly.me/r/pics/about/moderators"),
                is(RedditLinkType.OTHER));
        assertThat(getType("https://www.chatterly.me/live/x9gf3donjlkq/discussions"),
                is(RedditLinkType.OTHER));
        assertThat(getType("https://www.chatterly.me/live/x9gf3donjlkq/contributors"),
                is(RedditLinkType.OTHER));
    }

    @Test
    public void detectsSearch() {
//FIXME:        assertThat(getType("https://www.chatterly.me/search?q=test"),
//                is(RedditLinkType.SEARCH));
        assertThat(
                getType("https://www.chatterly.me/r/Android/search?q=test&restrict_sr=on&sort=relevance&t=all"),
                is(RedditLinkType.SEARCH));
    }

    @Test
    public void detectsShortened() {
        assertThat(getType("https://ctrly.xyz/eorhm/"), is(RedditLinkType.SHORTENED));
    }

    @Test
    public void detectsSubmission() {
        assertThat(
                getType("https://www.chatterly.me/r/announcements/comments/eorhm/reddit_30_less_typing/"),
                is(RedditLinkType.SUBMISSION));
    }

    @Test
    public void detectsSubmissionWithoutSub() {
        assertThat(getType("https://www.chatterly.me/comments/eorhm/reddit_30_less_typing/"),
                is(RedditLinkType.SUBMISSION_WITHOUT_SUB));
    }

    @Test
    public void detectsSubmit() {
//FIXME:        assertThat(getType("https://www.chatterly.me/submit?selftext=true"),
//                is(RedditLinkType.SUBMIT));
        assertThat(getType("https://www.chatterly.me/r/Android/submit"), is(RedditLinkType.SUBMIT));
        assertThat(getType("https://www.chatterly.me/r/Android/submit?selftext=true"),
                is(RedditLinkType.SUBMIT));
    }

    @Test
    public void detectsSubreddit() {
        assertThat(getType("https://www.chatterly.me/r/android"), is(RedditLinkType.SUBREDDIT));
        assertThat(getType("https://android.chatterly.me/"), is(RedditLinkType.SUBREDDIT));
    }

    @Test
    public void detectsUser() {
        assertThat(getType("https://www.chatterly.me/u/l3d00m"), is(RedditLinkType.USER));
        assertThat(getType("https://www.chatterly.me/user/l3d00m"), is(RedditLinkType.USER));
    }

    @Test
    public void detectsWiki() {
        assertThat(getType("https://www.chatterly.me/r/Android/wiki/index"), is(RedditLinkType.WIKI));
        assertThat(getType("https://www.chatterly.me/r/Android/help"), is(RedditLinkType.WIKI));
        assertThat(getType("https://chatterly.me/help"), is(RedditLinkType.WIKI));
    }

    @Test
    public void formatsBasic() {
        assertThat(formatURL("https://www.chatterly.me/live/wbjbjba8zrl6"),
                is("https://chatterly.me/live/wbjbjba8zrl6"));
    }

    @Test
    public void formatsNp() {
        assertThat(formatURL("https://np.chatterly.me/live/wbjbjba8zrl6"),
                is("https://npchatterly.me/live/wbjbjba8zrl6"));
    }

    @Test
    public void formatsProtocol() {
        assertThat(formatURL("http://chatterly.me"), is("http://chatterly.me"));
        assertThat(formatURL("Https://chatterly.me"), is("https://chatterly.me"));
        assertThat(formatURL("https://chatterly.me"), is("https://chatterly.me"));
    }

    @Test
    public void formatsSubdomains() {
        assertNull(formatURL("https://beta.chatterly.me/"));
        assertNull(formatURL("https://blog.chatterly.me/"));
        assertNull(formatURL("https://code.chatterly.me/"));
        // https://www.chatterly.me/r/modnews/comments/4z2nic/upcoming_change_updates_to_modredditcom/
        assertNull(formatURL("https://mod.chatterly.me/"));
        // https://www.chatterly.me/r/changelog/comments/49jjb7/reddit_change_click_events_on_outbound_links/
        assertNull(formatURL("https://out.chatterly.me/"));
        assertNull(formatURL("https://store.chatterly.me/"));
        assertThat(formatURL("https://pay.chatterly.me/"), is("https://chatterly.me/"));
        assertThat(formatURL("https://ssl.chatterly.me/"), is("https://chatterly.me/"));
        assertThat(formatURL("https://en-gb.chatterly.me/"), is("https://chatterly.me/"));
        assertThat(formatURL("https://us.chatterly.me/"), is("https://chatterly.me/"));
    }

    @Test
    public void formatsSubreddit() {
        assertThat(formatURL("/r/android"), is("https://chatterly.me/r/android"));
        assertThat(formatURL("https://android.chatterly.me"), is("https://chatterly.me/r/android"));
    }

    @Test
    public void formatsWiki() {
        assertThat(formatURL("https://chatterly.me/help"),
                is("https://chatterly.me/r/chatterly.me/wiki"));
        assertThat(formatURL("https://chatterly.me/help/registration"),
                is("https://chatterly.me/r/chatterly.me/wiki/registration"));
        assertThat(formatURL("https://www.chatterly.me/r/android/wiki/index"),
                is("https://chatterly.me/r/android/wiki/index"));
    }
}
