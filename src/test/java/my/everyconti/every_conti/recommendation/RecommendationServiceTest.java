package my.everyconti.every_conti.recommendation;

import jakarta.transaction.Transactional;
import my.everyconti.every_conti.common.utils.HashIdUtil;
import my.everyconti.every_conti.modules.recommendation.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class RecommendationServiceTest {


    @Autowired
    private HashIdUtil hashIdUtil;
    @Autowired
    private RecommendationService recommendationService;
//

//    @Test
//    @Commit
//    public void batchTest(){
//        recommendationService.runCoUsedSongStatBatch();
//    }

    @Test
    public void getCoUsedSongs(){
        System.out.println(recommendationService.getCoUsedSongs(hashIdUtil.encode(18L)));
    }
}