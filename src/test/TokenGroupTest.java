import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class TokenGroupTest {

    @Autowired
    private TokenGroup tokenGroup;

    private static final String CN = "Some canonical name";
    private static final int RANDOM_NUMBER = 100; 

    @Test
    public void testGetTokenGroups() throws Exception {

        List<String> sidList = tokenGroup.getTokenGroups(CN);
        assertThat(sidList).isNotEmpty();
        assertThat(sidList.size()).isEqualTo(RANDOM_NUMBER);

    }
}
